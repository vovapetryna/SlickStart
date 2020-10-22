package table

import model.{Chat, ChatUser, User}
import profile.PostgresProfile.api._

class ChatUserTable(tag: Tag) extends Table[ChatUser](tag, "chatsUsers") {
  def chatId = column[Chat.Id]("chatId")
  def userId = column[User.Id]("userId")
  def * = (chatId, userId).mapTo[ChatUser]
  def pk = primaryKey("chatUserPk", (chatId, userId))
  def chatFk = foreignKey("chatMmFk", chatId, ChatTable.query)(_.id)
  def userFk = foreignKey("userMmFk", userId, UserTable.query)(_.id)
}

object ChatUserTable {
  val query = TableQuery[ChatUserTable]
}