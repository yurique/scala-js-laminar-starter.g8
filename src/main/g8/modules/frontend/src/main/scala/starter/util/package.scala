package starter

import com.raquo.laminar.api.L._
import com.karasiq.markedjs.Marked

package object util {

  val unsafeMarkdown: UnsafeMarkdownReceiver.type = UnsafeMarkdownReceiver

  object UnsafeMarkdownReceiver {

    def :=(markdown: String): Modifier[HtmlElement] = {
      new Modifier[HtmlElement] {
        override def apply(element: HtmlElement): Unit = element.ref.innerHTML = Marked(markdown)
      }
    }

  }

  
}
