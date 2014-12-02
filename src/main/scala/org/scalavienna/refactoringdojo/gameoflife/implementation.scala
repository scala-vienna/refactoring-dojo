package org.scalavienna.refactoringdojo.gameoflife

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map

/**
 * You can change ANYTHING you want here (and you *really* should!)
 * Feel free to use other concepts, other names, approaches, etc.
 * Feel free to change the file-structure as well (use more files, etc.)
 * Feel free to introduce new classes, case-classes, objects, etc.
 * Finally: refactor mercilessly!
 */
class GoL {

  // This Map should have only keys "x" and "y"!!!
  // It is used to store coordinates.
  // They are 1-based.
  type Pos = Map[String, Int]

  //// Properties / Fields
  var size: Int = 0
  var live_cells: Seq[Map[String, Int]] = ArrayBuffer[Map[String, Int]]()

  //// Initialization from String
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
      live_cells.asInstanceOf[ArrayBuffer[Pos]] += Map[String, Int]("x" -> x, "y" -> y)
      i = i + 1
    }

  }

  // Calculate next iteration
  def calcNxtIter {
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
          val nc1 = countAround(Map[String, Int]("x" -> x, "y" -> y))
          nc1 == 2 || nc1 == 3
        }
        // has currently cell at position?
        var c2 = livcell(Map[String, Int]("x" -> x, "y" -> y))
        var c4 = {
          // enough for new cell to be born?
          var nc2 = countAround(Map[String, Int]("x" -> x, "y" -> y))
          nc2 == 3
        }
        //// c3: Will cell be added?
        var c3 = true

        if (c2 == 1) {
          c3 = c1
        } else
          c3 = c4

        if (c3)
          new_cells.append(Map[String, Int]("x" -> x, "y" -> y))
        else
          Map[String, Int]("x" -> x, "y" -> y)
      }
    }
    //// Copy cells to existing instance
    live_cells.asInstanceOf[ArrayBuffer[Pos]].clear()
    while (j < new_cells.size) {
      live_cells.asInstanceOf[ArrayBuffer[Pos]].append(new_cells.toList.apply(j))
      j = j + 1;
    }

  }

  // Is there a live cell at the given position?
  // If yes, returns 1
  private def livcell(p: Pos): Int = {
    try {
      var i = 0;
      while (i < live_cells.size) {
        var c = live_cells(i)
        if (c("x") == p("x") && c("y") == p("y")) {
          throw new RuntimeException("found!") // break from loop
        }
        i = i + 1;
      }
    } catch {
      case _: RuntimeException => return 1;
    }
    return -1;
  }

  // Produce a formatted world
  def to_str = {
    var i = 0;
    var l = "";
    while (i < this.size) {
      var j = 0;
      while (j < size) {
        var c = '_';
        this.livcell(Map[String, Int]("x" -> (j + 1), "y" -> (i + 1))) match {
          case 1 => c = 'O'
          case -1 => c = '.'
          case _ => {}
        }
        l = s"$l$c";
        j = j + 1;
      }
      l = s"$l\n"
      i = i + 1;
    }
    // Remove last \n
    l.substring(0, l.size - 1)
  }

  // Count live neighbours around position
  private def countAround(p: Pos): Int = {
    var p1 = Map[String, Int]("x" -> (p("x") - 1), "y" -> (p("y") - 1));
    var p2 = Map[String, Int]("x" -> (p("x") + 0), "y" -> (p("y") - 1));
    var p3 = Map[String, Int]("x" -> (p("x") + 1), "y" -> (p("y") - 1));
    var p4 = Map[String, Int]("x" -> (p("x") - 1), "y" -> (p("y") + 0));
    var p5 = Map[String, Int]("x" -> (p("x") + 1), "y" -> (p("y") + 0));
    var p6 = Map[String, Int]("x" -> (p("x") - 1), "y" -> (p("y") + 1));
    var p7 = Map[String, Int]("x" -> (p("x") + 0), "y" -> (p("y") + 1));
    var p8 = Map[String, Int]("x" -> (p("x") + 1), "y" -> (p("y") + 1));

    // target for positions within world
    var with_wor_arr = ArrayBuffer[Map[String, Int]]()

    // Filter positions which are within world

    if (within(p1) >= 0)
      with_wor_arr += p1
    if (within(p2) >= 0)
      with_wor_arr += p2
    if (!(within(p3) < 0))
      with_wor_arr += p3
    if (within(p4) >= 0)
      with_wor_arr += p4
    if (within(p5) == 0)
      with_wor_arr += p5
    if (within(p6) >= 0)
      with_wor_arr += p6
    if (within(p7) >= 0)
      with_wor_arr += p7
    if (within(p8) >= 0)
      with_wor_arr += p8

    // target for neighbour count
    var nc = 0;

    // Count live neighbours

    if (livcell(p1) == 1)
      nc = nc + 1
    if (livcell(p2) > 0)
      nc = nc + 1
    if (livcell(p3) > 0)
      nc = nc + 1
    if (livcell(p4) == 1)
      nc = nc + 1
    if (livcell(p5) > 0)
      nc = nc + 1
    if (livcell(p6) == 1)
      nc = nc + 1
    if (livcell(p7) == 1)
      nc = nc + 1
    if (livcell(p8) == 1)
      nc = nc + 1

    return nc;
  }

  // Is the position within world / board?
  // If yes, returns 0.
  private def within(pos: Pos): Int = {
    if (pos("y") > 0) {
      if (pos("y") > 1 || pos("y") == 1) {
        if (pos("y") < size || pos("y") == size) {
          if (!(pos("x") <= 0)) {
            if (pos("x") > 1 || pos("x") == 1) {
              return if (pos("x") < size || pos("x") == size) 0 else -1;
            } else {
              return -2;
            }
          } else {
            return -3;
          }
        } else {
          -4;
        }
      } else {
        return -5;
      }
    } else {
      return -6;
    }
  }

}

