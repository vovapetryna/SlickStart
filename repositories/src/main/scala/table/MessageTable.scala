package table

import java.time.LocalDateTime

import model.{Chat, Message, User}
import profile.PostgresProfile.api._

import scala.concurrent.ExecutionContext

class MessageTable(tag: Tag) extends Table[Message](tag, "messages") {
  def id = column[Message.Id]("id", O.PrimaryKey, O.AutoInc)
  def timeSent = column[LocalDateTime]("timeSent")
  def content = column[String]("content")
  def senderId = column[User.Id]("sender")
  def chatId = column[Chat.Id]("chat")
  def * = (id, senderId, chatId, content, timeSent).mapTo[Message]
  def sender =
    foreignKey("MessageTableUserTableSenderId", senderId, UserTable.query)(_.id)
  def chat =
    foreignKey("MessageTableChatTableChatId", chatId, ChatTable.query)(_.id)
}

object MessageTable {
  val query = TableQuery[MessageTable]
  val returnQuery = query returning query

  def byId(id: Message.Id) = query.filter(_.id === id)
  def byChat(id: Chat.Id) = query.filter(_.chatId === id)

  def likeContent(substring: String) =
    query.withFilter(m => m.content like s"%$substring%")
}
