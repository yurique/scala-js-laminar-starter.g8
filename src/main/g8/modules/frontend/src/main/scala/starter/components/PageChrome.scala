package starter.components

import com.raquo.laminar.api.L._
import starter.pages._

object PageChrome {

  def apply($child: Signal[HtmlElement]): HtmlElement =
    div(
      cls := "max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8",
      div(
        cls := "px-2 pt-2 pb-3 bg-gray-800 flex items-baseline",
        Link(
          TodoMvcPage,
          cls := "mt-1 block px-3 py-2 rounded-md text-base font-medium text-gray-300 hover:text-white hover:bg-gray-700 focus:outline-none focus:text-white focus:bg-gray-700",
          "ToDo"
        ),
        Link(
          AllPostsPage,
          cls := "mt-1 block px-3 py-2 rounded-md text-base font-medium text-gray-300 hover:text-white hover:bg-gray-700 focus:outline-none focus:text-white focus:bg-gray-700",
          "All Posts"
        ),
        Link(
          RandomPostPage,
          cls := "mt-1 block px-3 py-2 rounded-md text-base font-medium text-gray-300 hover:text-white hover:bg-gray-700 focus:outline-none focus:text-white focus:bg-gray-700",
          "Random Post"
        )
      ),
      div(
        cls := "bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10",
        child <-- $child
      )
    )

}
