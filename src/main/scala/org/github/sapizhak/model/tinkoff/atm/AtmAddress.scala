package org.github.sapizhak.model.tinkoff.atm
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Decoder

final case class AtmAddress(extract: NonEmptyString)

object AtmAddress {

  import io.circe.refined._

  implicit val atmAddressDecoder: Decoder[AtmAddress] =
    Decoder[NonEmptyString].map(AtmAddress.apply)

}
