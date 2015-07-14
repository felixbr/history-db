package backends.keyvalue

import backends.keyvalue.KeyValueBackend._
import core.aliases._
import core.{HistoryEntry, HistoryStorage}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._

trait KeyValueHistoryStorageMixin extends HistoryStorage {

  def backend: KeyValueBackend

  override def set(key: Key, value: Value): Future[Unit] =
    setToDB(key, value)

  override def get(key: Key): Future[Value] =
    getFromDB(key)

  override def history(key: Key, limit: Int = -1): Future[Seq[HistoryEntry]] = {
    for (
      ref <- backend.getReference(key);
      entries <- fetchHistoryFromDB(ref.entryRef, pending = limit)
    ) yield entries.map(e => HistoryEntry(e.timestamp, e.content))
  }


  private def setToDB(key: Key, value: Value): Future[Unit] = {
    val currentTime = System.currentTimeMillis().toString

    // fetch key of last entry if there was one
    val lastEntryKey: Future[Option[EntryKey]] = backend.getReference(key).map { ref =>
      Some(ref.entryRef)
    } recover {
      case _ => None
    }

    val newID = java.util.UUID.randomUUID().toString


    lastEntryKey
      .flatMap(k => backend.setEntry(newID, Entry(currentTime, k, value)))
      .flatMap(_ => backend.setReference(key, Reference(newID, currentTime)))
  }


  private def getFromDB(key: Key): Future[Value] = {
    for (
      ref <- backend.getReference(key);
      entryFuture <- backend.getEntry(ref.entryRef)
    ) yield entryFuture.content
  }

  private def fetchHistoryFromDB(nextEntryKey: EntryKey, entries: List[Entry] = List.empty, pending: Int = 0): Future[List[Entry]] = {
    backend.getEntry(nextEntryKey).map { entry =>
      entry.parent match {
        case _ if pending == 0 => entries
        case _ if pending == 1 => entries :+ entry
        case None              => entries :+ entry
        case Some(parentKey)   =>
          (entries :+ entry) ++ Await.result(fetchHistoryFromDB(parentKey, pending = pending - 1), 3.seconds)
      }
    } recover {
      case _ => entries
    }
  }

}
