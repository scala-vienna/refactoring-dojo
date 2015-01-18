package org.scalavienna.refactoringdojo.gameoflife

import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.scalatest.exceptions.TestFailedException
import scala.Array.canBuildFrom
import scala.Array.fallbackCanBuildFrom

/**
 * As a general rule, don't remove test-cases. You are free to add, though.
 * However, consider these as acceptance tests. No need to be exhaustive here.
 * 
 * Rely on your own unit-tests for in-depth coverage of your components.
 * */
class AcceptanceTests extends FunSuite with Matchers with AcceptanceTester {

  test("An isolated cell dies") {
    assertGoL(
      in = """ ....
               .O..
               ....
               ....""",
      exp = """....
               ....
               ....
               ....""")
  }

  test("A cell with only 1 neighbour dies") {
    assertGoL(
      in = """ ....
               .O..
               .O..
               ....""",
      exp = """....
               ....
               ....
               ....""")
  }

  test("A cell with 2 neighbours lives") {
    assertGoL(
      in = """ O...
               .O..
               ..O.
               ....""",
      exp = """....
               .O..
               ....
               ....""")
  }

  test("Acceptance test (covers all rules)") {
    assertGoL(
      in = """ O...
               .O..
               O.O.
               ....""",
      exp = """....
               OO..
               .O..
               ....""")
  }

}

/**
 * You may only change the *implementation* of the "calculate" 
 * method, NOT the type signature. Don't add methods here.
 */
trait TestGameCalculator {
  
  /**
   * This method is the bridge between the input
   * of the acceptance-tests and your real implementation
   * of the game-of-life algorithm.
   * 
   * It should accept a String like as in the in = """...
   * and produce a next iteration as expressed in the 
   * exp = """... 
   * ("in" stands for "input", "exp" stands for "expected").
   *  
   * You may change the code inside this method (you
   * will actually have to). But leave the signature as-is.
   * 
   * Try to keep this method as anemic as possible, 
   * delegating as much as possible to your own
   * components.
   * 
   * Ideally, it should consist of just one or two lines.
   * */
  def calculate(in: String): String = {
    val world = new GoL()
    world.from_string ( in )
    world.calcNxtIter
    world.to_str
  }
  
}

/**
 * You should not need to change anything here.
 * Try to leave this trait as-is. It's an implementation detail.
 * */
trait AcceptanceTester extends TestGameCalculator { self: FunSuite with Matchers =>

  private def parse(str: String): Seq[String] = {
    str.split("\n").map(_.trim())
  }

  def assertGoL(in: String, exp: String): Unit = {
    val out = calculate(in)
    if (parse(exp) != parse(out)) {
      def adjust(str: String) = str.split("\n").map(s => s"\t\t${s.trim()}").mkString("\n")
      val adjustedIn = adjust(in)
      val adjustedExp = adjust(exp)
      val adjustedOut = adjust(out)
      val msg = s"""Result did not match expectation:
        |Input:
        |${adjustedIn}
        |Expected:
        |${adjustedExp}
        |Got:
        |${adjustedOut}""".stripMargin
      throw new TestFailedException(msg, 4)
    }
  }
}
