package profile

import slick.jdbc.JdbcProfile

trait PostgresProfile extends JdbcProfile {
  override val api = new PostgresAPI

  class PostgresAPI extends super.API {
    implicit val chatIdMapper: BaseColumnType[model.Chat.Id] =
      MappedColumnType.base[model.Chat.Id, Long](_.value, model.Chat.Id(_))

    implicit val userIdMapper: BaseColumnType[model.User.Id] =
      MappedColumnType.base[model.User.Id, Long](_.value, model.User.Id(_))

    implicit val messageIdMapper: BaseColumnType[model.Message.Id] =
      MappedColumnType
        .base[model.Message.Id, Long](_.value, model.Message.Id(_))
  }
}

object PostgresProfile extends PostgresProfile
