package starter

import com.raquo.laminar.api.L._
import org.scalajs.dom
import org.scalajs.dom.document
import starter.components.PageChrome
import starter.config.FrontEndConfig

object App {

  def main(args: Array[String]): Unit = {
    val _ = documentEvents.onDomContentLoaded.foreach { _ =>
      val config = FrontEndConfig.config
      dom.console.log("publishable key from config", config.publishableKey)
      val container = document.getElementById("app-container") // This div, its id and contents are defined in index-fastopt.html/index-fullopt.html files
      val _ =
        render(
          container,
          PageChrome(Routes.view)
        )
    }(unsafeWindowOwner)
  }
}
