package api.akktor
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}
import repository.{ChatRepo, MessageRepo}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object ChatManager {
  def apply(
      messageRepo: MessageRepo,
      chatRepo: ChatRepo
  )(implicit ec: ExecutionContext): Behavior[ChatManager.Command] =
    Behaviors.setup(context => new ChatManager(context, messageRepo, chatRepo))

  sealed trait Command
  final case class CreateChat(
      chat: model.Chat,
      replyTo: ActorRef[RespondChatCreated]
  ) extends Command
  final case class RespondChatCreated(
      chatActor: Either[String, (model.Chat.Id, ActorRef[ChatActor.Command])]
  ) extends Command
}

class ChatManager(
    context: ActorContext[ChatManager.Command],
    messageRepo: MessageRepo,
    chatRepo: ChatRepo
)(implicit val ec: ExecutionContext)
    extends AbstractBehavior[ChatManager.Command](context) {
  import ChatManager._

  override def onMessage(msg: ChatManager.Command): Behavior[ChatManager.Command] =
    msg match {
      case CreateChat(chat, replyTo) =>
        val createdChat = chatRepo.create(chat)
        createdChat.onComplete {
          case Success(chat) =>
            val chatRef =
              context.spawn(ChatActor(chat, messageRepo), s"${chat.name}-actor")
            replyTo ! RespondChatCreated(Right(chat.id -> chatRef))
          case Failure(ex) => replyTo ! RespondChatCreated(Left(ex.getMessage))
        }
        this
    }
}
