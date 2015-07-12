package core

import scala.concurrent.Future
import aliases._

object aliases {
  type Key = String
  type Value = String  // for now
  type Timestamp = String
}

trait HistoryStorage {
  def set(key: Key, value: Value): Future[Any]

  def get(key: Key): Future[Value]
}

object HistoryDB {
  def set(key: Key, value: Value)(implicit storage: HistoryStorage): Future[Any] =
    storage.set(key, value)

  def get(key: Key)(implicit storage: HistoryStorage): Future[Value] =
    storage.get(key)
}

case class NotFound(key: Key) extends Exception
