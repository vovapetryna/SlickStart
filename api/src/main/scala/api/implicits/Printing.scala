package api.implicits

import model.{Chat, Message, User}

object Printing {
  implicit def messageToPrettyStringConverter(message: Message): String = {
    val margin = message.senderId.toString.length
    val textLength =
      message.timeSent.toString.length.max(message.content.length)

    "Message: " + message.id.toString + " " * 3 + "-" * (textLength + 4) + "\n" +
      "Chat:   " + message.chatId.toString + " " * 4 + "| " + message.content + " " * (textLength - message.content.length) + " |\n" +
      "Author: " + message.senderId.toString + " " * 3 + "/  " + message.timeSent.toString + " " * (textLength - message.timeSent.toString.length) + " |\n" +
      " " * (margin + 11) + "-" * (textLength + 5)
  }

  implicit def messageToStringConverter(messages: Seq[Message]): String =
    messages.foldLeft("") {
      case (resText, message) =>
        resText + "\n\n" + messageToPrettyStringConverter(message)
    }

  implicit def userToStringConverter(user: User): String = {
    s" * User login : ${user.login}\n * User id : ${user.id}"
  }

  implicit def usersToStringConverter(users: Seq[User]): String =
    users.foldLeft("") {
      case (resText, user) =>
        resText + "\n\n" + userToStringConverter(user)
    }

  implicit def chatToStringConverter(chat: Chat): String = {
    s" - Chat name : ${chat.name}\n - Chat id : ${chat.id}"
  }

  implicit def chatsToStringConverter(users: Seq[Chat]): String =
    users.foldLeft("") {
      case (resText, chat) =>
        resText + "\n\n" + chatToStringConverter(chat)
    }
}
