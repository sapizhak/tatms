package org.github.sapizhak.model.tinkoff.atm

import eu.timepit.refined.api.Refined
import eu.timepit.refined.types.all.NonNegInt
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.{Decoder, DecodingFailure}

import java.time.{DayOfWeek, LocalTime}

final case class WorkPeriod(
  closeDay: DayOfWeek,
  closeTime: LocalTime,
  openDay: DayOfWeek,
  openTime: LocalTime
)

object WorkPeriod {

  import io.circe.refined._
  import cats.syntax.all._

  implicit val workPeriodDecoder: Decoder[WorkPeriod] =
    Decoder.instance { hc =>
      val wd: String => Either[DecodingFailure, DayOfWeek] = fieldName =>
        hc.get[NonNegInt](fieldName).flatMap { case Refined(t) =>
          t match {
            case 0 => Right(DayOfWeek.MONDAY)
            case 1 => Right(DayOfWeek.TUESDAY)
            case 2 => Right(DayOfWeek.WEDNESDAY)
            case 3 => Right(DayOfWeek.THURSDAY)
            case 4 => Right(DayOfWeek.FRIDAY)
            case 5 => Right(DayOfWeek.SATURDAY)
            case 6 => Right(DayOfWeek.SUNDAY)
            case n => Left(DecodingFailure(s"WorkPeriod.DayOfWeek.${fieldName.capitalize}.Invalid($n)", Nil))
          }
        }

      val lt: String => Either[DecodingFailure, LocalTime] = fieldName =>
        hc.get[NonEmptyString](fieldName).flatMap { string =>
          val (h, m) = string.value.splitAt(2)
          (h.toIntOption, m.toIntOption)
            .mapN(LocalTime.of)
            .toRight(DecodingFailure(s"WorkPeriod.LocalTime.Invalid($string)", Nil))

        }

      for {
        cd <- wd("closeDay")
        ct <- lt("closeTime")
        od <- wd("openDay")
        ot <- lt("openTime")
      } yield WorkPeriod(closeDay = cd, closeTime = ct, openDay = od, openTime = ot)
    }

}
