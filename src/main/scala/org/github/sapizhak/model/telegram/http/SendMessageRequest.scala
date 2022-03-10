package org.github.sapizhak.model.telegram.http
import eu.timepit.refined.types.string.NonEmptyString
import org.http4s.{QueryParamEncoder, QueryParamKeyLike}

final case class SendMessageRequest(chatId: NonEmptyString, text: NonEmptyString, parseMode: NonEmptyString)

object SendMessageRequest {

  import eu.timepit.refined.auto._

  def make(chatId: NonEmptyString, text: NonEmptyString): SendMessageRequest =
    SendMessageRequest(chatId, text, "MarkdownV2")

}
