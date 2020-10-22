package repository

import model.{Chat, ChatUser, User}
import profile.PostgresProfile.api._
import table.ChatUserTable

class ChatUserRepo(db: Database) {
  def init = db.run(ChatUserTable.query.schema.create)

  def create(chatUser: ChatUser) = db.run(ChatUserTable.returnQuery += chatUser)

  def createAll(chatsUsers: Seq[ChatUser]) =
    db.run(ChatUserTable.returnQuery ++= chatsUsers)

  def chatsByUser(id: User.Id) =
    db.run(
      (for {
        chatUser <- ChatUserTable.byUserId(id)
        chat <- chatUser.chat
      } yield chat).result
    )

  def usersByChat(id: Chat.Id) =
    db.run(
      (for {
        chatUser <- ChatUserTable.byChatId(id)
        user <- chatUser.user
      } yield user).result
    )

  def getAll = db.run(ChatUserTable.query.result)

  def delete(chatId: Chat.Id, userId: User.Id) =
    db.run(ChatUserTable.buChatAndUser(chatId, userId).delete)
}
