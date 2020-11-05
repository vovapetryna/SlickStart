package api.akktor

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}
import repository.{ChatRepo, MessageRepo}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

object ChatManager {
  def apply(
      messageRepo: MessageRepo,
      chatRepo: ChatRepo
  ): Behavior[ChatManager.Command] =
    Behaviors.setup(context => new ChatManager(context, messageRepo, chatRepo))

  sealed trait Command
  final case class CreateChat(
      chat: model.Chat,
      replyTo: ActorRef[RespondChatCreated]
  ) extends Command
  final case class RespondChatCreated(
      chatActor: (model.Chat.Id, ActorRef[ChatActor.Command])
  ) extends Command

  final case class GetChat(id: model.Chat.Id, replyTo: ActorRef[ResponseChat]) extends Command
  final case class ResponseChat(chat: model.Chat) extends Command
}

class ChatManager(
    context: ActorContext[ChatManager.Command],
    messageRepo: MessageRepo,
    chatRepo: ChatRepo
) extends AbstractBehavior[ChatManager.Command](context) {
  import ChatManager._

  implicit val ec: ExecutionContextExecutor = context.executionContext

  override def onMessage(
      msg: ChatManager.Command
  ): Behavior[ChatManager.Command] =
    msg match {
      case CreateChat(chat, replyTo) =>
        chatRepo
          .create(chat)
          .map(chat =>
            RespondChatCreated(chat.id -> context.spawn(ChatActor(chat, messageRepo), s"${chat.name}-actor"))
          )
          .map(replyTo ! _)
        this
      case GetChat(id, replyTo) =>
        chatRepo
          .getById(id)
          .map(_.head)
          .map { chat: model.Chat => ResponseChat(chat) }
          .map(replyTo ! _)
        this
    }
}


