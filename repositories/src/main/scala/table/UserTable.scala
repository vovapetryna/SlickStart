package table

import model.User
import profile.PostgresProfile.api._

class UserTable(tag: Tag) extends Table[User](tag, "users") {
  def login = column[String]("login")
  def id = column[User.Id]("id", O.PrimaryKey, O.AutoInc)
  def * = (id, login).mapTo[User]
}

object UserTable {
  val query = TableQuery[UserTable]
  val returnQuery = query returning query

  def byId(id: User.Id) = query.filter(_.id === id)
  def byLogin(login: String) = query.filter(_.login === login)
  def likeLogin(login: String) = query.withFilter(_.login like s"%$login%")
}

