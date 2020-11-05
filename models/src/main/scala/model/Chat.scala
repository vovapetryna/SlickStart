package model

case class Chat(id: Chat.Id, name: String) {
  override def toString: String = s"\n - Chat name : ${this.name}\n - Chat id : ${this.id}"
}

object Chat {
  case class Id(value: Long) extends AnyVal

  object Id {
    val empty: Id = Id(-1L)
  }

  val tupled: ((Id, String)) => Chat = (this.apply _).tupled
}
