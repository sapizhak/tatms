package org.github.sapizhak.model.tinkoff
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Decoder

sealed abstract class Code(val extract: NonEmptyString) extends Product with Serializable

object Code {

  import eu.timepit.refined.auto._
  import io.circe.refined._

  case object RequestLimitExceeded extends Code("REQUEST_LIMIT_EXCEEDED")

  val all: Set[Code] = Set(RequestLimitExceeded)

  implicit val codeDecoder: Decoder[Code] =
    Decoder[NonEmptyString].emap(s => all.find(_.extract == s).toRight(s"Code.Unknown($s)"))

}
