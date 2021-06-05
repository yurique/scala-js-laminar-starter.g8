package starter.config

import com.typesafe.config.Config
import io.circe.config.syntax._
import io.circe.generic.auto._
import io.circe.config.parser

object ConfigurationParser {

  def parse(rawConfig: Config): Configuration =
    parser
      .decodePath[Configuration](rawConfig, "starter").fold(
        { e =>
          println(s"failed to parse configuration")
          e.printStackTrace()
          throw e
        },
        identity
      )

}
