package table

import model.{User, UserPk}
import repository.Profile

trait UserTable { self: Profile =>
  import profile.api._

  class UserTable(tag: Tag) extends Table[User](tag, "USERS") {
    def login = column[String]("LOGIN")
    def id = column[UserPk]("ID", O.PrimaryKey, O.AutoInc)
    def * = (login, id).mapTo[User]
  }

  lazy val users = TableQuery[UserTable]
  lazy val insertUsers = users returning users.map(_.id)
}
