package model

case class Chat(id: Chat.Id, name: String)

object Chat {
  case class Id(value: Long) extends AnyVal

  object Id {
    val empty: Id = Id(-1L)
  }

  val tupled = (this.apply _).tupled
}
