package repository

import model._
import slick.jdbc.JdbcProfile

trait DataBaseModule {
  val profile: JdbcProfile

  import profile.api._

  class UserTable(tag: Tag) extends Table[User](tag, "USERS") {
    def login = column[String]("LOGIN")
    def id = column[UserPk]("ID", O.PrimaryKey, O.AutoInc)
    def * = (login, id).mapTo[User]
  }

  lazy val users = TableQuery[UserTable]
  lazy val insertUsers = users returning users.map(_.id)

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

  class ChatsTable(tag: Tag) extends Table[Chat](tag, "CHATS") {
    def id = column[ChatPk]("ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def * = (name, id).mapTo[Chat]
  }

  lazy val chats = TableQuery[ChatsTable]
  lazy val insertChats = chats returning chats.map(_.id)
}

object H2DatabaseLayer {
  val db = new DataBaseModule {val profile = slick.jdbc.H2Profile}
}
