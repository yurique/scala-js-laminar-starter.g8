import sbtcrossproject.CrossPlugin.autoImport.crossProject
import sbtcrossproject.CrossPlugin.autoImport.CrossType

organization in ThisBuild := "$organization$"

name := "$name$"

version in ThisBuild := "$version$"

scalaVersion in ThisBuild := Settings.versions.scala

scalacOptions in ThisBuild ++= Settings.scalacOptions

scalafmtOnCompile in ThisBuild := true

val sharedSettings = Seq(
  addCompilerPlugin(
    "org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full
  ),
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
  // for Scala 2.12 and lower
  // addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
  // ---
)

lazy val shared =
  (crossProject(JSPlatform, JVMPlatform).crossType(CrossType.Pure) in file(
    "modules/shared"
  )).disablePlugins(RevolverPlugin)
    .settings(
      libraryDependencies ++= Settings.sharedDependencies.value
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
  (crossProject(JSPlatform).crossType(CrossType.Pure) in file(
    "modules/jest"
  )).disablePlugins(RevolverPlugin)
    .jsSettings(
      scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
    )
    .settings(sharedSettings)
    .dependsOn(shared)

lazy val frontend =
  (crossProject(JSPlatform).crossType(CrossType.Pure) in file(
    "modules/frontend"
  )).disablePlugins(RevolverPlugin)
    .jsSettings(
//      Test / jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv(),
//      Test / scalaJSUseTestModuleInitializer := false,
//      Test / scalaJSUseMainModuleInitializer := true,
      Test / fastOptJS / artifactPath :=
        ((crossTarget in fastOptJS).value /
          ((moduleName in fastOptJS).value + "-fastopt.test.js")),
      Test / fullOptJS / artifactPath :=
        ((crossTarget in fullOptJS).value /
          ((moduleName in fullOptJS).value + "-opt.test.js")),
      testDev := {
        (fastOptJS in Test).value
        runJest()
      },
      testProd := {
        (fullOptJS in Test).value
        runJest()
      },
      scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
      libraryDependencies ++= Settings.frontendDependencies.value
    )
    .settings(sharedSettings)
    .dependsOn(shared, jest % Test)

lazy val backend =
  (project in file("modules/backend"))
    .enablePlugins(JavaAppPackaging)
    .settings(Revolver.settings.settings)
    .settings(
      libraryDependencies ++= Settings.backendDependencies.value,
      mainClass in reStart := Some("starter.boot.Boot"),
      reLogTag := ""
    )
    .settings(sharedSettings)
    .dependsOn(shared.jvm)
