import com.typesafe.scalalogging._
import model.{Chat, User}
import org.slf4j.LoggerFactory
import profile.PostgresProfile.api._
import repository.{ChatRepo, MessageRepo, UserRepo}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext}

object Main extends App {
  private val logger = Logger(LoggerFactory.getLogger(this.getClass))

  val db = Database.forConfig("slickH2Db")

  val userRepo = new UserRepo(db)
  val chatRepo = new ChatRepo(db)
  val MessageRepo = new MessageRepo(db)

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

  val testUsers = Seq(
    User(User.Id.empty, "vova"),
    User(User.Id.empty, "Petya"),
    User(User.Id.empty, "Vasya")
  )
  val testChats =
    Seq(Chat(Chat.Id.empty, "Global"), Chat(Chat.Id.empty, "News"))

  val populateDb = for {
    _ <- userRepo.init
    _ <- chatRepo.init
    insertedUsersIds <- userRepo.createAll(testUsers)
    insertedChatsIds <- chatRepo.createAll(testChats)
  } yield (insertedUsersIds, insertedChatsIds)

  println(Await.result(populateDb, 2.seconds))
}
