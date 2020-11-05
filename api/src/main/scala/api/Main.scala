package api

import java.time.LocalDateTime

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
import scala.concurrent.{Await, ExecutionContext, Future}

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
  lazy val messageController = new MessageController(messageRepo, chatUserController)
  lazy val chatUserController = new ChatUserController(chatUserRepo)

  def execAndPrint[T](action: Future[T]): T = {
    val retValue = Await.result(action, 2.seconds)
    logger.info("\n" + retValue.toString + "\n\n")
    retValue
  }

  def exec[T](action: Future[T]): T = {
    Await.result(action, 2.seconds)
  }

  val init =
    userRepo.init zip chatRepo.init zip messageRepo.init zip chatUserRepo.init
  Await.result(init, 1.seconds)

  val testUsers = Seq(
    User(User.Id.empty, "FooBar"),
    User(User.Id.empty, "Petya"),
    User(User.Id.empty, "FooBarBaz")
  )
  val testChats =
    Seq(Chat(Chat.Id.empty, "Global"), Chat(Chat.Id.empty, "News"))
  val testMessages =
    Seq("Hi buddy)", "How r u buddy", "scala the best")


  logger.info("Create users and chats")
  val users = execAndPrint(userController.createAll(testUsers))
  val chats = execAndPrint(chatController.createAll(testChats))

  val chatUserCombinations = for {
    c <- chats
    u <- users
  } yield ChatUser(c.id, u.id)
  exec(chatUserController.createAll(chatUserCombinations))

  val messageCombinations = for {
    c <- chats
    u <- users
    m <- testMessages
  } yield Message(Message.Id.empty, u.id, c.id, m, LocalDateTime.now())
  val messages = execAndPrint(messageController.createAll(messageCombinations))

  logger.info("Get all first user's chats after he left first chat")
  exec(userController.leaveChat(chats.head.id, users.head.id))
  execAndPrint(chatUserController.chatsByUser(users.head.id))

  logger.info("Get all first chat's members")
  execAndPrint(chatUserController.usersByChat(chats.head.id))

  logger.info("Get all first chat's messages")
  execAndPrint(messageController.getAllByChat(chats.head.id))

  logger.info("Edit old message content")
  exec(
    messageController
      .editById(messages.head.id, "Editing message text is really simple!")
  )
  execAndPrint(messageController.getById(messages.head.id))

  logger.info("Try to write to unauthorized chat")
  execAndPrint(
    messageController.create(
      Message(Message.Id.empty, users.head.id, chats.head.id, "Test message to wrong chat", LocalDateTime.now())
    ) recover { case _ => s" ! Sorry you have not permissions to write to chat ${chats.head.id} join it first)"}
  )
  logger.info("Join that chat and try again")
  execAndPrint(userController.joinChat(users.head.id, chats.head.id))
  execAndPrint(
    messageController.create(
      Message(Message.Id.empty, users.head.id, chats.head.id, "Test message to wrong chat after joining it!", LocalDateTime.now())
    )
  )
  execAndPrint(messageController.getAllByChat(chats.head.id))

  logger.info("Search all users with substring in login")
  execAndPrint(userController.searchByLogin("Bar"))

  logger.info("Search message with \"buddy\" substring")
  execAndPrint(messageController.search("buddy"))
}
