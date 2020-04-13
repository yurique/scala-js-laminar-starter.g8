package scalajsjest

import scala.language.experimental.macros

object JestRunner {

  def run(): Unit = macro JestMacro.runMacroImpl

}
