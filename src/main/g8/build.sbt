import sbtcrossproject.CrossPlugin.autoImport.crossProject
import sbtcrossproject.CrossPlugin.autoImport.CrossType

inThisBuild(
  List(
    organization := "$organization$",
    version := "$version$",
    scalaVersion := DependencyVersions.scala,
    scalafmtOnCompile := true
  )
)

name := "$name$"

val sharedSettings = Seq.concat(
  ScalaOptions.fixOptions,
  Seq(
    addCompilerPlugin(
      ("org.typelevel" %% "kind-projector" % "0.13.0").cross(CrossVersion.full)
    ),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
    // for Scala 2.12 and lower
    // addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
    // ---
  )
)

lazy val shared =
  (crossProject(JSPlatform, JVMPlatform).crossType(CrossType.Pure) in file(
    "modules/shared"
  )).disablePlugins(RevolverPlugin)
    .settings(
      libraryDependencies ++= Seq.concat(
        Dependencies.scribe.value,
        Dependencies.circe.value,
        Dependencies.newtype.value,
        Dependencies.unindent.value
      )
    )
    .settings(sharedSettings)

val TEST_FILE = s"./sjs.test.js"

val testDev  = Def.taskKey[Unit]("test in dev mode")
val testProd = Def.taskKey[Unit]("test in prod mode")

def runJest(): Unit = {
  import sys.process._
  val jestResult = """yarn test --colors""".!
  if (jestResult != 0) throw new IllegalStateException("Jest Suite failed")
}

lazy val jest =
  project
    .in(file("modules/jest"))
    .disablePlugins(RevolverPlugin)
    .enablePlugins(ScalaJSPlugin)
    .settings(
      scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) }
    )
    .settings(sharedSettings)
    .settings(
      libraryDependencies ++= Seq("org.scala-lang" % "scala-reflect" % scalaVersion.value)
    )
    .dependsOn(shared.js)

lazy val frontend =
  project
    .in(
      file("modules/frontend")
    )
    .enablePlugins(ScalaJSPlugin)
    .disablePlugins(RevolverPlugin)
    .settings(
      scalaJSUseMainModuleInitializer := true,
//      Test / jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv(),
//      Test / scalaJSUseTestModuleInitializer := false,
//      Test / scalaJSUseMainModuleInitializer := true,
      Test / fastOptJS / artifactPath :=
        ((fastOptJS / crossTarget).value /
          ((fastOptJS / moduleName).value + "-fastopt.test.js")),
      Test / fullOptJS / artifactPath :=
        ((fullOptJS / crossTarget).value /
          ((fullOptJS / moduleName).value + "-opt.test.js")),
      testDev := {
        (Test / fastOptJS).value
        runJest()
      },
      testProd := {
        (Test / fullOptJS).value
        runJest()
      },
      scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
      libraryDependencies ++= Seq.concat(
        Dependencies.laminar.value,
        Dependencies.laminext.value,
        Dependencies.`url-dsl`.value,
        Dependencies.waypoint.value,
        Dependencies.`dom-test-utils`.value
      )
    )
    .settings(sharedSettings)
    .dependsOn(shared.js, jest % Test)

lazy val backend =
  project
    .in(file("modules/backend"))
    .enablePlugins(JavaAppPackaging)
    .settings(Revolver.settings.settings)
    .settings(
      libraryDependencies ++= Seq.concat(
        Dependencies.`akka-http`.value,
        Dependencies.`akka-http-json`.value,
        Dependencies.akka.value,
        Dependencies.`typesafe-config`.value,
        Dependencies.`circe-config`.value
      ),
      reStart / mainClass := Some("starter.boot.Boot"),
      reLogTag := ""
    )
    .settings(sharedSettings)
    .dependsOn(shared.jvm)
