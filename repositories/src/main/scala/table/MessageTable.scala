package table

import model.{Chat, Message, User}
import profile.PostgresProfile.api._

class MessageTable(tag: Tag) extends Table[Message](tag, "messages") {
  def id = column[Message.Id]("id", O.PrimaryKey, O.AutoInc)
  def content = column[String]("content")
  def senderId = column[User.Id]("sender")
  def chatId = column[Chat.Id]("chat")
  def * = (id, senderId, chatId, content).mapTo[Message]
  def sender = foreignKey("MessageTableUserTableSenderId", senderId, UserTable.query)(_.id)
  def chat = foreignKey("MessageTableChatTableChatId", chatId, ChatTable.query)(_.id)
}

object MessageTable {
  val query = TableQuery[MessageTable]
  val returnQuery = query returning query

  def byId(id: Message.Id) = query.filter(_.id === id)
}

