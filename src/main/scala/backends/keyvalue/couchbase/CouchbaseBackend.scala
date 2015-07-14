package backends.keyvalue.couchbase

import backends.keyvalue.KeyValueBackend
import backends.keyvalue.KeyValueBackend._
import core.NotFound
import core.aliases._
import org.reactivecouchbase.ReactiveCouchbaseDriver

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CouchbaseBackend(bucketName: String) extends KeyValueBackend {
  val driver = ReactiveCouchbaseDriver()
  val bucket = driver.bucket(bucketName)

  override def setEntry(key: EntryKey, entry: Entry): Future[Unit] = {
    bucket.set(key, entry).mapTo[Unit]
  }

  override def setReference(key: Key, reference: Reference): Future[Unit] = {
    bucket.set(key, reference).mapTo[Unit]
  }

  override def getEntry(key: EntryKey): Future[Entry] = {
    bucket.get[Entry](key).flatMap {
      case Some(entry) => Future.successful(entry)
      case None        => Future.failed(NotFound(key))
    }
  }

  override def getReference(key: Key): Future[Reference] = {
    bucket.get[Reference](key).flatMap {
      case Some(ref) => Future.successful(ref)
      case None      => Future.failed(NotFound(key))
    }
  }
}


