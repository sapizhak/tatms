package org.github.sapizhak.model.tinkoff

import eu.timepit.refined.types.string.NonEmptyString
import pureconfig.ConfigReader
import pureconfig.error.UnknownKey

sealed abstract class City(val extract: NonEmptyString) extends Product with Serializable

object City {

  import eu.timepit.refined.auto._

  case object SaintPetersburg extends City("saint-petersburg")
  case object Ekaterinburg    extends City("ekaterinburg")
  case object Moscow          extends City("moscow")
  case object Kazan           extends City("kazan")
  case object Novosibirsk     extends City("novosibirsk")
  case object NizhniNovgorod  extends City("nizhni-novgorod")
  case object Chelyabinsk     extends City("chelyabinsk")
  case object Samara          extends City("samara")
  case object Omks            extends City("omsk")
  case object RostovNaDonu    extends City("rostov-na-donu")
  case object Ufa             extends City("ufa")
  case object Krasnoyarsk     extends City("krasnoyarsk")
  case object Voronezh        extends City("voronezh")
  case object Perm            extends City("perm")
  case object Volgograd       extends City("volgograd")

  private val all: Set[City] =
    Set(
      SaintPetersburg,
      Ekaterinburg,
      Moscow,
      Kazan,
      Novosibirsk,
      NizhniNovgorod,
      Chelyabinsk,
      Samara,
      Omks,
      RostovNaDonu,
      Ufa,
      Krasnoyarsk,
      Voronezh,
      Perm,
      Volgograd
    )

  implicit val cityCR: ConfigReader[City] = ConfigReader.fromNonEmptyString { s =>
    City.all.find(_.extract.value == s).toRight(UnknownKey(s))
  }

}
