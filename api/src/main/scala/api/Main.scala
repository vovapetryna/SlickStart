package api

import api.controllerImpl.{
  ChatController,
  ChatUserController,
  MessageController,
  UserController
}
import com.typesafe.scalalogging.Logger
import model.{Chat, ChatUser, User}
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

  val populateDb = for {
    insertedUsersIds <- userRepo.createAll(testUsers)
    insertedChatsIds <- chatRepo.createAll(testChats)
  } yield (insertedUsersIds, insertedChatsIds)

  val usersChats = Await.result(populateDb, 2.seconds)

  val testChatsUsers = usersChats._1
    .lazyZip(usersChats._2)
    .map { case (userId, chatId) => ChatUser(chatId.id, userId.id) }

  val populateChatUser = for {
    insertedRelations <- chatUserRepo.createAll(testChatsUsers)
  } yield insertedRelations

  val chatUserRelation = Await.result(populateChatUser, 2.seconds)

  val getUsersChats = for {
    chatsBUser <- chatUserRepo.chatsByUser(chatUserRelation.head.userId)
    usersBChat <- chatUserRepo.usersByChat(chatUserRelation.head.chatId)
  } yield (chatsBUser, usersBChat)

  println(Await.result(getUsersChats, 2.seconds))
}
