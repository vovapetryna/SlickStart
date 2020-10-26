package api.controllerImpl

import api.controllerInterface.{ChatUserControllerI, UserControllerI}
import model.{Chat, ChatUser, User}
import repository.UserRepo

import scala.concurrent.{ExecutionContext, Future}

class UserController(repo: UserRepo, chatUserController: ChatUserControllerI)(
    implicit ec: ExecutionContext
) extends UserControllerI {

  def create(user: User): Future[User] = repo.create(user)

  def createAll(users: Seq[User]): Future[Seq[User]] =
    repo.createAll(users)

  def getById(id: User.Id): Future[User] =
    repo.getById(id).map(_.head)

  def getAll: Future[Seq[User]] = repo.getAll

  def delete(id: User.Id): Future[Int] = repo.delete(id)

  def joinChat(userId: User.Id, chatId: Chat.Id): Future[ChatUser] =
    chatUserController.create(ChatUser(chatId, userId))

  def leaveChat(chatId: Chat.Id, userId: User.Id): Future[Int] =
    chatUserController.delete(chatId, userId)

  def searchByLogin(login: String): Future[Seq[User]] = repo.getLikeLogin(login)
}
