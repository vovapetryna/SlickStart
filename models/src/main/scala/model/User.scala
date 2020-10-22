package model

case class User(id: User.Id, login: String)

object User {
  case class Id(value: Long) extends AnyVal

  object Id {
    val empty: Id = Id(-1L)
  }

  val tupled = (this.apply _).tupled
}
