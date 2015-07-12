package backends.keyvalue

import backends.keyvalue.KeyValueBackend._
import core.aliases._

import scala.concurrent.Future


trait KeyValueBackend {
  def setReference(key: Key, reference: Reference): Future[Any]

  def setEntry(key: BackendKey, entry: Entry): Future[Any]

  def getReference(key: Key): Future[Reference]

  def getEntry(key: BackendKey): Future[Entry]
}

object KeyValueBackend {
  type BackendKey = String

  case class Entry(timestamp: Timestamp, parent: Option[BackendKey], content: Value)

  case class Reference(entryRef: BackendKey, lastModified: Timestamp)
}
