package org.github.sapizhak.model.tinkoff
import eu.timepit.refined.types.string.NonEmptyString
import org.github.sapizhak.model.tinkoff.atm.AtmPoint

final case class Cluster(
  bounds: Bounds,
  center: GeoPoint,
  hash: NonEmptyString,
  id: NonEmptyString,
  points: List[AtmPoint]
)
