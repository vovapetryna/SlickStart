package api.marshaling

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

trait ModelsMarshaling extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userIdFormat: RootJsonFormat[model.User.Id] = jsonFormat1(model.User.Id.apply)
  implicit val userFormat: RootJsonFormat[model.User] = jsonFormat2(model.User.apply)

  implicit val chatIdFormat: RootJsonFormat[model.Chat.Id] = jsonFormat1(model.Chat.Id.apply)
  implicit val chatFormat: RootJsonFormat[model.Chat] = jsonFormat2(model.Chat.apply)
}
