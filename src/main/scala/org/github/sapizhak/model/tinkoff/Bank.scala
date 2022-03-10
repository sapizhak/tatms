package org.github.sapizhak.model.tinkoff

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.{Decoder, Encoder}

sealed abstract class Bank(val extract: NonEmptyString) extends Product with Serializable

object Bank {

  import eu.timepit.refined.auto._
  import io.circe.refined._

  case object Sberbank     extends Bank("11242")
  case object Vtb          extends Bank("11249")
  case object Alpha        extends Bank("11250")
  case object Raiffeisen   extends Bank("11241")
  case object Gazprom      extends Bank("11371")
  case object MoscowCredit extends Bank("11475")
  case object RosBank      extends Bank("11248")
  case object PromSvyaz    extends Bank("11243")
  case object UralSib      extends Bank("11245")
  case object Otkritie     extends Bank("11633")
  case object Tinkoff      extends Bank("tcs")

  val all: Set[Bank] =
    Set(Sberbank, Vtb, Alpha, Raiffeisen, Gazprom, MoscowCredit, RosBank, PromSvyaz, UralSib, Otkritie, Tinkoff)

  implicit val bankEncoder: Encoder[Bank] =
    Encoder[NonEmptyString].contramap(_.extract)

  implicit val bankDecoder: Decoder[Bank] =
    Decoder[NonEmptyString].emap(s => all.find(_.extract == s).toRight(s"Bank.Unknown($s)"))

}
