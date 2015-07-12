package backends.keyvalue.inmemory

import backends.keyvalue.KeyValueHistoryImpl
import core.HistoryStorage
import core.aliases.{Value, Key}

import scala.concurrent.Future

object InMemoryHistoryStorage extends HistoryStorage {

  implicit val backend = InMemoryBackend
  val keyValueHistory = new KeyValueHistoryImpl

  override def set(key: Key, value: Value): Future[Any] =
    keyValueHistory.set(key, value)


  override def get(key: Key): Future[Value] =
    keyValueHistory.get(key)
}
