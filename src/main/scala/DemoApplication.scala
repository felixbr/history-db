import backends.keyvalue.couchbase.CouchbaseHistoryStorage
import backends.keyvalue.inmemory.InMemoryHistoryStorage
import backends.keyvalue.redis.RedisHistoryStorage
import core._

import scala.concurrent.Await
import scala.concurrent.duration._

object DemoApplication extends App {

  implicit val storage = new InMemoryHistoryStorage
  // implicit val storage = new CouchbaseHistoryStorage()
  // implicit val storage = new RedisHistoryStorage

  val key = "MyKey"
  val value1 = "25"
  val value2 = "30"

  Await.ready(HistoryDB.set(key, value1), 3.seconds)
  println(Await.result(HistoryDB.get(key), 3.seconds))
  Await.ready(HistoryDB.set(key, value2), 3.seconds)
  println(Await.result(HistoryDB.get(key), 3.seconds))

}
