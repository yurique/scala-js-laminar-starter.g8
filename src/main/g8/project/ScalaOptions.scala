import sbt.Keys._
import sbt._

object ScalaOptions {
  val fixOptions = Seq(
    scalacOptions ~= (_.filterNot(
      Set(
        "-Wdead-code",
        "-Wunused:implicits",
        "-Wunused:explicits",
        "-Wunused:imports",
        "-Wunused:params",
        "-Wunused:privates"
      )
    )),
    scalacOptions ++= CrossVersion.partialVersion(scalaVersion.value).collect { case (2, _) =>
      "-Ymacro-annotations"
    }
  )

}
