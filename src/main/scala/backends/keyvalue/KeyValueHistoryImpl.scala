package backends.keyvalue

import backends.keyvalue.KeyValueBackend._
import core.HistoryStorage
import core.aliases._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class KeyValueHistoryImpl(implicit backend: KeyValueBackend) extends HistoryStorage {

  override def set(key: Key, value: Value): Future[Any] =
    setToDB(key, value)

  override def get(key: Key): Future[Value] =
    getFromDB(key)

  private def setToDB(key: Key, value: Value): Future[Any] = {
    val currentTime = System.currentTimeMillis().toString

    // fetch key of last entry if there was one
    val lastEntryKey: Future[Option[BackendKey]] = backend.getReference(key).map { ref =>
      Some(ref.entryRef)
    } recover {
      case _ => None
    }

    val newID = java.util.UUID.randomUUID().toString


    lastEntryKey
      .map(k => backend.setEntry(newID, Entry(currentTime, k, value)))
      .map(_ => backend.setReference(key, Reference(newID, currentTime)))
  }


  private def getFromDB(key: Key): Future[Value] = {
    for (
      ref <- backend.getReference(key);
      entryFuture <- backend.getEntry(ref.entryRef)
    ) yield entryFuture.content
  }

}
