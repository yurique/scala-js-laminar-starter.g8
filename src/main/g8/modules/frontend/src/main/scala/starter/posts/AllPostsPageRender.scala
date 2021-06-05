package starter.posts

import com.raquo.laminar.api.L._
import io.laminext.fetch.circe._
import starter.components.PostView
import starter.data.Error
import starter.data.PostRepr

object AllPostsPageRender {

  def render: HtmlElement = {
    val content = {
      Fetch
        .get("/api/v1/posts")
        .decodeEither[Error, List[PostRepr]]
        .data
        .map {
          case Right(posts) => renderPosts(posts)
          case Left(error) =>
            div(
              cls := "bg-red-100 text-red-700 p-4",
              span(error.message)
            )
        }
    }
    div(child <-- content)
  }

  private def renderPosts(posts: List[PostRepr]) = {
    div(
      cls := "grid grid-cols-3",
      posts.map(p =>
        div(
          cls := "p-4",
          PostView(p)
        )
      )
    )
  }

}
