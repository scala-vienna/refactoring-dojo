package org.scalavienna.refactoringdojo.gameoflife

import scala.collection.mutable.ArrayBuffer

/**
 * You can change ANYTHING you want here (and you *really* should!)
 * Feel free to use other concepts, other names, approaches, etc.
 * Feel free to change the file-structure as well (use more files, etc.)
 */

case class Pos(x: Int, y: Int)

class World {

  var size: Int = 0
  var live_cells: Seq[Pos] = Seq()

  def from_string(in: String) = {

    //// Some helper variables

    var s = 0;
    var j = 0;
    var l = ""
    var i = 0;
    var c = 'x';

    var lines: Array[String] = Array()

    //// Parse lines

    var ss1 = in.split("\n")
    var ss2 = ArrayBuffer[String]()
    while (i < ss1.size) {
      l = ss1(i)
      l = l.trim()
      ss2.appendAll(Seq(l))
      i = i + 1
    }

    lines = ss2.toArray

    //// Calculate size of world from lines

    i = 0;
    while (i < lines.size) {
      l = lines(i)
      j = 0;
      s = 0;
      while (j < l.size) {
        j = j + 1;
      }
      s = j
      i = i + 1;
    }
    size = s

    //// Find live-cell positions from lines

    var x_arr = ArrayBuffer[Int]()
    var y_arr = ArrayBuffer[Int]()
    var dead_cells = 0

    j = 0;
    while (j < lines.size) {
      i = 0;
      l = lines(j)
      while (i < l.size) {
        c = l.charAt(i)
        if (c == 'O') {
          x_arr.append(i + 1)
          if (c == 'O') {
            y_arr.append(j + 1)
          } else {
            dead_cells += 1
          }
        } else {
          dead_cells += 1
        }
        i = i + 1
      }
      j = j + 1
    }

    var lc_arr = ArrayBuffer[Pos]()
    i = 0;
    while (i < x_arr.size) {
      var x = x_arr(i)
      var y = y_arr(i)
      lc_arr += Pos(x, y)
      i = i + 1
    }

    live_cells = lc_arr.toSeq

  }

  def next_iteration: World = {
    val new_world = new World()
    new_world.size = this.size
    new_world.live_cells = ArrayBuffer[Pos]()
    var i = 0;
    while (i < size) {
      i = i + 1;
      var y = i;
      for {
        x <- 1 until size
      } yield {
        if (will_have_live_cell_at(Pos(x, y)))
          new_world.live_cells.asInstanceOf[ArrayBuffer[Pos]].append(Pos(x, y))
        else
          Pos(x, y)
      }
    }
    new_world.live_cells = new_world.live_cells.toSeq
    new_world
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
    var p1 = Pos(pos.x - 1, pos.y - 1);
    var p2 = Pos(pos.x + 0, pos.y - 1);
    var p3 = Pos(pos.x + 1, pos.y - 1);
    var p4 = Pos(pos.x - 1, pos.y + 0);
    var p5 = Pos(pos.x + 1, pos.y + 0);
    var p6 = Pos(pos.x - 1, pos.y + 1);
    var p7 = Pos(pos.x + 0, pos.y + 1);
    var p8 = Pos(pos.x + 1, pos.y + 1);

    var with_wor_arr = ArrayBuffer[Pos]()

    if (within_world(p1))
      with_wor_arr += p1
    if (within_world(p2))
      with_wor_arr += p2
    if (within_world(p3))
      with_wor_arr += p3
    if (within_world(p4))
      with_wor_arr += p4
    if (within_world(p5))
      with_wor_arr += p5
    if (within_world(p6))
      with_wor_arr += p6
    if (within_world(p7))
      with_wor_arr += p7
    if (within_world(p8))
      with_wor_arr += p8

    var nc = 0;

    if (has_live_cell_at(p1))
      nc = nc + 1
    if (has_live_cell_at(p2))
      nc = nc + 1
    if (has_live_cell_at(p3))
      nc = nc + 1
    if (has_live_cell_at(p4))
      nc = nc + 1
    if (has_live_cell_at(p5))
      nc = nc + 1
    if (has_live_cell_at(p6))
      nc = nc + 1
    if (has_live_cell_at(p7))
      nc = nc + 1
    if (has_live_cell_at(p8))
      nc = nc + 1

    return nc;
  }

  private def within_world(pos: Pos): Boolean = {
    if (pos.y > 0) {
      if (pos.y > 1 || pos.y == 1) {
        if (pos.y < size || pos.y == size) {
          if (!(pos.x <= 0)) {
            if (pos.x > 1 || pos.x == 1) {
              return if (pos.x < size || pos.x == size) true else false;
            } else {
              return false;
            }
          } else {
            return false;
          }
        } else {
          return false;
        }
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

}

object WorldFormatter {

  def format(world: World): String = {
    var i = 0;
    var l = "";
    while (i < world.size) {
      var j = 0;
      while (j < world.size) {
        var c = '_';
        world.has_live_cell_at(Pos(j + 1, i + 1)) match {
          case true => c = 'O'
          case false => c = '.'
        }
        l = s"$l$c";
        j = j + 1;
      }
      l = l + '\n'
      i = i + 1;
    }
    l.substring(0, l.size - 1)
  }

}
