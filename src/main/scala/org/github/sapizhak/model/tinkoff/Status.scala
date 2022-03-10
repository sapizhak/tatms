package org.github.sapizhak.model.tinkoff

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Decoder

sealed abstract class Status(val extract: NonEmptyString) extends Product with Serializable

object Status {

  import eu.timepit.refined.auto._
  import io.circe.refined._

  case object Ok extends Status("Ok")

  val all: Set[Status] = Set(Ok)

  implicit val statusDecoder: Decoder[Status] =
    Decoder[NonEmptyString].emap(s => all.find(_.extract == s).toRight(s"Status.Unknown($s)"))

}
