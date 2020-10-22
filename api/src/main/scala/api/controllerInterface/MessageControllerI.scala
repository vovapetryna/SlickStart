package api.controllerInterface

import model.Message

import scala.concurrent.Future

trait MessageControllerI {
  def create(message: Message): Future[Message]

  def createAll(messages: Seq[Message]): Future[Seq[Message]]

  def getById(id: Message.Id): Future[Message]

  def getAll: Future[Seq[Message]]

  def delete(id: Message.Id): Future[Int]
}
