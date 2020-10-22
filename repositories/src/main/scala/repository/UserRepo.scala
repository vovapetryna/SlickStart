package repository

import model.{Chat, User}
import profile.PostgresProfile.api._
import table.UserTable

import scala.concurrent.Future

class UserRepo(db: Database) {
  def init: Future[Unit] = db.run(UserTable.query.schema.create)

  def create(user: User) = db.run(UserTable.returnQuery += user)

  def createAll(users: Seq[User]) = db.run(UserTable.returnQuery ++= users)

  def update(user: User) = db.run(UserTable.byId(user.id).update(user))

  def getById(id: User.Id) =
    db.run(UserTable.byId(id).result)

  def getByLogin(login: String) =
    db.run(UserTable.byLogin(login).result)

  def getAll = db.run(UserTable.query.result)

  def delete(id: User.Id) = db.run(UserTable.byId(id).delete)
}
