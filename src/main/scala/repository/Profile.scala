package repository

import slick.jdbc.JdbcProfile

trait Profile {
  val profile: JdbcProfile
  import profile.api._
  val db: Database
}
