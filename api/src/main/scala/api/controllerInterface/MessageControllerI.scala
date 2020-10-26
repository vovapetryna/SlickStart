package api.controllerInterface

import model.{Chat, Message}

import scala.concurrent.Future

trait MessageControllerI {
  def create(message: Message): Future[Message]

  def createAll(messages: Seq[Message]): Future[Seq[Message]]

  def getById(id: Message.Id): Future[Message]

  def getAll: Future[Seq[Message]]

  def delete(id: Message.Id): Future[Int]

  def getAllByChat(id: Chat.Id): Future[Seq[Message]]

  def editById(id: Message.Id, content: String): Future[Int]
}
