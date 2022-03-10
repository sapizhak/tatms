package org.github.sapizhak.model.tinkoff.http
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.github.sapizhak.model.tinkoff.Payload

final case class ClustersResponse(payload: Payload)
