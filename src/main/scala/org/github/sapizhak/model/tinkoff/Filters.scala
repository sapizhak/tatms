package org.github.sapizhak.model.tinkoff

final case class Filters(banks: Set[Bank], showUnavailable: Boolean, currencies: Set[Currency])

object Filters {

  val tinkoffUsdAtms: Filters =
    Filters(
      Set(Bank.Tinkoff),
      showUnavailable = true,
      currencies = Set.empty
    )

}
