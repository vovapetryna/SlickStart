package table

import model.{Chat, ChatUser, User}
import profile.PostgresProfile.api._

class ChatUserTable(tag: Tag) extends Table[ChatUser](tag, "chatsUsers") {
  def chatId = column[Chat.Id]("chatId")
  def userId = column[User.Id]("userId")
  def * = (chatId, userId).mapTo[ChatUser]
  def pk = primaryKey("chatUserPk", (chatId, userId))
  def chat =
    foreignKey("ChatUserTableChatTableChatId", chatId, ChatTable.query)(_.id)
  def user =
    foreignKey("ChatUserTableUserTableUserId", userId, UserTable.query)(_.id)
}

object ChatUserTable {
  val query = TableQuery[ChatUserTable]
  val returnQuery = query returning query

  def byUserId(id: User.Id) = query.filter(_.userId === id)
  def byChatId(id: Chat.Id) = query.filter(_.chatId === id)
  def buChatAndUser(chatId: Chat.Id, userId: User.Id) =
    query.filter(p => p.chatId === chatId && p.userId === userId)
}
