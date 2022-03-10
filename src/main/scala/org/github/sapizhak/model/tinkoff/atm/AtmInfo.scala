package org.github.sapizhak.model.tinkoff.atm

final case class AtmInfo(available: Boolean, isTerminal: Boolean, limits: List[FullLimit], statuses: Statuses)
