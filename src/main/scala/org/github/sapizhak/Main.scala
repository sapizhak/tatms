package org.github.sapizhak
import cats.effect.kernel.Ref.Make
import cats.effect.{Async, ExitCode, IO, IOApp, Resource}
import cats.{ApplicativeThrow, Monad}
import org.http4s.ember.client.EmberClientBuilder
import org.typelevel.log4cats.slf4j.Slf4jLogger
import fs2.Stream
import org.github.sapizhak.model.telegram.http.SendMessageRequest
import org.github.sapizhak.model.tinkoff.atm.{AtmId, AtmPoint, ShortLimit}
import org.github.sapizhak.model.tinkoff.http.ClustersRequest
import org.github.sapizhak.service.clients._
import org.github.sapizhak.service.Limits
import org.http4s.client.Client

import scala.concurrent.duration.{Duration, SECONDS}

object Main extends IOApp {

  import cats.syntax.all._
  import io.circe.generic.auto._
  import io.circe.refined._
  import org.http4s.circe.CirceEntityCodec._

  def makeHttpClient[F[_]: Async]: Resource[F, Client[F]] =
    EmberClientBuilder
      .default[F]
      .withTimeout(Duration(4, SECONDS))
      .build

  def program[F[_]: Async: Monad: Make: ApplicativeThrow]: F[Unit] =
    Slf4jLogger.create[F].flatMap { l =>
      ApplicationConfig.readOrRaise.flatMap { c =>
        makeHttpClient.use { cli =>
          val pollAtmPoints: Stream[F, (AtmPoint, (AtmId, Set[ShortLimit]))] =
            Stream
              .emits(c.boundedCities.map(_.bounds).map(ClustersRequest.default))
              .evalMap(TinkoffClustersApi.getClusters(cli, c.tinkoff.baseUri, _))
              .map(_.clusters.flatMap(_.points).map(p => (p, (p.id, p.limits))))
              .flatMap(Stream.emits)

          Limits
            .instance[F](pollAtmPoints.map(_._2))
            .flatMap { limits =>
              Stream
                .awakeEvery(c.every)
                .void
                .flatMap { _ =>
                  pollAtmPoints.flatMap { case (atmp, t) =>
                    limits
                      .events(t)
                      .map(_.makeMessage(atmp))
                      .evalTap(m => l.info(s"MESSAGE: $m"))
                  }
                    .map(SendMessageRequest.make(c.telegram.chatId, _))
                    .evalTap(TelegramBotTokenApi.sendMessageFromBot(cli, c.telegram.baseUri, c.telegram.botToken, _))
                }
                .compile
                .drain
                .foreverM
                .void
            }
        }
      }
    }

  override def run(args: List[String]): IO[ExitCode] =
    program[IO].as(ExitCode.Success)
}
