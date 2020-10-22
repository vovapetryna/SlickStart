package api.controllerImpl

import api.controllerInterface.UserControllerI
import model.{Chat, ChatUser, User}
import repository.UserRepo

import scala.concurrent.{ExecutionContext, Future}

class UserController(repo: UserRepo, chatUserController: ChatUserController)(
    implicit ec: ExecutionContext
) extends UserControllerI {

  override def create(user: User): Future[User] = repo.create(user)

  override def createAll(users: Seq[User]): Future[Seq[User]] =
    repo.createAll(users)

  override def getById(id: User.Id): Future[User] =
    repo.getById(id).map(_.head)

  override def getAll: Future[Seq[User]] = repo.getAll

  override def delete(id: User.Id): Future[Int] = repo.delete(id)

  override def addChat(userId: User.Id, chatId: Chat.Id): Future[ChatUser] =
    chatUserController.create(ChatUser(chatId, userId))

  override def leaveChat(chatId: Chat.Id, userId: User.Id): Future[Int] =
    chatUserController.delete(chatId, userId)
}
