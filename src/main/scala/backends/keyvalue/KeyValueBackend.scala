package backends.keyvalue

import backends.keyvalue.KeyValueBackend._
import core.aliases._
import play.api.libs.json.Json

import scala.concurrent.Future


trait KeyValueBackend {
  def setReference(key: Key, reference: Reference): Future[Unit]

  def setEntry(key: EntryKey, entry: Entry): Future[Unit]

  def getReference(key: Key): Future[Reference]

  def getEntry(key: EntryKey): Future[Entry]
}

object KeyValueBackend {
  type EntryKey = String

  case class Entry(timestamp: Timestamp, parent: Option[EntryKey], content: Value)

  case class Reference(entryRef: EntryKey, lastModified: Timestamp)

  object Entry {
    implicit val entryFormat = Json.format[Entry]
  }

  object Reference {
    implicit val referenceFormat = Json.format[Reference]
  }
}
