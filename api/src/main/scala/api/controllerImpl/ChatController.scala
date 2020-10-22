package api.controllerImpl

import api.controllerInterface.ChatControllerI
import model.{Chat, User}
import repository.ChatRepo

import scala.concurrent.{ExecutionContext, Future}

class ChatController(repo: ChatRepo, chatUserController: ChatUserController)(
    implicit ec: ExecutionContext
) extends ChatControllerI {
  override def create(chat: Chat): Future[Chat] = repo.create(chat)

  override def createAll(chats: Seq[Chat]): Future[Seq[Chat]] =
    repo.createAll(chats)

  override def getById(id: Chat.Id): Future[Chat] = repo.getById(id).map(_.head)

  override def getAll: Future[Seq[Chat]] = repo.getAll

  override def delete(id: Chat.Id): Future[Int] = repo.delete(id)

  override def deleteUser(chatId: Chat.Id, userId: User.Id): Future[Int] =
    chatUserController.delete(chatId, userId)
}
