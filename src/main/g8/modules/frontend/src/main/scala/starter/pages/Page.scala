package starter.pages
import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

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

  implicit val codePage: Codec.AsObject[Page] = deriveCodec[Page]

}
