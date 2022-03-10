package org.github.sapizhak.service
import cats.Monad
import cats.effect.Ref
import cats.effect.kernel.Concurrent
import cats.effect.kernel.Ref.Make
import eu.timepit.refined.types.numeric.PosInt
import org.github.sapizhak.model.tinkoff.Currency
import org.github.sapizhak.model.tinkoff.atm.{AtmId, ShortLimit}

trait Limits[F[_]] {

  def events: ((AtmId, Set[ShortLimit])) => fs2.Stream[F, AtmEvent]

}

object Limits {

  import cats.syntax.all._

  def instance[F[_]: Make: Monad: Concurrent](poll: fs2.Stream[F, (AtmId, Set[ShortLimit])]): F[Limits[F]] =
    Ref.of[F, Map[AtmId, Set[ShortLimit]]](Map.empty).flatMap { ref =>
      poll.evalTap { case (id, l) => ref.update(_.updated(id, l)) }.compile.drain.as {
        new Limits[F] {

          def event(existing: ShortLimit, incoming: ShortLimit): Option[AtmEvent] = {
            val amountdiff = incoming.amount.value - existing.amount.value
            lazy val abs   = PosInt.unsafeFrom(Math.abs(amountdiff))
            if (incoming.amount.value > existing.amount.value) // incoming 600, existing 500
              AtmEvent.Added(existing.currency, abs, incoming.amount).some
            else if (incoming.amount.value < existing.amount.value) // incoming 500, existing 600
              AtmEvent.Withdrawn(existing.currency, abs, incoming.amount).some
            else none
          }

          def events(existingLimits: Set[ShortLimit], incomingLimits: Set[ShortLimit]): Set[AtmEvent] =
            Currency.all.flatMap { c =>
              (
                existingLimits.find(_.currency == c),
                incomingLimits.find(_.currency == c)
              ) match {
                case (Some(existingLimit), Some(incomingLimit)) =>
                  event(existingLimit, incomingLimit)
                case (None, Some(incomingLimit)) =>
                  AtmEvent.Loaded(c, incomingLimit.amount).some
                case _ => none
              }
            }

          def events: ((AtmId, Set[ShortLimit])) => fs2.Stream[F, AtmEvent] = { case (id, incomingLimits) =>
            val fset = ref.modify { map =>
              map.get(id) match {
                case None =>
                  (map.updated(id, incomingLimits), Set.empty[AtmEvent])
                case Some(existingLimits) =>
                  (map.updated(id, incomingLimits), events(existingLimits, incomingLimits))
              }
            }
            fs2.Stream.evalSeq(fset.map(_.toSeq))
          }
        }
      }
    }

}
