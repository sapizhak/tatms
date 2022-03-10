package org.github.sapizhak.model.tinkoff.atm

import eu.timepit.refined.types.all.{NonNegInt, PosInt}
import org.github.sapizhak.model.tinkoff.Currency

final case class FullLimit(
  amount: NonNegInt,
  currency: Currency,
  depositionDenominations: List[PosInt],
  depositionMaxAmount: PosInt,
  depositionMinAmount: PosInt,
  overTrustedLimit: Boolean,
  withdrawDenominations: List[PosInt],
  withdrawMaxAmount: PosInt
)
