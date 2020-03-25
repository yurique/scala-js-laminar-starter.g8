package starter

import org.scalajs.dom

import scala.scalajs.js

object BrowserTools {

  def pushState(statedata: js.Any, title: String, url: String): Unit = {
    dom.window.history.pushState(statedata, title, url)
    emitPopStateEvent()
  }

  def emitPopStateEvent(statedata: js.Any = js.undefined): Unit = {
    val event = js.Dynamic.newInstance(js.Dynamic.global.Event)("popstate", statedata).asInstanceOf[dom.PopStateEvent]
    val ignored = dom.window.dispatchEvent(event)
  }

}
