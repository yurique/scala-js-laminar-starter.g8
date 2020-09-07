package starter.todomvc

import com.raquo.laminar.api.L._
import org.scalajs.dom.ext.KeyCode

// Laminar is a simple, expressive, and safe UI library for Scala.js
// https://github.com/raquo/Laminar

// Everything we need is in this one file. TodoMvcApp.render() is called from App.scala
object TodoMvcApp {

  // This implementation is very loosely based on Outwatch TodoMVC, for comparison see
  // https://github.com/clovellytech/outwatch-examples/tree/master/todomvc/src/main/scala/todomvc

  // Models

  case class TodoItem(id: Int, text: String, completed: Boolean)

  sealed abstract class Filter(val name: String, val passes: TodoItem => Boolean)

  object ShowAll extends Filter("All", _ => true)

  object ShowActive extends Filter("Active", !_.completed)

  object ShowCompleted extends Filter("Completed", _.completed)

  val filters: List[Filter] = ShowAll :: ShowActive :: ShowCompleted :: Nil

  sealed trait Command

  case class Create(itemText: String) extends Command

  case class UpdateText(itemId: Int, text: String) extends Command

  case class UpdateCompleted(itemId: Int, completed: Boolean) extends Command

  case class Delete(itemId: Int) extends Command

  case object DeleteCompleted extends Command

  // State

  // Var-s are reactive state variables suitable for both local state and redux-like global stores.
  // Laminar uses my library Airstream as its reactive layer https://github.com/raquo/Airstream

  private val itemsVar = Var(List[TodoItem]())

  private val filterVar = Var[Filter](ShowAll)

  private var lastId = 1 // just for auto-incrementing IDs

  private val commandObserver = Observer[Command] {
    case Create(itemText) =>
      lastId += 1
      if (filterVar.now() == ShowCompleted) filterVar.set(ShowAll)
      itemsVar.update(_ :+ TodoItem(id = lastId, text = itemText, completed = false))
    case UpdateText(itemId, text) =>
      itemsVar.update(_.map(item => if (item.id == itemId) item.copy(text = text) else item))
    case UpdateCompleted(itemId, completed) =>
      itemsVar.update(_.map(item => if (item.id == itemId) item.copy(completed = completed) else item))
    case Delete(itemId) =>
      itemsVar.update(_.filterNot(_.id == itemId))
    case DeleteCompleted =>
      itemsVar.update(_.filterNot(_.completed))
  }

  // Rendering

  // This is what we expose to the public – a single div element: not a stream, not some virtual DOM representation.
  // You can get the real JS DOM element it manages using its .ref property – that reference does not change over time.
  def render: HtmlElement = {
    div(
      div(
        h1(
          cls := "text-3xl font-bold leading-tight text-gray-900 font-condensed",
          "TODOS"
        ),
        renderNewTodoInput
      ),
      div(
        hideIfNoItems,
        cls := "",
        ul(
          cls := "min-w-full ",
          children <-- itemsVar.signal.combineWith(filterVar.signal).map2(_ filter _.passes).split(_.id)(renderTodoItem)
        )
      ),
      renderStatusBar
    )
  }

  private def renderNewTodoInput =
    input(
      cls := "min-w-full appearance-none block border-b px-3 py-2 text-purple-800 focus:outline-none focus:shadow-outline-purple focus:border-purple-300 text-xl",
      placeholder("What needs to be done?"),
      onMountFocus,
      inContext { thisNode =>
        // Note: mapTo below accepts parameter by-name, evaluating it on every enter key press
        onEnterPress.mapTo(thisNode.ref.value).filter(_.nonEmpty) -->
          commandObserver.contramap[String] { text =>
            thisNode.ref.value = "" // clear input
            Create(itemText = text)
          }
      }
    )

  // Render a single item. Note that the result is a single element: not a stream, not some virtual DOM representation.
  private def renderTodoItem(itemId: Int, initialTodo: TodoItem, $item: Signal[TodoItem]): HtmlElement = {
    val isEditingVar = Var(false) // Example of local state
    val updateTextObserver = commandObserver.contramap[UpdateText] { updateCommand =>
      isEditingVar.set(false)
      updateCommand
    }
    li(
      cls := "px-6 py-3 border-b border-purple-200 bg-purple-50 flex items-center",
      cls <-- $item.map(
        item =>
          Map(
            "line-through" -> item.completed
          )
      ),
      onDblClick.filter(_ => !isEditingVar.now()).mapTo(true) --> isEditingVar.writer,
      children <-- isEditingVar.signal.map[List[HtmlElement]] {
        case true =>
          renderTextUpdateInput(itemId, $item, updateTextObserver) :: Nil
        case false =>
          List(
            renderCheckboxInput(itemId, $item),
            label(
              cls := "ml-1",
              child.text <-- $item.map(_.text)
            ),
            button(
              cls := "w-8 h-8 text-red-600 p-2 ml-4",
              Icons.delete,
              onClick.mapTo(Delete(itemId)) --> commandObserver
            )
          )
      }
    )
  }

  // Note that we pass reactive variables: `$item` for reading, `updateTextObserver` for writing
  private def renderTextUpdateInput(itemId: Int, $item: Signal[TodoItem], updateTextObserver: Observer[UpdateText]) =
    input(
      cls := "min-w-md appearance-none block border-b px-3 py-2 text-purple-800 focus:outline-none focus:shadow-outline-purple focus:border-purple-300",
      defaultValue <-- $item.map(_.text),
      onMountFocus,
      inContext { thisNode =>
        @inline def updateText = UpdateText(itemId, thisNode.ref.value)

        List(
          onEnterPress.mapTo(updateText) --> updateTextObserver,
          onBlur.mapTo(updateText) --> updateTextObserver
        )
      }
    )

  private def renderCheckboxInput(itemId: Int, $item: Signal[TodoItem]) =
    input(
      cls := "block w-8 h-8",
      typ := "checkbox",
      checked <-- $item.map(_.completed),
      inContext { thisNode =>
        onInput.mapTo(UpdateCompleted(itemId, completed = thisNode.ref.checked)) --> commandObserver
      }
    )

  private def renderStatusBar =
    footer(
      hideIfNoItems,
      cls := "mt-4 bg-purple-100 p-4",
      div(
        cls := "text-purple-800 tracking-wide",
        child <-- itemsVar.signal
          .map(_.count(!_.completed))
          .map(
            count =>
              div(
                cls := "flex items-center",
                div(
                  cls := "font-bold text-xl",
                  s"${count}"
                ),
                div(
                  cls := "ml-2",
                  pluralize(count, "item left", "items left")
                )
              )
          )
      ),
      div(
        ul(
          cls := "flex max-w-lg items-center",
          filters.map(filter => li(renderFilterButton(filter)))
        ),
        child.maybe <-- itemsVar.signal.map { items =>
          if (items.exists(ShowCompleted.passes))
            Some(
              div(
                cls := "mt-2",
                button(
                  cls := "inline-flex items-center border border-transparent rounded-md focus:outline-none",
                  cls := "px-3 py-2 text-sm",
                  cls := "text-white bg-indigo-600 hover:bg-indigo-500 focus:border-indigo-700 focus:shadow-outline-indigo active:bg-indigo-700",
                  cls := "cursor-pointer",
                  "Clear completed",
                  onClick.map(_ => DeleteCompleted) --> commandObserver
                )
              )
            )
          else None
        }
      )
    )

  private def renderFilterButton(filter: Filter) =
    a(
      cls := "block py-2 flex-1 px-4 py-2 text-purple-800",
      cls <-- filterVar.signal.map(
        selectedFilter =>
          Map(
            "underline" -> (selectedFilter == filter)
          )
      ),
      cls := "cursor-pointer",
      onClick.preventDefault.mapTo(filter) --> filterVar.writer,
      filter.name
    )

  // Every little thing in Laminar can be abstracted away
  private def hideIfNoItems: Mod[HtmlElement] =
    display <-- itemsVar.signal.map(items => if (items.nonEmpty) "" else "none")

  // Helpers

  private def pluralize(num: Int, singular: String, plural: String): String =
    s"${if (num == 1) singular else plural}"

  private val onEnterPress = onKeyPress.filter(_.keyCode == KeyCode.Enter)
}
