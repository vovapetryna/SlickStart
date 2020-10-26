package api.controllerInterface

import model.{Chat, ChatUser, User}

import scala.concurrent.Future

trait UserControllerI {
  def create(user: User): Future[User]

  def createAll(users: Seq[User]): Future[Seq[User]]

  def getById(id: User.Id): Future[User]

  def getAll: Future[Seq[User]]

  def delete(id: User.Id): Future[Int]

  def joinChat(userId: User.Id, chatId: Chat.Id): Future[ChatUser]

  def leaveChat(chatId: Chat.Id, userId: User.Id): Future[Int]

  def searchByLogin(login: String): Future[Seq[User]]
}
