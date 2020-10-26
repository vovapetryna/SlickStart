package model

import java.time.LocalDateTime

case class Message(
    id: Message.Id,
    senderId: User.Id,
    chatId: Chat.Id,
    content: String,
    timeSent: LocalDateTime
)

object Message {
  case class Id(value: Long) extends AnyVal

  object Id {
    val empty: Id = Id(-1L)
  }

  val tupled = (this.apply _).tupled
}

