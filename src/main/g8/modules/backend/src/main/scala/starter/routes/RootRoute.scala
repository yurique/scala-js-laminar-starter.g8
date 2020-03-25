package starter.routes

import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import starter.http.RoutesBase
import starter.data.PostRepr
import scala.util.Random
import akka.http.scaladsl.model.StatusCodes

class RootRoute extends RoutesBase {

  def route: Route = {
    pathPrefix("api" / "v1") {
      pathPrefix("posts") {
        concat(
          pathEnd {
            complete(TestData.posts)
          },
          path("random") {
            TestData.posts
              .drop(Random.nextInt(TestData.posts.size + 1)) // bug intentional :)
              .headOption match {
              case Some(p) => complete(p)
              case None    => complete(StatusCodes.InternalServerError)
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

object TestData {

  val posts: Seq[PostRepr] = Seq(
    PostRepr(
      id = 1,
      text =
        """
        |Your time is limited, so don't waste it living someone else's life. Don't be trapped by dogma – which is living with the results of other people's thinking.
        """.stripMargin,
      author = "Steve Jobs"
    ),
    PostRepr(
      id = 2,
      text = """
        |The way to get started is to quit talking and begin doing.
        """.stripMargin,
      author = "Walt Disney"
    ),
    PostRepr(
      id = 3,
      text =
        """
        |The greatest glory in living lies not in never falling, but in rising every time we fall." 
        """.stripMargin,
      author = "Nelson Mandela"
    )
  )

}
