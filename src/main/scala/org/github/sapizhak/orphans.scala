package org.github.sapizhak

import org.http4s.Uri
import pureconfig.ConfigReader
import pureconfig.error.CannotConvert

object orphans {

  import cats.syntax.either._

  implicit val uriCR: ConfigReader[Uri] = ConfigReader.fromNonEmptyString { s =>
    Uri.fromString(s).leftMap(er => CannotConvert(s, "org.http4s.Uri", er.message))
  }

}
