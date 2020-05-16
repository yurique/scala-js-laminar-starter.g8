package starter

import com.raquo.laminar.api.L._
import starter.todomvc.TodoMvcApp
//import com.raquo.laminarexamples.oldstuff.todo.TodoApp
import org.scalajs.dom
import org.scalajs.dom.document
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.annotation.JSExportTopLevel
import starter.config.FrontEndConfig
import starter.Routes
import starter.components.PageChrome

@JSExportTopLevel("App")
object App {

  @JSExport
  def start(): Unit = {
    val config = FrontEndConfig.config
    dom.console.log("publishable key from config", config.publishableKey)
    val container = document.getElementById("app-container") // This div, its id and contents are defined in index-fastopt.html/index-fullopt.html files
    var ignoredRoot =
      render(
        container,
        PageChrome(Routes.view)
      )
  }
}
