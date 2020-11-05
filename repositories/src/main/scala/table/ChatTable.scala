package table

import model.Chat
import profile.PostgresProfile.api._

class ChatTable(tag: Tag) extends Table[Chat](tag, "chats") {
  def id = column[Chat.Id]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name", O.Unique)
  def * = (id, name).mapTo[Chat]
}

object ChatTable {
  val query = TableQuery[ChatTable]
  val returnQuery = query returning query

  def byId(id: Chat.Id) = query.filter(_.id === id)
}
