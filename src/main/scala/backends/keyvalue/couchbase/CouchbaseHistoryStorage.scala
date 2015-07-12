package backends.keyvalue.couchbase

import backends.keyvalue.KeyValueHistoryImpl
import core.HistoryStorage
import core.aliases._

import scala.concurrent.Future

class CouchbaseHistoryStorage(bucketName: String = "default") extends HistoryStorage {

  implicit val backend = new CouchbaseBackend(bucketName)
  val keyValueHistory = new KeyValueHistoryImpl

  override def set(key: Key, value: Value): Future[Any] =
    keyValueHistory.set(key, value)

  override def get(key: Key): Future[Value] =
    keyValueHistory.get(key)
}
