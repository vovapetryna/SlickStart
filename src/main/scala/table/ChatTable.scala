package table

import model.{Chat, ChatPk}
import repository.Profile

trait ChatTable {
  self: Profile =>

  import profile.api._

  class ChatsTable(tag: Tag) extends Table[Chat](tag, "CHATS") {
    def id = column[ChatPk]("ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def * = (name, id).mapTo[Chat]
  }

  lazy val chats = TableQuery[ChatsTable]
  lazy val insertChats = chats returning chats.map(_.id)
}
