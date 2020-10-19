package repository

trait H2Profile extends Profile {
  override val profile = slick.jdbc.H2Profile
  import profile.api._
  override val db = Database.forConfig("slickH2Db")
}
