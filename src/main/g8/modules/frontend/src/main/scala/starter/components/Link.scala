package starter.components

import com.raquo.laminar.api.L._
import starter.pages.Page
import starter.Routes

object Link {

  def apply(page: Page, mods: Modifier[HtmlElement]*): HtmlElement = {
    a(
      href := page.path,
      onClick.preventDefault --> { _ =>
        Routes.pushState(page)
      },
      mods
    )
  }

}
