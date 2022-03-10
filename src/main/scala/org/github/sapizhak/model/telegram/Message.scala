package org.github.sapizhak.model.telegram

final case class Message(messageId: Int, senderChat: Chat, chat: Chat, date: Long, text: String)
