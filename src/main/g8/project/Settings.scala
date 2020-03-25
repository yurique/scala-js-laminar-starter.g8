import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt._

object Settings {

  object versions {
    val scala = "2.13.1"
    val laminar = "0.8.0"
    val `url-dsl` = "0.2.0"
    val waypoint = "0.1.0"
    val akka = "2.6.4"
    val `akka-http` = "10.1.11"
    val `akka-http-json` = "1.31.0"
    val circe = "0.13.0"
    val `circe-derivation` = "0.13.0-M3"
    val `typesafe-config` = "1.4.0"
    val pureconfig = "0.12.3"
    val scribe = "2.7.12"
    val newtype = "0.4.3"
    val uTest = "0.6.6"
  }

  val scalacOptions = Seq(
    "-target:jvm-1.8",
    "-unchecked",
    "-deprecation",
    "-feature",
    "-Xlint:nullary-unit,inaccessible,nullary-override,infer-any,missing-interpolator,private-shadow,type-parameter-shadow,poly-implicit-overload,option-implicit,delayedinit-select,stars-align",
    "-Xcheckinit",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-Ywarn-value-discard",
    // for Scala 2.13 only
    "-Ymacro-annotations",
    // ---
    "-encoding",
    "utf8"
  )

  object libs {

    val laminar: Def.Initialize[Seq[ModuleID]] = Def.setting {
      Seq(
        "com.raquo" %%% "laminar" % versions.laminar
      )
    }

    val `url-dsl`: Def.Initialize[Seq[ModuleID]] = Def.setting {
      Seq(
        "be.doeraene" %%% "url-dsl" % versions.`url-dsl`
      )
    }

    val waypoint: Def.Initialize[Seq[ModuleID]] = Def.setting {
      Seq(
        "com.raquo" %%% "waypoint" % versions.waypoint
      )
    }

    val uTest: Def.Initialize[Seq[ModuleID]] = Def.setting {
      Seq(
        "com.lihaoyi" %%% "utest" % versions.uTest
      )
    }

    val `akka-http`: Def.Initialize[Seq[ModuleID]] = Def.setting {
      Seq(
        "com.typesafe.akka" %%% "akka-http" % versions.`akka-http`
      )
    }

    val `akka-http-json`: Def.Initialize[Seq[ModuleID]] = Def.setting {
      Seq(
        "de.heikoseeberger" %%% "akka-http-circe" % versions.`akka-http-json`
      )
    }

    val akka: Def.Initialize[Seq[ModuleID]] = Def.setting {
      Seq(
        "com.typesafe.akka" %%% "akka-actor" % versions.akka,
        "com.typesafe.akka" %%% "akka-actor-typed" % versions.akka,
        "com.typesafe.akka" %%% "akka-stream" % versions.akka,
        "com.typesafe.akka" %%% "akka-testkit" % versions.akka % Test
      )
    }

    val `typesafe-config`: Def.Initialize[Seq[ModuleID]] = Def.setting {
      Seq(
        "com.typesafe" % "config" % versions.`typesafe-config`
      )
    }

    val pureconfig: Def.Initialize[Seq[ModuleID]] = Def.setting {
      Seq(
        "com.github.pureconfig" %%% "pureconfig" % versions.pureconfig
      )
    }

    val circe: Def.Initialize[Seq[ModuleID]] = Def.setting {
      Seq(
        "io.circe" %%% "circe-core" % versions.circe,
        "io.circe" %%% "circe-generic" % versions.circe,
        "io.circe" %%% "circe-derivation" % versions.`circe-derivation`,
        "io.circe" %%% "circe-derivation-annotations" % versions.`circe-derivation`,
        "io.circe" %%% "circe-parser" % versions.circe
      )
    }

    val scribe: Def.Initialize[Seq[ModuleID]] = Def.setting {
      Seq(
        "com.outr" %%% "scribe" % versions.scribe
      )
    }

    val newtype: Def.Initialize[Seq[ModuleID]] = Def.setting {
      Seq(
        "io.estatico" %% "newtype" % versions.newtype
      )
    }

  }

  val sharedDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting {
    Seq.concat(
      libs.scribe.value,
      libs.circe.value,
      libs.newtype.value
    )
  }

  val backendDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting {
    Seq.concat(
      libs.`akka-http`.value,
      libs.`akka-http-json`.value,
      libs.akka.value,
      libs.`typesafe-config`.value,
      libs.pureconfig.value
    )
  }

  val frontendDependencies: Def.Initialize[Seq[ModuleID]] = Def.setting {
    Seq.concat(
      libs.laminar.value,
      libs.`url-dsl`.value,
      libs.waypoint.value
    )
  }

}
