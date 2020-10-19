package table

import model.{ChatPk, Message, MessagePk, UserPk}
import repository.Profile

trait MessageTable extends UserTable with ChatTable { self: Profile =>

  import profile.api._

  class MessagesTable(tag: Tag) extends Table[Message](tag, "MESSAGES") {
    def id = column[MessagePk]("ID", O.PrimaryKey, O.AutoInc)
    def content = column[String]("CONTENT")
    def senderId = column[UserPk]("SENDER")
    def chatId = column[Option[ChatPk]]("CHAT")
    def * = (senderId, content, chatId, id).mapTo[Message]
    def sender = foreignKey("SENDER_FK", senderId, users)(_.id)
    def chat = foreignKey("CHAT_FK", chatId, chats)(_.id)
  }

  lazy val messages = TableQuery[MessagesTable]
  lazy val insertMessages = messages returning messages.map(_.id)

}
