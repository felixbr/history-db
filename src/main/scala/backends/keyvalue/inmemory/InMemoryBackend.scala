package backends.keyvalue.inmemory

import java.util.concurrent.ConcurrentHashMap

import backends.keyvalue.KeyValueBackend
import backends.keyvalue.KeyValueBackend._
import core.NotFound
import core.aliases.Key

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object InMemoryBackend extends KeyValueBackend {
  private val entries = new ConcurrentHashMap[Key, Entry]()
  private val references = new ConcurrentHashMap[Key, Reference]()

  override def setEntry(key: Key, entry: Entry): Future[Any] = Future {
    entries.put(key, entry)
  }

  override def getEntry(key: EntryKey): Future[Entry] = Future {
    Option(entries.get(key)) match {
      case Some(value) => value
      case None        => throw NotFound(key)
    }
  }

  override def setReference(key: Key, reference: Reference): Future[Any] = Future {
    references.put(key, reference)
  }

  override def getReference(key: Key): Future[Reference] = Future {
    Option(references.get(key)) match {
      case Some(value) => value
      case None        => throw NotFound(key)
    }
  }
}
