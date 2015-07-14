package core

import scala.concurrent.Future
import aliases._

object aliases {
  type Key = String
  type Value = String  // for now
  type Timestamp = String
}

case class HistoryEntry(timestamp: Timestamp, value: Value)

trait HistoryStorage {
  def set(key: Key, value: Value): Future[Unit]

  def get(key: Key): Future[Value]

  def history(key: Key, limit: Int = -1): Future[Seq[HistoryEntry]]
}

object HistoryDB {
  def set(key: Key, value: Value)(implicit storage: HistoryStorage): Future[Unit] =
    storage.set(key, value)

  def get(key: Key)(implicit storage: HistoryStorage): Future[Value] =
    storage.get(key)

  def showHistory(key: Key, limit: Int = -1)
      (implicit storage: HistoryStorage): Future[Seq[HistoryEntry]] = {

    storage.history(key, limit = limit)
  }
}

case class NotFound(key: Key) extends Exception
