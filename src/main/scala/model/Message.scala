package model

import slick.lifted.MappedTo

case class MessagePk(value: Long) extends AnyVal with MappedTo[Long]

case class Message (senderId: UserPk,
                    content: String,
                    chatId: Option[ChatPk] = None,
                    id: MessagePk = MessagePk(0L))
