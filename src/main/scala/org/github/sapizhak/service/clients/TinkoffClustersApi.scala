package org.github.sapizhak.service.clients
import cats.effect.kernel.Concurrent
import org.github.sapizhak.model.tinkoff.Payload
import org.github.sapizhak.model.tinkoff.http.{ClustersRequest, ClustersResponse}
import org.http4s.client.Client
import org.http4s.{EntityDecoder, EntityEncoder, MediaType, Method, Request, Uri}
import org.http4s.headers.{Accept, `Content-Type`}

object TinkoffClustersApi {

  import ClustersRequest._
  import io.circe.generic.auto._
  import io.circe.refined._
  import org.http4s.circe.CirceEntityCodec._
  import cats.syntax.all._

  private def makeRequest[F[_]: Concurrent](baseUri: Uri, request: ClustersRequest)(implicit
    ED: EntityEncoder[F, ClustersRequest]
  ): Request[F] =
    Request[F](Method.POST, baseUri)
      .withEntity(request)
      .withHeaders(Accept(MediaType.application.json), `Content-Type`(MediaType.application.`json`))

  def getClusters[F[_]: Concurrent](cli: Client[F], baseUri: Uri, request: ClustersRequest)(implicit
    E: EntityEncoder[F, ClustersRequest],
    D: EntityDecoder[F, ClustersResponse]
  ): F[Payload] = cli.expect[ClustersResponse](makeRequest(baseUri, request)).map(_.payload)

}
