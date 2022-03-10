package org.github.sapizhak.service

import eu.timepit.refined.types.all.PosInt
import eu.timepit.refined.types.string.NonEmptyString
import org.github.sapizhak.model.tinkoff.Currency
import org.github.sapizhak.model.tinkoff.atm.{AtmPoint, WorkPeriod}
import eu.timepit.refined.auto._
import cats.syntax.all._

sealed trait AtmEvent extends Product with Serializable { self =>

  def currency: Currency
  def amount: PosInt

  private final def message(p: AtmPoint): String = {

    val header: String = self match {
      case w: AtmEvent.Withdrawn =>
        s"🏧 ⬇️: $amount ${currency.extract} WAS WITHDRAWN FROM AN ATM (CURRENT LIMIT: ${w.current}) 🏧"
      case a: AtmEvent.Added =>
        s"🏧 ⬆️: $amount ${currency.extract} WAS ADDED TO AN ATM 🏧 (CURRENT LIMIT: ${a.current})"
      case _: AtmEvent.Loaded => s"🏧 ↕️: $amount ${currency.extract} WAS LOADED INTO AN ATM 🏧"
    }

    val wperiods: List[WorkPeriod] =
      p.workPeriods
        .sortBy(a => (a.openDay, a.closeDay))
        .distinctBy(s => (s.openTime, s.closeTime))

    val periods: String =
      (wperiods.length == 1)
        .guard[Option]
        .flatMap(_ => wperiods.headOption.map(p => s"EVERYDAY: ${p.openTime}-${p.closeTime}"))
        .getOrElse(wperiods.map(p => s"${p.openDay}: ${p.openTime}-${p.closeTime}").mkString(","))

    s"""$header
       |ADDRESS = ${p.address.extract.toUpperCase}
       |GMAP = https://www.google.com/maps/search/?api=1&query=${p.location.lat},${p.location.lng}
       |YMAP = http://maps.yandex.ru/?text=${p.location.lat},${p.location.lng}
       |WORKS = $periods
       |PHONES = ${p.phone.map(_.value).mkString_(",")}
       |""".stripMargin
  }

  val makeMessage: AtmPoint => NonEmptyString = p => NonEmptyString.unsafeFrom(message(p))

}

object AtmEvent {

  final case class Withdrawn(currency: Currency, amount: PosInt, current: PosInt) extends AtmEvent

  final case class Added(currency: Currency, amount: PosInt, current: PosInt) extends AtmEvent

  final case class Loaded(currency: Currency, amount: PosInt) extends AtmEvent

}
