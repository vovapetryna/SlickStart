package table

import model.User
import profile.PostgresProfile.api._
import slick.dbio.Effect
import slick.sql.SqlAction

class UserTable(tag: Tag) extends Table[User](tag, "users") {
  def login = column[String]("login")
  def id = column[User.Id]("id", O.PrimaryKey, O.AutoInc)
  def * = (id, login).mapTo[User]
}

object UserTable {
  val query = TableQuery[UserTable]
  val idsQuery = query returning query

  def byId(id: User.Id): Query[UserTable, User, Seq] = query.filter(_.id === id)
  def findById(id: User.Id): SqlAction[Option[User], NoStream, Effect.Read] = byId(id).result.headOption
}

