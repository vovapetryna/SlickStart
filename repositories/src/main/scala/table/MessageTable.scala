package table

import model.{User, Message, Chat}
import profile.PostgresProfile.api._

class MessageTable(tag: Tag) extends Table[Message](tag, "messages") {
  def id = column[Message.Id]("id", O.PrimaryKey, O.AutoInc)
  def content = column[String]("content")
  def senderId = column[User.Id]("sender")
  def chatId = column[Chat.Id]("chat")
  def * = (id, senderId, chatId, content).mapTo[Message]
  def sender = foreignKey("senderFk", senderId, UserTable.query)(_.id)
  def chat = foreignKey("chatFk", chatId, ChatTable.query)(_.id)
}

object MessageTable {
  val query = TableQuery[MessageTable]
  val idsQuery = query returning query
}

