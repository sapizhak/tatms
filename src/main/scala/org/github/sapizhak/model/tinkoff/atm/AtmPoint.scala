package org.github.sapizhak.model.tinkoff.atm

import eu.timepit.refined.types.string.NonEmptyString
import org.github.sapizhak.model.tinkoff.GeoPoint

final case class AtmPoint(
  address: AtmAddress,
  atmInfo: AtmInfo,
  id: AtmId,
  installPlace: NonEmptyString,
  limits: Set[ShortLimit],
  location: GeoPoint,
  phone: List[NonEmptyString],
  workPeriods: List[WorkPeriod]
)
