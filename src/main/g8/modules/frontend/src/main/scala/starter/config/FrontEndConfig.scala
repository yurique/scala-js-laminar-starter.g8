package starter.config

import io.circe.derivation.annotations.JsonCodec
import io.circe.parser._

import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.JSImport

@JsonCodec
final case class FrontEndConfig(
  publishableKey: String
)

object FrontEndConfig {

  @js.native
  @JSImport("frontend-config", JSImport.Namespace)
  private object ConfigGlobalScope extends js.Object {

    val config: js.Object = js.native

  }

  lazy val config: FrontEndConfig = decode[FrontEndConfig](JSON.stringify(ConfigGlobalScope.config)).fold(throw _, identity)

}
