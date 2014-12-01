package org.scalavienna.refactoringdojo.gameoflife

import scala.collection.mutable.ArrayBuffer

/**
 * You can change ANYTHING you want here (and you *really* should!)
 * Feel free to use other concepts, other names, approaches, etc.
 * Feel free to change the file-structure as well (use more files, etc.)
 */

case class Pos(x: Int, y: Int)

class GameOfLife {

  //// PRoperties / Fields
  var size: Int = 0
  var live_cells: Seq[Pos] = ArrayBuffer[Pos]()

  //// Initializator from String
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

    //// Copy found live-cells to instance field
    
    i = 0;
    live_cells.asInstanceOf[ArrayBuffer[Pos]].clear()
    while (i < x_arr.size) {
      var x = x_arr(i)
      var y = y_arr(i)
      live_cells.asInstanceOf[ArrayBuffer[Pos]] += Pos(x, y)
      i = i + 1
    }

  }

  def next_iteration {
    var new_cells = ArrayBuffer[Pos]()
    var i = 0;
    var j = 0;
    //// Calculate new cells
    while (i < size) {
      i = i + 1;
      var y = i;
      for {
        x <- 1 until size
      } yield {
        //// Conditions
        var c1 = {
          // enough neighbours to live on?
          val nc1 = neighbours_count_for(Pos(x, y))
          nc1 == 2 || nc1 == 3
        }
        var c2 = has_live_cell_at(Pos(x, y))
        var c4 = {
          // enough for new cell to be born?
          var nc2 = neighbours_count_for(Pos(x, y))
          nc2 == 3
        }
        //// React to conditions
        var c3 = if (c2) c1 else c4
        if (c3)
          new_cells.append(Pos(x, y))
        else
          Pos(x, y)
      }
    }
    //// Copy to existing instance
    live_cells.asInstanceOf[ArrayBuffer[Pos]].clear()
    while (j < new_cells.size) {
      live_cells.asInstanceOf[ArrayBuffer[Pos]].append(new_cells.toList.apply(j))
      j = j + 1;
    }

  }

  def has_live_cell_at(pos: Pos): Boolean = {
    try {
      var i = 0;
      while (i < live_cells.size) {
        var c = live_cells(i)
        if (c.x == pos.x && c.y == pos.y) {
          throw new RuntimeException("found!")
        }
        i = i + 1;
      }
    } catch {
      case _: RuntimeException => return true;
    }
    return false;
  }

  def to_str = {
    var i = 0;
    var l = "";
    while (i < this.size) {
      var j = 0;
      while (j < size) {
        var c = '_';
        this.has_live_cell_at(Pos(j + 1, i + 1)) match {
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

