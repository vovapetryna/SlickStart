package repository

import model.Chat
import table.ChatTable
import profile.PostgresProfile.api._

class ChatRepo(db: Database) {

  def init = db.run(ChatTable.query.schema.create)

  def create(chat: Chat) = db.run(ChatTable.returnQuery += chat)

  def createAll(chats: Seq[Chat]) = db.run(ChatTable.returnQuery ++= chats)

  def update(chat: Chat) = db.run(ChatTable.byId(chat.id).update(chat))

  def getById(id: Chat.Id) =
    db.run(ChatTable.byId(id).result)

  def getAll = db.run(ChatTable.query.result)

  def delete(id: Chat.Id) = db.run(ChatTable.byId(id).delete)
}