package starter.boot

import akka.actor.ActorSystem
import akka.util.Timeout
import scala.concurrent.duration._
import akka.http.scaladsl.Http
import starter.config.ConfigurationParser
import starter.config.Configuration
import starter.routes.RootRoute
import pureconfig.generic.auto._

class Application() {

  private implicit val system = ActorSystem("starter-http")
  private val Configuration(
    httpConfig
  ) =
    ConfigurationParser.parse[Configuration](system.settings.config, "starter")

  private val root = new RootRoute()

  def start(): Unit = {
    import system.dispatcher
    implicit val timeout: Timeout = 10.seconds

    val bindingFuture = Http().bindAndHandle(
      root.route,
      httpConfig.interface,
      httpConfig.port
    )

    bindingFuture.failed.foreach { ex =>
      scribe.error(s"bind failed: ${httpConfig}", ex)
    }
    bindingFuture.foreach { binding => scribe.info(s"bound: $binding") }

  }

}
