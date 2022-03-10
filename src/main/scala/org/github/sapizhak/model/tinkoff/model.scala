package org.github.sapizhak.model.tinkoff
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Interval

package object model {

  type LatitudeInterval  = Interval.Open[W.`-90.0`.T, W.`90.0`.T]
  type LongitudeInterval = Interval.Open[W.`-180.0`.T, W.`180.0`.T]

  type Latitude  = Double Refined LatitudeInterval
  type Longitude = Double Refined LongitudeInterval

}
