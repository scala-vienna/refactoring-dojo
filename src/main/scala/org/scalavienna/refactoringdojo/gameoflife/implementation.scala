package org.scalavienna.refactoringdojo.gameoflife

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map

/**
 * This is a dummy implementation just to test that the setup is working correctly.
 * It will cause all tests to fail. This is normal.
 * 
 * At the Dojo, you will be provided here with a working but very ugly implementation.
 * 
 * To practice and warm-up, feel free to come up with a working implementation of
 * your own.
 */
class GoL {
  
  var input = ""

  //// Initialization from String
  def from_string(in: String) = {
    // (mega lazy, just saves the input as-is)
    input = in
  }
  
  // Calculate next-iteration
  def calcNxtIter() {
    // (does nothing, really)
  }

  // Produce a formatted world
  def to_str = {
    // (actually just returns the input unchanged)
    input
  }

}

