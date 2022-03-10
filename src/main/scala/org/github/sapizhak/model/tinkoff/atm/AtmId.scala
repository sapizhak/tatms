package org.github.sapizhak.model.tinkoff.atm
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Decoder

final case class AtmId(extract: NonEmptyString)

object AtmId {

  import io.circe.refined._

  implicit val atmAddressDecoder: Decoder[AtmId] =
    Decoder[NonEmptyString].map(AtmId.apply)

}
