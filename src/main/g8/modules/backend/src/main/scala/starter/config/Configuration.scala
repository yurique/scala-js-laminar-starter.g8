package starter.config

final case class HttpConfiguration(
  interface: String,
  port: Int
)

final case class Configuration(
  http: HttpConfiguration
)
