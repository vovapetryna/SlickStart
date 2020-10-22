package model

case class Message(
    id: Message.Id,
    senderId: User.Id,
    chatId: Chat.Id,
    content: String
)

object Message {
  case class Id(value: Long) extends AnyVal

  object Id {
    val empty: Id = Id(-1L)
  }

  val tupled = (this.apply _).tupled
}
