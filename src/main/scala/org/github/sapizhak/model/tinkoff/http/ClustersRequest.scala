package org.github.sapizhak.model.tinkoff.http

import io.circe.Encoder
import org.github.sapizhak.model.tinkoff.{Bounds, Filters, Zoom, http}
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

final case class ClustersRequest(bounds: Bounds, filters: Filters, zoom: Zoom)

object ClustersRequest {

  implicit def requestEntityEncoder[F[_]](implicit E: Encoder[ClustersRequest]): EntityEncoder[F, ClustersRequest] =
    jsonEncoderOf[F, ClustersRequest]

  val default: Bounds => ClustersRequest = http.ClustersRequest(_, Filters.tinkoffUsdAtms, Zoom.ten)

}
