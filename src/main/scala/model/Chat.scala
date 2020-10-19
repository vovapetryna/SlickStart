package model

import slick.lifted.MappedTo

case class ChatPk(value: Long) extends AnyVal with MappedTo[Long]

case class Chat (name: String,
                id: ChatPk = ChatPk(0L))
