package starter.components

import com.raquo.laminar.api.L._
import starter.data.PostRepr
import io.laminext.syntax.markdown._
import starter.pages.PostPage

object PostView {

  def apply(post: PostRepr): HtmlElement =
    div(
      cls := "space-y-4",
      div(
        cls := "px-4 text-blue-500 p-2",
        Link(PostPage(post.id), "permalink")
      ),
      div(
        cls := "px-4 prose",
        unsafeMarkdown := post.text
      ),
      div(
        cls := "px-4 text-lg text-gray-600 tracking-wide",
        div(
          cls := "ml-4",
          post.author
        )
      )
    )

}
