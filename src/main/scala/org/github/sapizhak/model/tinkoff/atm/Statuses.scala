package org.github.sapizhak.model.tinkoff.atm

final case class Statuses(
  cardReaderOperational: Boolean,
  cashInAvailable: Boolean,
  criticalFailure: Boolean,
  nfcOperational: Boolean,
  qrOperational: Boolean
)
