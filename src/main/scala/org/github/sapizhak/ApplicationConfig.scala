package org.github.sapizhak

import cats.ApplicativeThrow
import eu.timepit.refined.types.string.NonEmptyString
import org.github.sapizhak.ApplicationConfig.{BoundedCity, TelegramConfig, TinkoffConfig}
import org.github.sapizhak.model.telegram.BotToken
import org.github.sapizhak.model.tinkoff.{Bounds, City}
import org.http4s.Uri
import pureconfig.ConfigSource

import scala.concurrent.duration.FiniteDuration

final case class ApplicationConfig(
  tinkoff: TinkoffConfig,
  telegram: TelegramConfig,
  boundedCities: List[BoundedCity],
  every: FiniteDuration
) {

  val message: String =
    s"""tinkoff.base-uri: ${tinkoff.baseUri},
       |telegram.base-uri: ${telegram.baseUri},
       |telegram.chat: ${telegram.chatId}
       |repeat-every: ${every.length} ${every.unit}
       |known cities: ${boundedCities.map(_.city.extract).mkString(", ")}
       |""".stripMargin

}

object ApplicationConfig {

  import orphans._
  import pureconfig.generic.auto._
  import eu.timepit.refined.pureconfig._

  def readOrRaise[F[_]: ApplicativeThrow]: F[ApplicationConfig] =
    ApplicativeThrow[F].catchNonFatal(ConfigSource.default.loadOrThrow[ApplicationConfig])

  final case class TelegramConfig(baseUri: Uri, botToken: BotToken, chatId: NonEmptyString)

  final case class TinkoffConfig(baseUri: Uri)

  final case class BoundedCity(city: City, bounds: Bounds)

}
