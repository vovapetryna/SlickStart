package api.controllerImpl

import api.controllerInterface.{ChatUserControllerI, MessageControllerI}
import model.{Chat, Message, User}
import repository.MessageRepo

import scala.concurrent.{ExecutionContext, Future}

class MessageController(
    repo: MessageRepo,
    chatUserController: ChatUserControllerI
)(implicit ec: ExecutionContext)
    extends MessageControllerI {
  def create(message: Message): Future[Message] = for {
    chats <- chatUserController.chatsByUser(message.senderId)
    sent <- repo.create(message) if chats.map(_.id).contains(message.chatId)
  } yield sent

  def createAll(messages: Seq[Message]): Future[Seq[Message]] =
    repo.createAll(messages)

  def getById(id: Message.Id): Future[Message] = repo.getById(id).map(_.head)

  def getAll: Future[Seq[Message]] = repo.getAll

  def delete(id: Message.Id): Future[Int] = repo.delete(id)

  def getAllByChat(id: Chat.Id): Future[Seq[Message]] = repo.getAllByChat(id)

  def editById(id: Message.Id, content: String): Future[Int] =
    getById(id).flatMap(message => repo.edit(message.copy(content = content)))

  def search(substring: String): Future[Seq[Message]] = repo.search(substring)
}
