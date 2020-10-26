package api.controllerImpl

import api.controllerInterface.ChatControllerI
import model.{Chat, User}
import repository.ChatRepo

import scala.concurrent.{ExecutionContext, Future}

class ChatController(repo: ChatRepo, chatUserController: ChatUserController)(
    implicit ec: ExecutionContext
) extends ChatControllerI {
  def create(chat: Chat): Future[Chat] = repo.create(chat)

  def createAll(chats: Seq[Chat]): Future[Seq[Chat]] =
    repo.createAll(chats)

  def getById(id: Chat.Id): Future[Chat] = repo.getById(id).map(_.head)

  def getAll: Future[Seq[Chat]] = repo.getAll

  def delete(id: Chat.Id): Future[Int] = repo.delete(id)

  def deleteUser(chatId: Chat.Id, userId: User.Id): Future[Int] =
    chatUserController.delete(chatId, userId)
}
