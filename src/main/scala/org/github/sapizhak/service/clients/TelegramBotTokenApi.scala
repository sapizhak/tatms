package org.github.sapizhak.service.clients
import cats.Applicative
import cats.effect.kernel.Concurrent
import org.github.sapizhak.model.telegram.BotToken
import org.github.sapizhak.model.telegram.http.SendMessageRequest
import org.http4s.client.Client
import org.http4s.headers.`Content-Type`
import org.http4s.{MediaType, Method, Request, Uri}

object TelegramBotTokenApi {

  private def makeRequest[F[_]: Concurrent](baseUri: Uri, token: BotToken, request: SendMessageRequest): Request[F] =
    Request[F](
      Method.POST,
      baseUri / token / "sendMessage" +? ("chat_id", request.chatId.value) +? ("text", request.text.value)
    ).withHeaders(`Content-Type`(MediaType.application.`json`))

  def sendMessageFromBot[F[_]: Concurrent: Applicative](
    cli: Client[F],
    baseUri: Uri,
    token: BotToken,
    request: SendMessageRequest
  ): F[Unit] =
    cli.expect(makeRequest(baseUri, token, request))

}
