import com.typesafe.scalalogging._
import model.User
import org.slf4j.LoggerFactory
import repository.UserRepo

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

object Main extends App {
  private val logger = Logger(LoggerFactory.getLogger(this.getClass))

  val testUsers = Seq(User("vova"), User("Petya"), User("Vasya"))

  val addUsers = UserRepo.createM(testUsers)
  val usersTest = for {
    _ <- UserRepo.schemaInit
    usersAdded <- UserRepo.createM(testUsers)
    vovaId <- UserRepo.getByLogin("vova")
  } yield vovaId

  println(Await.result(usersTest, 2.seconds))
}
