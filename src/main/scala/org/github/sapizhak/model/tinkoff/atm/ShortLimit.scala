package org.github.sapizhak.model.tinkoff.atm

import eu.timepit.refined.types.all.PosInt
import org.github.sapizhak.model.tinkoff.Currency

final case class ShortLimit(amount: PosInt, currency: Currency, denominations: List[PosInt], max: PosInt)

object ShortLimit {

  implicit val shortLimitOrdering: Ordering[ShortLimit] = Ordering.by(_.currency)

}
