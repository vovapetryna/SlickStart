package repository

import model.{Message, MessagePk}
import table.MessageTable

trait MessageRepo extends MessageTable { self: Profile =>

  import profile.api._

  def schemaInit = db.run(messages.schema.create)

  def create(message: Message) = db.run(insertMessages += message)

  def createM(messagesData: Seq[Message]) = db.run(messages ++= messagesData)

  def update(message: Message) =
    db.run(messages.filter(_.id === message.id).update(message))

  def getById(messageId: Long) =
    db.run(messages.filter(_.id === MessagePk(messageId)).result)

  def getAll() = db.run(messages.result)

  def delete(messageId: Long) = db.run(messages.filter(_.id === MessagePk(messageId)).delete)

  def getMessageWithChatAndSender(messageId: Long) =
    db.run {
      (for {
        message <- messages
        chat <- message.chat
        sender <- message.sender
        if message.id === MessagePk(messageId)
      } yield (message, chat, sender)).result
    }
}

object MessageRepo extends MessageRepo with H2Profile
