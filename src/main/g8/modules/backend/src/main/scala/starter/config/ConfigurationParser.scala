package starter.config

import pureconfig._
import com.typesafe.config.Config

object ConfigurationParser {

  def parse[T](rawConfig: Config, path: String)(implicit reader: Derivation[ConfigReader[T]]): T = {
    ConfigSource
      .fromConfig(rawConfig)
      .at(path)
      .load[T]
      .fold(
        f =>
          throw new RuntimeException(
            s"Failed to parse configuration: ${f.toList.map(e => s"${e.description} (${e.location.fold("")(_.toString)})").mkString("[\n", ",\n", "\n]")}"
          ),
        identity
      )
  }

}
