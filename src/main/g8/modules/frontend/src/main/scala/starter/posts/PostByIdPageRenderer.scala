package starter.posts

import com.raquo.laminar.api.L._
import io.circe.parser._
import org.scalajs.dom
import starter.components.PostView
import starter.data.PostRepr
import starter.http.HttpError
import starter.http.Requests
import starter.pages.PostPage

import scala.concurrent.ExecutionContext.Implicits.global

object PostByIdPageRenderer {

  def render($id: Signal[PostPage]): HtmlElement = {
    val content =
      $id.map { page =>
        EventStream
          .fromFuture(
            Requests.get(s"/api/v1/posts/${page.id}").map { xhr =>
              dom.console.log(xhr)
              xhr
            }
          ).map(xhr =>
            if (xhr.status != 200) {
              Left(HttpError(s"${xhr.status} ${xhr.statusText} ${xhr.response}"))
            } else {
              decode[PostRepr](xhr.responseText)
          }).map {
            case Right(posts) => renderPosts(posts)
            case Left(error) =>
              div(
                cls := "bg-red-100 text-red-700",
                span(error.getMessage())
              )
          }
      }
    div(child <-- content.map { c =>
      div(child.maybe <-- c.toWeakSignal)
    })
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
