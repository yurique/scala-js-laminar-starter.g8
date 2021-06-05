package starter.boot

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import starter.config.ConfigurationParser
import starter.config.Configuration
import starter.routes.RootRoute

class Application() {

  implicit private val system: ActorSystem = ActorSystem("starter-http")
  private val Configuration(
    httpConfig
  ) =
    ConfigurationParser.parse(system.settings.config)

  private val root = new RootRoute()

  def start(): Unit = {
    import system.dispatcher

    val bindingFuture =
      Http()
        .newServerAt(
          httpConfig.interface,
          httpConfig.port
        )
        .bind(root.route)

    bindingFuture.failed.foreach { ex =>
      scribe.error(s"bind failed: ${httpConfig}", ex)
    }
    bindingFuture.foreach { binding =>
      scribe.info(s"bound: $binding")
    }

  }

}
