package starter.http

import akka.http.scaladsl.server.{Directives, PathMatcher1}

trait RoutesBase extends Directives with CirceSupportWithCodecs
