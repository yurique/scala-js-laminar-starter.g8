package starter.posts

import com.raquo.laminar.api.L._
import io.laminext.fetch.circe._
import starter.components.PostView
import starter.data.Error
import starter.data.PostRepr

object RandomPostPageRender {

  def render: HtmlElement = {
    val content =
      Fetch
        .get(s"/api/v1/posts/random")
        .decodeEither[Error, PostRepr]
        .data
        .map {
          case Right(post) => renderPost(post)
          case Left(error) =>
            div(
              cls := "bg-red-100 text-red-700",
              span(error.getMessage())
            )
        }
    div(child <-- content)
  }

  private def renderPost(post: PostRepr) = {
    div(
      cls := "flex -m-2",
      div(
        cls := "max-w-lg m-4",
        PostView(post)
      )
    )
  }

}
