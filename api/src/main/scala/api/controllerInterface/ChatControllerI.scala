package api.controllerInterface

import model.{Chat, User}

import scala.concurrent.Future

trait ChatControllerI {
  def create(chat: Chat): Future[Chat]

  def createAll(chats: Seq[Chat]): Future[Seq[Chat]]

  def getById(id: Chat.Id): Future[Chat]

  def getAll: Future[Seq[Chat]]

  def delete(id: Chat.Id): Future[Int]

  def deleteUser(chatId: Chat.Id, userId: User.Id): Future[Int]
}
