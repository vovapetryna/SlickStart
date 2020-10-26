package model

case class ChatUser(chatId: Chat.Id, userId: User.Id) {
  override def toString: String = s"\n + user $userId has permissions to write to chat ---> $chatId"
}
