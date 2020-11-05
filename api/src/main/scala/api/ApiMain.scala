package api

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.javadsl.model.StatusCodes
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import api.akktor.{ChatActor, ChatManager}
import com.typesafe.config.ConfigFactory
import profile.PostgresProfile.api._
import repository.{ChatRepo, ChatUserRepo, MessageRepo, UserRepo}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor}

object ApiMain extends App with marshaling.ModelsMarshaling {
  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")

  val db = Database.forConfig("slickH2Db")

  val userRepo = new UserRepo(db)
  val chatRepo = new ChatRepo(db)
  val messageRepo = new MessageRepo(db)
  val chatUserRepo = new ChatUserRepo(db)

  val init =
    userRepo.init zip chatRepo.init zip messageRepo.init zip chatUserRepo.init
  Await.result(init, 1.seconds)

  implicit val system: ActorSystem[ChatManager.Command] =
    ActorSystem(ChatManager(messageRepo, chatRepo), "REST")
  implicit val executionContext: ExecutionContextExecutor =
    system.executionContext

  var chatIdToChatRef = Map.empty[model.Chat.Id, ActorRef[ChatActor.Command]]

  val route: Route =
    concat(
      get {
        pathPrefix("chat" / LongNumber) { id =>
          implicit val timeout: Timeout = 5.seconds
          val chat = system ? (replyId => ChatManager.GetChat(model.Chat.Id(id), replyId))
          onSuccess(chat) {
            case ChatManager.ResponseChat(chat) => complete(chat)
          }
        }
      },
      post {
        pathPrefix("chat") {
          parameter("name".as[String]) {
            name =>
              implicit val timeout: Timeout = 5.seconds
              val chatIdChatRef = system ? (replyId =>
                ChatManager.CreateChat(model.Chat(model.Chat.Id.empty, name), replyId))
              complete("Chat added to pool")
          }
        }
      }
    )

  val bindingFuture = Http().newServerAt(host, port).bind(route)

  println(s"Server available at http://$host:$port")
}
