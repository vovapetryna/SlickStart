package api.controllerImpl

import api.controllerInterface.MessageControllerI
import model.Message
import repository.MessageRepo

import scala.concurrent.{ExecutionContext, Future}

class MessageController(repo: MessageRepo)(implicit ec: ExecutionContext) extends MessageControllerI{
  override def create(message: Message): Future[Message] = repo.create(message)

  override def createAll(messages: Seq[Message]): Future[Seq[Message]] = repo.createAll(messages)

  override def getById(id: Message.Id): Future[Message] = repo.getById(id).map(_.head)

  override def getAll: Future[Seq[Message]] = repo.getAll

  override def delete(id: Message.Id): Future[Int] = repo.delete(id)
}
