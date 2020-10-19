package table

import model.{ChatPk, ChatUser, UserPk}
import repository.Profile

trait ChatUserTable extends ChatTable with UserTable {self: Profile =>

  import profile.api._

  class ChatUserTable(tag: Tag) extends Table[ChatUser](tag, "CHAT_USER") {
    def chatId = column[ChatPk]("CHAT_ID")
    def userId = column[UserPk]("USER_PK")
    def * = (chatId, userId).mapTo[ChatUser]
    def pk = primaryKey("CHAT_USER_PK", (chatId, userId))
    def chatFk = foreignKey("CHAT_MM_FK", chatId, chats)(_.id)
    def userFk = foreignKey("USER_MM_FK", userId, users)(_.id)
  }

  lazy val chatUserRelations = TableQuery[ChatUserTable]
}
