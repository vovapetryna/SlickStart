package repository

import model.{Chat, ChatPk}
import table.ChatTable

trait ChatRepo extends ChatTable { self: Profile =>

  import profile.api._

  def schemaInit = db.run(chats.schema.create)
  def create(chatData: Chat) = db.run(chats += chatData)
  def createM(chatsData: Seq[Chat]) = db.run(chats ++= chatsData)
  def update(chat: Chat) = db.run(chats.filter(_.id === chat.id).update(chat))
  def getById(chatId: ChatPk) = db.run(chats.filter(_.id === chatId).result)
  def getAll() = db.run(chats.result)
  def delete(chatId: ChatPk) = db.run(chats.filter(_.id === chatId).delete)
}

object ChatRepo extends ChatTable with H2Profile