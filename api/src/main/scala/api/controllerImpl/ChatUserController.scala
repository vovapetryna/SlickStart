package api.controllerImpl

import api.controllerInterface.ChatUserControllerI
import model.{Chat, ChatUser, User}
import repository.ChatUserRepo

import scala.concurrent.Future

class ChatUserController(repo: ChatUserRepo) extends ChatUserControllerI {
  def create(chatUser: ChatUser): Future[ChatUser] = repo.create(chatUser)

  def createAll(chatsUsers: Seq[ChatUser]): Future[Seq[ChatUser]] =
    repo.createAll(chatsUsers)

  def getAll: Future[Seq[ChatUser]] = repo.getAll

  def chatsByUser(id: User.Id): Future[Seq[Chat]] = repo.chatsByUser(id)

  def usersByChat(id: Chat.Id): Future[Seq[User]] = repo.usersByChat(id)

  def delete(chatId: Chat.Id, userId: User.Id): Future[Int] =
    repo.delete(chatId, userId)
}
