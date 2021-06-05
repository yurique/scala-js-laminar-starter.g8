package starter.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server._
import starter.data.Error
import starter.data.PostRepr
import starter.http.RoutesBase

import scala.util.Random
import scala.util.control.NonFatal
import unindent._

class RootRoute extends RoutesBase {

  def route: Route = {
    (handleRejections(apiRejectionHandler) & handleExceptions(apiExceptionHandler)) {
      pathPrefix("api" / "v1") {
        pathPrefix("posts") {
          concat(
            pathEnd {
              complete(TestData.posts)
            },
            path("random") {
              complete {
                TestData.posts
                  .drop(Random.nextInt(TestData.posts.size + 1)) // bug intentional :)
                  .head
              }
            },
            path(IntNumber) { id =>
              TestData.posts.find(_.id == id) match {
                case Some(p) => complete(p)
                case None    => complete(StatusCodes.NotFound)
              }
            }
          )
        }
      }
    }
  }

  private def apiRejectionHandler =
    RejectionHandler
      .newBuilder()
      .handle {
        case AuthorizationFailedRejection =>
          extractUri { uri =>
            scribe.error(s"${uri.path} | unauthorized API call")
            complete(StatusCodes.Unauthorized -> Error("Authorization Required"))
          }
        case MalformedRequestContentRejection(reason, error) =>
          extractUri { uri =>
            scribe.error(s"${uri.path} | malformed content - ${reason}", error)
            complete(StatusCodes.BadRequest -> Error(reason))
          }
      }
      .handleNotFound {
        complete(StatusCodes.NotFound -> Error("Not Found"))
      }
      .result()

  private def apiExceptionHandler: ExceptionHandler = ExceptionHandler { case NonFatal(ex) =>
    extractUri { uri =>
      scribe.error(s"$uri | unhandled exception", ex)
      complete(StatusCodes.InternalServerError, Error(ex.getMessage))
    }
  }

}

object TestData {

  val posts: Seq[PostRepr] = Seq(
    PostRepr(
      id = 1,
      text = i"""
       Your time is limited, so don't waste it living someone else's life. Don't be trapped by dogma – which is living with the results of other people's thinking.
       """,
      author = "Steve Jobs"
    ),
    PostRepr(
      id = 2,
      text = i"""
       The way to get started is to quit talking and begin doing.
       """,
      author = "Walt Disney"
    ),
    PostRepr(
      id = 3,
      text = i"""
       The greatest glory in living lies not in never falling, but in rising every time we fall."
       """,
      author = "Nelson Mandela"
    )
  )

}
