package api.controllerImpl

import api.controllerInterface.MessageControllerI
import model.{Chat, Message}
import repository.MessageRepo

import scala.concurrent.{ExecutionContext, Future}

class MessageController(repo: MessageRepo)(implicit ec: ExecutionContext)
    extends MessageControllerI {
  def create(message: Message): Future[Message] = repo.create(message)

  def createAll(messages: Seq[Message]): Future[Seq[Message]] =
    repo.createAll(messages)

  def getById(id: Message.Id): Future[Message] = repo.getById(id).map(_.head)

  def getAll: Future[Seq[Message]] = repo.getAll

  def delete(id: Message.Id): Future[Int] = repo.delete(id)

  def getAllByChat(id: Chat.Id): Future[Seq[Message]] = repo.getAllByChat(id)

  def editById(id: Message.Id, content: String): Future[Int] =
    getById(id).flatMap(message => repo.edit(message.copy(content = content)))
}
