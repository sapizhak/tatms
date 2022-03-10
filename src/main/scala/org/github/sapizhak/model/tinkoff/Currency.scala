package org.github.sapizhak.model.tinkoff

import cats.Order
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.{Decoder, Encoder}

import scala.collection.immutable.SortedSet

sealed abstract class Currency(val extract: NonEmptyString) extends Product with Serializable

object Currency {

  import eu.timepit.refined.auto._
  import io.circe.refined._

  implicit val orderingCurrency: Ordering[Currency] = Ordering.by(_.extract.value)

  implicit val orderCurrency: Order[Currency] = Order.fromOrdering

  case object USD extends Currency("USD")
  case object EUR extends Currency("EUR")
  case object RUB extends Currency("RUB")

  val foreign: SortedSet[Currency] = SortedSet(USD, EUR)

  val all: Set[Currency] = Set(USD, EUR, RUB)

  implicit val currencyDecoder: Decoder[Currency] =
    Decoder[NonEmptyString].emap(s => all.find(_.extract == s).toRight(s"Currency.Unknown($s)"))

  implicit val currencyEncoder: Encoder[Currency] =
    Encoder[NonEmptyString].contramap(_.extract)

}
