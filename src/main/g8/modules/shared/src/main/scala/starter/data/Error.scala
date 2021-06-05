package starter.data
import io.circe.generic.JsonCodec

@JsonCodec
final case class Error(message: String) extends Exception(message)
