package repository

import model.{Chat, Message}
import profile.PostgresProfile.api._
import table.MessageTable

class MessageRepo(db: Database) {
  def init = db.run(MessageTable.query.schema.create)

  def create(message: Message) = db.run(MessageTable.returnQuery += message)

  def createAll(messages: Seq[Message]) =
    db.run(MessageTable.returnQuery ++= messages)

  def update(message: Message) =
    db.run(MessageTable.byId(message.id).update(message))

  def getById(id: Message.Id) =
    db.run(MessageTable.byId(id).result)

  def getAll() = db.run(MessageTable.query.result)

  def delete(id: Message.Id) = db.run(MessageTable.byId(id).delete)

  def getMessageWithChatAndSender(id: Message.Id) =
    db.run {
      (for {
        message <- MessageTable.byId(id)
        chat <- message.chat
        sender <- message.sender
      } yield (message, chat, sender)).result
    }

  def getAllByChat(id: Chat.Id) = db.run(MessageTable.byChat(id).result)

  def edit(message: Message) =
    db.run(MessageTable.byId(message.id).update(message))

  def search(substring: String) =
    db.run(MessageTable.likeContent(substring).result)
}
