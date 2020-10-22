package repository

import model.{Chat, User}
import profile.PostgresProfile.api._
import table.UserTable

import scala.concurrent.Future

class UserRepo(db: Database) {

  def init: Future[Unit] = db.run(UserTable.query.schema.create)

  def create(userData: User) = db.run(UserTable.idsQuery += userData)

  def createAll(usersData: Seq[User]) = db.run(UserTable.idsQuery ++= usersData)

  def update(user: User) = db.run(UserTable.byId(user.id).update(user))

  def getById(userId: Long) =
    db.run(UserTable.byId(User.Id(userId)).result)

  def getByLogin(login: String) =
    db.run(UserTable.query.filter(_.login === login).result)

  def getAll = db.run(UserTable.query.result)

  def delete(userId: Long) = db.run(UserTable.byId(User.Id(userId)).delete)

  def task(withChats: Boolean)(implicit
      ex: scala.concurrent.ExecutionContext
  ) =
    db.run(UserTable.query.result.flatMap { usersSequence =>
      if (withChats) {
        DBIO.sequence(usersSequence.map { user =>
          val chatIdsForUser = table.ChatUserTable.query
            .filter(_.userId === user.id)
            .map(_.chatId)
            .distinct
          table.ChatTable.query
            .filter(_.id in chatIdsForUser)
            .result
            .map(user -> _)
        })
      } else {
        DBIO.successful(usersSequence.map(_ -> Seq()))
      }
    })

  def task2(implicit ex: scala.concurrent.ExecutionContext): Future[Seq[Chat]] =
    db.run(UserTable.query.result)
      .flatMap(userSequence => db.run(table.ChatTable.query.result))
}
