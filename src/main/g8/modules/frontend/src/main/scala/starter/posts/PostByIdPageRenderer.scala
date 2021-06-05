package starter.posts

import com.raquo.laminar.api.L._
import io.laminext.fetch.circe._
import starter.components.PostView
import starter.data.Error
import starter.data.PostRepr
import starter.pages.PostPage

object PostByIdPageRenderer {

  def render($id: Signal[PostPage]): HtmlElement = {
    val content =
      $id.map { page =>
        Fetch
          .get(s"/api/v1/posts/${page.id}")
          .decodeEither[Error, PostRepr]
          .data
          .map {
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
