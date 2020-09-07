package starter.http

final case class HttpError(message: String) extends Exception(message)
