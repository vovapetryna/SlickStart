package api.controllerInterface

import model.{Chat, ChatUser, User}

import scala.concurrent.Future

trait ChatUserControllerI {
  def create(chatUser: ChatUser): Future[ChatUser]

  def createAll(chatsUsers: Seq[ChatUser]): Future[Seq[ChatUser]]

  def getAll: Future[Seq[ChatUser]]

  def chatsByUser(id: User.Id): Future[Seq[Chat]]

  def usersByChat(id: Chat.Id): Future[Seq[User]]

  def delete(chatId: Chat.Id, userId: User.Id): Future[Int]
}
