package org.github.sapizhak.model.telegram
import eu.timepit.refined.types.string.NonEmptyString
import org.http4s.Uri.Path.{Segment, SegmentEncoder}
import pureconfig.ConfigReader

final case class BotToken(value: NonEmptyString)

object BotToken {

  implicit val botTokenSegmentEncoder: SegmentEncoder[BotToken] =
    SegmentEncoder.instance(a => Segment(s"bot${a.value.value}"))

  implicit val botTokenCR: ConfigReader[BotToken] = ConfigReader.fromNonEmptyString { a =>
    Right(BotToken(NonEmptyString.unsafeFrom(a)))
  }

}
