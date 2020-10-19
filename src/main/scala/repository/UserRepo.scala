package repository

import model.{User, UserPk}
import table.UserTable

trait UserRepo extends UserTable { self: Profile =>

  import profile.api._

  def schemaInit = db.run(users.schema.create)

  def create(userData: User) = db.run(insertUsers += userData)

  def createM(usersData: Seq[User]) = db.run(users ++= usersData)

  def update(user: User) = db.run(users.filter(_.id === user.id).update(user))

  def getById(userId: Long) =
    db.run(users.filter(_.id === UserPk(userId)).result)

  def getByLogin(login: String) =
    db.run(users.filter(_.login === login).result)

  def getAll = db.run(users.result)

  def delete(userId: UserPk) = db.run(users.filter(_.id === userId).delete)
}

object UserRepo extends UserRepo with H2Profile
