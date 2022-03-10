package org.github.sapizhak.model.tinkoff

import eu.timepit.refined.types.all.NonNegInt
import io.circe.{Decoder, Encoder}

final case class Zoom(extract: NonNegInt)

object Zoom {

  import eu.timepit.refined.auto._
  import io.circe.refined._

  implicit val zoomEncoder: Encoder[Zoom] =
    Encoder[NonNegInt].contramap(_.extract)

  implicit val zoomDecoder: Decoder[Zoom] =
    Decoder[NonNegInt].map(Zoom.apply)

  val ten: Zoom = Zoom(10)

}
