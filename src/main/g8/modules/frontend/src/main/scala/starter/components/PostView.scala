package starter.components

import com.raquo.laminar.api.L._
import starter.data.PostRepr
import starter.util._
import starter.pages.PostPage

object PostView {

  def apply(post: PostRepr): HtmlElement =
    div(
      div(
        cls := "px-4 text-blue-500 p-2",
        Link(PostPage(post.id), "permalink")
      ),
      div(
        cls := "px-4 markdown-block",
        unsafeMarkdown := post.text
      ),
      div(
        cls := "px-4 text-xl text-gray-500 tracking-wide",
        div(
          cls := "ml-4",
          post.author
        )
      )
    )

}
