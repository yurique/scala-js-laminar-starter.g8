package starter.config

import io.circe.parser._
import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.JSGlobal
import io.circe.derivation.annotations.JsonCodec

@JsonCodec
final case class FrontEndConfig(
  publishableKey: String
)

object FrontEndConfig {

  @js.native
  @JSGlobal("window")
  private[this] object ConfigGlobalScope extends js.Object {

    val ___appConfig: js.Object = js.native

  }

  lazy val config: FrontEndConfig = decode[FrontEndConfig](
    JSON.stringify(ConfigGlobalScope.___appConfig)
  ).fold(throw _, identity)

}
