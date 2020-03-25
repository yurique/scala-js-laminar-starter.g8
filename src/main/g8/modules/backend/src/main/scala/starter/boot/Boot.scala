package starter.boot

import akka.actor.ActorSystem
import akka.util.Timeout
import akka.http.scaladsl.Http

object Boot {

  def main(args: Array[String]): Unit = {
    val application = new Application
    application.start()
  }

}
