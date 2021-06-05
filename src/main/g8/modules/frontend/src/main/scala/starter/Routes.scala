package starter

import com.raquo.laminar.api.L._
import com.raquo.waypoint._
import org.scalajs.dom
import io.circe.syntax._
import io.circe.parser._
import starter.todomvc.TodoMvcApp
import starter.pages._
import starter.posts.AllPostsPageRender
import starter.posts.RandomPostPageRender
import starter.posts.PostByIdPageRenderer

object Routes {

  private val todoRoute       = Route.static(TodoMvcPage, root / "todo" / endOfSegments)
  private val allPostsRoute   = Route.static(AllPostsPage, root / "posts" / endOfSegments)
  private val randomPostRoute = Route.static(RandomPostPage, root / "posts" / "random" / endOfSegments)

  private val postByIdRoute = Route[PostPage, Int](
    encode = post => post.id,
    decode = arg => PostPage(id = arg),
    pattern = root / "posts" / segment[Int] / endOfSegments
  )
  private val notFoundRoute = Route.static(NotFoundPage, root)

  val router = new Router[Page](
    routes = List(todoRoute, allPostsRoute, randomPostRoute, postByIdRoute, notFoundRoute),
    getPageTitle = _.toString,                                                                      // mock page title (displayed in the browser tab next to favicon)
    serializePage = page => page.asJson.noSpaces,                                                   // serialize page data for storage in History API log
    deserializePage = pageStr => decode[Page](pageStr).fold(e => ErrorPage(e.getMessage), identity) // deserialize the above
  )(
    initialUrl = dom.document.location.href, // must be a valid LoginPage or UserPage url
    owner = unsafeWindowOwner,               // this router will live as long as the window
    origin = dom.document.location.origin.get,
    $popStateEvent = windowEvents.onPopState
  )

  private val splitter =
    SplitRender[Page, HtmlElement](router.$currentPage)
      .collectSignal[ErrorPage] { $errorPage =>
        div(
          div("An unpredicted error has just happened. We think this is truly unfortunate."),
          div(
            child.text <-- $errorPage.map(_.error)
          )
        )
      }
      .collectSignal[PostPage] { $postPage =>
        PostByIdPageRenderer.render($postPage)
      }
      .collectStatic(TodoMvcPage) { TodoMvcApp.render }
      .collectStatic(AllPostsPage) { AllPostsPageRender.render }
      .collectStatic(RandomPostPage) { RandomPostPageRender.render }
      .collectStatic(NotFoundPage) { div("Not Found") }

  def pushState(page: Page): Unit = {
    router.pushState(page)
  }

  def replaceState(page: Page): Unit = {
    router.replaceState(page)
  }

  val view: Signal[HtmlElement] = splitter.$view

}
