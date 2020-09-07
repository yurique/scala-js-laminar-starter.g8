package starter.http

import scala.scalajs.js.UndefOr
import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.ext.Ajax.InputData
import org.scalajs.dom.raw.XMLHttpRequest
import org.scalajs.dom.raw.ProgressEvent
import scala.concurrent.Future
import scala.concurrent.Promise
import org.scalajs.dom.ext.AjaxException

object Requests {

  private[this] val jsonHeaders = Map(
    "content-type" -> "application/json"
  )

  def xmlHttpRequest(
    method: String,
    url: String,
    data: UndefOr[InputData],
    timeout: Int,
    headers: Map[String, String],
    withCredentials: Boolean,
    responseType: String,
    progress: UndefOr[ProgressEvent => Unit]
  ): Future[XMLHttpRequest] = {
    val req = new dom.XMLHttpRequest()
    val promise = Promise[dom.XMLHttpRequest]()

    req.onreadystatechange = { e: dom.Event =>
      if (req.readyState == 4) {
        promise.success(req)
      }
    }
    progress.foreach { onProgress =>
      req.upload.onprogress = p => onProgress(p)
    }
    req.open(method, url)
    req.responseType = responseType
    req.timeout = timeout
    req.withCredentials = withCredentials
    headers.foreach(x => req.setRequestHeader(x._1, x._2))
    if (data == null)
      req.send()
    else
      req.send(data)
    promise.future
  }

  def get(
    url: String,
    timeout: Int = 0,
    headers: Map[String, String] = Map.empty,
    withCredentials: Boolean = false,
    responseType: String = "",
    progress: UndefOr[ProgressEvent => Unit] = js.undefined
  ) =
    get_raw(url, timeout, jsonHeaders ++ headers, withCredentials, responseType, progress)

  def get_raw(
    url: String,
    timeout: Int = 0,
    headers: Map[String, String] = Map.empty,
    withCredentials: Boolean = false,
    responseType: String = "",
    progress: UndefOr[ProgressEvent => Unit] = js.undefined
  ) =
    xmlHttpRequest("GET", url, data = js.undefined, timeout, headers, withCredentials, responseType, progress)

}
