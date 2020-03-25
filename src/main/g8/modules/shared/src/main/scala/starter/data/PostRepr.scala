package starter.data

import io.circe.derivation.annotations.JsonCodec

@JsonCodec
final case class PostRepr(
  id: Int,
  text: String,
  author: String
)