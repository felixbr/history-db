package backends.keyvalue.couchbase

import backends.keyvalue.KeyValueBackend
import backends.keyvalue.KeyValueBackend._
import core.aliases._
import org.reactivecouchbase.ReactiveCouchbaseDriver
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CouchbaseBackend(bucketName: String) extends KeyValueBackend {
  val driver = ReactiveCouchbaseDriver()
  val bucket = driver.bucket(bucketName)

  implicit val entryFormat = Json.format[Entry]
  implicit val referenceFormat = Json.format[Reference]

  override def setEntry(key: BackendKey, entry: Entry): Future[Any] = {
    bucket.set(key, entry)
  }

  override def setReference(key: Key, reference: Reference): Future[Any] = {
    bucket.set(key, reference)
  }

  override def getEntry(key: BackendKey): Future[Entry] = {
    bucket.get[Entry](key).flatMap {
      case Some(entry) => Future.successful(entry)
      case None        => Future.failed(new RuntimeException("Entry not found!"))
    }
  }

  override def getReference(key: Key): Future[Reference] = {
    bucket.get[Reference](key).flatMap {
      case Some(ref) => Future.successful(ref)
      case None      => Future.failed(new RuntimeException("Ref not found!"))
    }
  }
}


