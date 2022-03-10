package org.github.sapizhak.model.tinkoff.atm

import eu.timepit.refined.types.string.NonEmptyString
import org.github.sapizhak.model.tinkoff.Bank

final case class AtmBrand(
  brand: Bank,
  name: NonEmptyString
)
