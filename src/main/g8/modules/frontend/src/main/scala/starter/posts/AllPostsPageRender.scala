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

object AllPostsPageRender {

  def render: HtmlElement = {
    val content = EventStream.fromFuture(
      Requests.get("/api/v1/posts").map { xhr =>
        dom.console.log(xhr)
        xhr
      }
    ).map( xhr =>
      if (xhr.status != 200) {
        Left(HttpError(s"${xhr.status} ${xhr.statusText} ${xhr.response}"))
      } else {
        decode[List[PostRepr]](xhr.responseText) 
      }
    ).map {
      case Right(posts) => renderPosts(posts)
      case Left(error) => div(
        cls := "bg-red-100 text-red-700",
        span(error.getMessage())
      )
    }
    div(child <-- content)
  }

  private def renderPosts(posts: List[PostRepr]) = {
    div(
      cls := "flex -m-2",
      posts.map( p =>
          div(
            cls := "max-w-lg m-4",
            PostView(p)
          )
      )
    )
  }

}