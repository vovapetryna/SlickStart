package api.controllerImpl

import api.controllerInterface.ChatUserControllerI
import model.{Chat, ChatUser, User}
import repository.ChatUserRepo

import scala.concurrent.Future

class ChatUserController(repo: ChatUserRepo) extends ChatUserControllerI {
  override def create(chatUser: ChatUser): Future[ChatUser] = repo.create(chatUser)

  override def createAll(chatsUsers: Seq[ChatUser]): Future[Seq[ChatUser]] = repo.createAll(chatsUsers)

  override def getAll: Future[Seq[ChatUser]] = repo.getAll

  override def chatsByUser(id: User.Id): Future[Seq[Chat]] = repo.chatsByUser(id)

  override def usersByChat(id: Chat.Id): Future[Seq[User]] = repo.usersByChat(id)

  override def delete(chatId: Chat.Id, userId: User.Id): Future[Int] = repo.delete(chatId, userId)
}
