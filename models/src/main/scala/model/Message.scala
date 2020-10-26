package model

import java.time.LocalDateTime

case class Message(
    id: Message.Id,
    senderId: User.Id,
    chatId: Chat.Id,
    content: String,
    timeSent: LocalDateTime


) {
  override def toString: String = {
    val margin = this.senderId.toString.length
    val textLength =
      this.timeSent.toString.length.max(this.content.length)

    "\n\nMessage: " + this.id.toString + " " * 3 + "-" * (textLength + 4) + "\n" +
      "Chat:   " + this.chatId.toString + " " * 4 + "| " + this.content + " " * (textLength - this.content.length) + " |\n" +
      "Author: " + this.senderId.toString + " " * 3 + "/  " + this.timeSent.toString + " " * (textLength - this.timeSent.toString.length) + " |\n" +
      " " * (margin + 11) + "-" * (textLength + 5)
  }
}

object Message {
  case class Id(value: Long) extends AnyVal

  object Id {
    val empty: Id = Id(-1L)
  }

  val tupled = (this.apply _).tupled
}

