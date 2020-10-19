package model

import slick.lifted.MappedTo

case class UserPk(value: Long) extends AnyVal with MappedTo[Long]

case class User (login: String,
                id: UserPk = UserPk(0L))
