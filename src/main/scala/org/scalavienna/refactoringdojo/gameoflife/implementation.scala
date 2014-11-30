package org.scalavienna.refactoringdojo.gameoflife

import scala.collection.mutable.ArrayBuffer

/**
 * You can change ANYTHING you want here.
 * Feel free to use other concepts, other names, approaches, etc.
 * Feel free to change the file-structure as well (use more files, etc.)
 */

case class Pos(x: Int, y: Int) {
  def surrounding_positions: Seq[Pos] = {
    val deltas =
      Seq(
        (-1, -1), (+0, -1), (+1, -1),
        (-1, +0), /*     */ (+1, +0),
        (-1, +1), (+0, +1), (+1, +1))
    deltas.map {
      case (delta_x, delta_y) =>
        Pos(x + delta_x, y + delta_y)
    }
  }
}

case class World(size: Int, live_cells: Seq[Pos]) {

  val range = 1 until size

  def next_iteration: World = {
    val future_living_cells =
      for {
        y <- range
        x <- range
        if will_have_live_cell_at(Pos(x, y))
      } yield {
        Pos(x, y)
      }
    World(size, future_living_cells)
  }

  def has_live_cell_at(pos: Pos): Boolean = {
    live_cells.exists(some_cell_pos => some_cell_pos == pos)
  }

  private def will_have_live_cell_at(pos: Pos): Boolean = {
    if (has_live_cell_at(pos)) {
      will_cell_live_on_at(pos)
    } else {
      will_new_cell_be_born_at(pos)
    }
  }

  private def will_cell_live_on_at(pos: Pos): Boolean = {
    val neighbours = neighbours_count_for(pos)
    neighbours == 2 || neighbours == 3
  }

  private def will_new_cell_be_born_at(pos: Pos): Boolean = {
    val neighbours = neighbours_count_for(pos)
    neighbours == 3
  }

  private def neighbours_count_for(pos: Pos): Int = {
    val candidates = pos.surrounding_positions
    candidates.filter(within_world).count(has_live_cell_at)
  }

  private def within_world(pos: Pos): Boolean = {
    range.contains(pos.y) && range.contains(pos.x)
  }

}

object WorldParser {

  def from_string(in: String): World = {
    val lines = parse_lines(in)
    val size = get_size(lines)

    val live_cells = ArrayBuffer[Pos]()

    for {
      (line, y) <- lines.zipWithIndex
      (ch, x) <- line.zipWithIndex
    } {
      if (ch == 'O')
        live_cells += Pos(x + 1, y + 1)
    }

    new World(size, live_cells)
  }

  private def get_size(lines: Seq[String]): Int = {
    val w = get_width(lines)
    val h = get_height(lines)
    assert(w == h, s"Width and height should be equal (w: $w, h: $h)")
    w
  }

  private def get_width(lines: Seq[String]): Int = {
    lines.head.size
  }

  private def get_height(lines: Seq[String]): Int = {
    lines.size
  }

  private def parse_lines(str: String): Seq[String] = {
    str.split("\n").map(_.trim())
  }

}

object WorldFormatter {

  def format(world: World): String = {
    val charLines = for (y <- 1 to world.size) yield {
      for (x <- 1 to world.size) yield {
        if (world.has_live_cell_at(Pos(x, y))) 'O' else '.'
      }
    }
    charLines.map(_.mkString).mkString("\n")
  }

}
