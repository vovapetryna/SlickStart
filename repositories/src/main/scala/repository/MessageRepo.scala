package repository

import model.Message
import table.MessageTable
import profile.PostgresProfile.api._

class MessageRepo(db: Database) {

  def init = db.run(MessageTable.query.schema.create)

  def create(message: Message) = db.run(MessageTable.idsQuery += message)

  def createAll(messagesData: Seq[Message]) = db.run(MessageTable.idsQuery ++= messagesData)

  def update(message: Message) =
    db.run(MessageTable.query.filter(_.id === message.id).update(message))

  def getById(messageId: Message.Id) =
    db.run(MessageTable.query.filter(_.id === messageId).result)

  def getAll() = db.run(MessageTable.query.result)

  def delete(messageId: Message.Id) = db.run(MessageTable.query.filter(_.id === messageId).delete)

  def getMessageWithChatAndSender(messageId: Message.Id) =
    db.run {
      (for {
        message <- MessageTable.query if message.id === messageId
        chat <- message.chat
        sender <- message.sender
      } yield (message, chat, sender)).result
    }
}
