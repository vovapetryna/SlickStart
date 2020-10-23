package api

import api.controllerImpl.{
  ChatController,
  ChatUserController,
  MessageController,
  UserController
}
import com.typesafe.scalalogging.Logger
import model.{Chat, ChatUser, Message, User}
import org.slf4j.LoggerFactory
import profile.PostgresProfile.api._
import repository.{ChatRepo, ChatUserRepo, MessageRepo, UserRepo}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext}

object Main extends App {
  private val logger = Logger(LoggerFactory.getLogger(this.getClass))

  val db = Database.forConfig("slickH2Db")

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

  val userRepo = new UserRepo(db)
  val chatRepo = new ChatRepo(db)
  val messageRepo = new MessageRepo(db)
  val chatUserRepo = new ChatUserRepo(db)

  lazy val userController = new UserController(userRepo, chatUserController)
  lazy val chatController = new ChatController(chatRepo, chatUserController)
  lazy val messageController = new MessageController(messageRepo)
  lazy val chatUserController = new ChatUserController(chatUserRepo)

  val init =
    userRepo.init zip chatRepo.init zip messageRepo.init zip chatUserRepo.init
  Await.result(init, 1.seconds)

  val testUsers = Seq(
    User(User.Id.empty, "vova"),
    User(User.Id.empty, "Petya"),
    User(User.Id.empty, "Vasya")
  )
  val testChats =
    Seq(Chat(Chat.Id.empty, "Global"), Chat(Chat.Id.empty, "News"))
  val testMessages =
    Seq("Hi buddy)", "How r u", "Thank you!)", "u r welcome", "scala the best")

  val populateUsersChats = for {
    insertedUsersIds <- userController.createAll(testUsers)
    insertedChatsIds <- chatController.createAll(testChats)
  } yield (insertedChatsIds, insertedUsersIds)

  val chatUsersRelations = for {
    (chats, users) <- populateUsersChats
  } yield (for { c <- chats; u <- users } yield (c, u)).map {
    case (chat, user) => ChatUser(chat.id, user.id)
  }

  val messageRelations = for {
    (chats, users) <- populateUsersChats
  } yield (for { c <- chats; u <- users; m <- testMessages } yield (c, u, m))
    .map {
      case (chat, user, message) =>
        Message(Message.Id.empty, user.id, chat.id, message)
    }

  val populateChatUserRelation = for {
    chatUser <- chatUsersRelations
    insertedRelations <- chatUserController.createAll(chatUser)
  } yield insertedRelations

  val populateMessages = for {
    messages <- messageRelations
    insertedMessage <- messageController.createAll(messages)
  } yield insertedMessage

  val getUsersChats = for {
    chatUserRelation <- populateChatUserRelation
    chatsBUser <- chatUserRepo.chatsByUser(chatUserRelation.head.userId)
    usersBChat <- chatUserRepo.usersByChat(chatUserRelation.head.chatId)
  } yield (chatsBUser, usersBChat)

  println(Await.result(getUsersChats, 2.seconds))
  println(Await.result(populateMessages, 2.seconds))
}
