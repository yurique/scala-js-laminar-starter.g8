package starter

import io.circe.generic.semiauto._

package object pages {

  sealed trait Page {
    def path: String
  }
  case object TodoMvcPage extends Page {
    def path: String = "/todo"
  }
  case object AllPostsPage extends Page {
    def path: String = "/posts"
  }
  case object RandomPostPage extends Page {
    def path: String = "/posts/random"
  }
  case class PostPage(id: Int) extends Page {
    def path: String = s"/posts/$id"
  }
  case class ErrorPage(error: String) extends Page {
    def path: String = "/"
  }  
  case object NotFoundPage extends Page {
    def path: String = "/"
  }

  object Page {
   
    implicit val codePage = deriveCodec[Page]

  }

}
