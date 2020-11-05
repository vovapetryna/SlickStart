package api.akktor

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}
import repository.MessageRepo

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object ChatActor {
  def apply(chat: model.Chat, messageRepo: MessageRepo)(implicit ec: ExecutionContext): Behavior[Command] =
    Behaviors.setup(context => new ChatActor(context, chat, messageRepo))

  sealed trait Command
  final case class RespondMessageAdd(
      eitherMessage: Either[String, model.Message]
  )
  final case class AddMessage(
      message: model.Message,
      replyTo: ActorRef[RespondMessageAdd]
  ) extends Command
}

class ChatActor(
    context: ActorContext[ChatActor.Command],
    chat: model.Chat,
    messageRepo: MessageRepo
)(implicit val ec: ExecutionContext)
    extends AbstractBehavior[ChatActor.Command](context) {
  import ChatActor._

  override def onMessage(msg: ChatActor.Command): Behavior[ChatActor.Command] =
    msg match {
      case AddMessage(messages, replyTo) =>
        val maybeMessage: Future[model.Message] = messageRepo.create(messages)
        maybeMessage.onComplete {
          case Success(message) =>
            replyTo ! RespondMessageAdd(Right(message))
          case Failure(ex) =>
            replyTo ! RespondMessageAdd(Left(ex.getMessage))
        }
        this
    }
}
