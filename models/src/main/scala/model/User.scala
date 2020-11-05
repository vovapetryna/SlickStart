package model

case class User(id: User.Id, login: String) {
  override def toString: String = s"\n * User login : ${this.login}\n * User id : ${this.id}"
}

object User {
  case class Id(value: Long) extends AnyVal

  object Id {
    val empty: Id = Id(-1L)
  }

  val tupled: ((Id, String)) => User = (this.apply _).tupled
}
