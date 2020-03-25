package starter.posts

import com.raquo.laminar.api.L._
import starter.http.Requests
import io.circe.parser._
import starter.data.PostRepr
import starter.components.PostView
import org.scalajs.dom.ext.AjaxException
import starter.http.HttpError
import scala.concurrent.ExecutionContext.Implicits.global
import org.scalajs.dom
import starter.pages.`package`.PostPage

object PostByIdPageRenderer {

  def render($id: Signal[PostPage]): HtmlElement = {
    val content =
    $id.map { page => 
      EventStream.fromFuture(
        Requests.get(s"/api/v1/posts/${page.id}").map { xhr =>
          dom.console.log(xhr)
          xhr
        }
      ).map( xhr =>
        if (xhr.status != 200) {
          Left(HttpError(s"${xhr.status} ${xhr.statusText} ${xhr.response}"))
        } else {
          decode[PostRepr](xhr.responseText) 
        }
      ).map {
        case Right(posts) => renderPosts(posts)
        case Left(error) => div(
          cls := "bg-red-100 text-red-700",
          span(error.getMessage())
        )
      }
    }
    div(child <-- content.map { c => div(child.maybe <-- c.toWeakSignal) })
  }

  private def renderPosts(post: PostRepr) = {
    div(
      cls := "flex -m-2",
      div(
        cls := "max-w-lg m-4",
        PostView(post)
      )
    )
  }

}