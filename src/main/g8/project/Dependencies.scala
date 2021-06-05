import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt._

object Dependencies {

  val laminar: Def.Initialize[Seq[ModuleID]] = Def.setting {
    Seq(
      "com.raquo" %%% "laminar" % DependencyVersions.laminar
    )
  }

  val laminext: Def.Initialize[Seq[ModuleID]] = Def.setting {
    Seq(
      "io.laminext" %%% "core"            % DependencyVersions.laminext,
      "io.laminext" %%% "validation-core" % DependencyVersions.laminext,
      "io.laminext" %%% "validation-cats" % DependencyVersions.laminext,
      "io.laminext" %%% "fetch"           % DependencyVersions.laminext,
      "io.laminext" %%% "fetch-circe"     % DependencyVersions.laminext,
      "io.laminext" %%% "markdown"        % DependencyVersions.laminext,
      "io.laminext" %%% "websocket"       % DependencyVersions.laminext,
      "io.laminext" %%% "websocket-circe" % DependencyVersions.laminext,
      "io.laminext" %%% "util"            % DependencyVersions.laminext
    )
  }

  val `url-dsl`: Def.Initialize[Seq[ModuleID]] = Def.setting {
    Seq(
      "be.doeraene" %%% "url-dsl" % DependencyVersions.`url-dsl`
    )
  }

  val waypoint: Def.Initialize[Seq[ModuleID]] = Def.setting {
    Seq(
      "com.raquo" %%% "waypoint" % DependencyVersions.waypoint
    )
  }

  val uTest: Def.Initialize[Seq[ModuleID]] = Def.setting {
    Seq(
      "com.lihaoyi" %%% "utest" % DependencyVersions.uTest
    )
  }

  val `akka-http`: Def.Initialize[Seq[ModuleID]] = Def.setting {
    Seq(
      "com.typesafe.akka" %%% "akka-http" % DependencyVersions.`akka-http`
    )
  }

  val `akka-http-json`: Def.Initialize[Seq[ModuleID]] = Def.setting {
    Seq(
      "de.heikoseeberger" %%% "akka-http-circe" % DependencyVersions.`akka-http-json`
    )
  }

  val akka: Def.Initialize[Seq[ModuleID]] = Def.setting {
    Seq(
      "com.typesafe.akka" %%% "akka-actor"       % DependencyVersions.akka,
      "com.typesafe.akka" %%% "akka-actor-typed" % DependencyVersions.akka,
      "com.typesafe.akka" %%% "akka-stream"      % DependencyVersions.akka,
      "com.typesafe.akka" %%% "akka-testkit"     % DependencyVersions.akka % Test
    )
  }

  val `typesafe-config`: Def.Initialize[Seq[ModuleID]] = Def.setting {
    Seq(
      "com.typesafe" % "config" % DependencyVersions.`typesafe-config`
    )
  }

  val `circe-config`: Def.Initialize[Seq[ModuleID]] = Def.setting {
    Seq(
      "io.circe" %%% "circe-config" % DependencyVersions.`circe-config`
    )
  }

  val circe: Def.Initialize[Seq[ModuleID]] = Def.setting {
    Seq(
      "io.circe" %%% "circe-core"                   % DependencyVersions.circe,
      "io.circe" %%% "circe-generic"                % DependencyVersions.circe,
      "io.circe" %%% "circe-derivation"             % DependencyVersions.`circe-derivation`,
      "io.circe" %%% "circe-derivation-annotations" % DependencyVersions.`circe-derivation`,
      "io.circe" %%% "circe-parser"                 % DependencyVersions.circe
    )
  }

  val scribe: Def.Initialize[Seq[ModuleID]] = Def.setting {
    Seq(
      "com.outr" %%% "scribe" % DependencyVersions.scribe
    )
  }

  val newtype: Def.Initialize[Seq[ModuleID]] = Def.setting {
    Seq(
      "io.estatico" %% "newtype" % DependencyVersions.newtype
    )
  }

  val `dom-test-utils`: Def.Initialize[Seq[ModuleID]] = Def.setting {
    Seq(
      "com.raquo" %%% "domtestutils" % DependencyVersions.`dom-test-utils` % Test
    )
  }

  val `unindent`: Def.Initialize[Seq[ModuleID]] = Def.setting {
    Seq(
      "com.davegurnell" %% "unindent" % DependencyVersions.`unindent`
    )
  }

}
