import scala.concurrent._
import scala.concurrent.duration._
import scala.util._
import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory
import repository.H2DatabaseLayer

import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App{
  private val logger = Logger(LoggerFactory.getLogger(this.getClass))

  val db = H2DatabaseLayer.db
}
