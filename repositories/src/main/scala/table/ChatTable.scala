package table

import model.Chat
import profile.PostgresProfile.api._

class ChatTable(tag: Tag) extends Table[Chat](tag, "chats") {
  def id = column[Chat.Id]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def * = (id, name).mapTo[Chat]
}

object ChatTable {
  val query = TableQuery[ChatTable]
  val idsQuery = query returning query
}
