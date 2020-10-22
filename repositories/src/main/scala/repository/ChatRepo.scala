package repository

import model.Chat
import table.ChatTable
import profile.PostgresProfile.api._

class ChatRepo(db: Database) {

  def init = db.run(ChatTable.query.schema.create)

  def create(chatData: Chat) = db.run(ChatTable.idsQuery += chatData)

  def createAll(chatsData: Seq[Chat]) = db.run(ChatTable.idsQuery ++= chatsData)

  def update(chat: Chat) = db.run(ChatTable.query.filter(_.id === chat.id).update(chat))

  def getById(chatId: Long) =
    db.run(ChatTable.query.filter(_.id === Chat.Id(chatId)).result)

  def getAll() = db.run(ChatTable.query.result)

  def delete(chatId: Long) = db.run(ChatTable.query.filter(_.id === Chat.Id(chatId)).delete)
}