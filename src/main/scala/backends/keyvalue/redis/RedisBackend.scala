package backends.keyvalue.redis

import backends.keyvalue.KeyValueBackend
import backends.keyvalue.KeyValueBackend.{Entry, EntryKey, Reference}
import com.redis.RedisClient
import com.typesafe.config.ConfigFactory
import core.NotFound
import core.aliases.Key
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RedisBackend extends KeyValueBackend {
  val conf = ConfigFactory.load()

  val host = conf.getString("redis.host")
  val port = conf.getInt("redis.port")

  val redis = new RedisClient(host, port)

  override def setReference(key: Key, reference: Reference): Future[Unit] = Future {
    redis.set(key, Json.toJson(reference)) match {
      case true => Future.successful(())
      case false => Future.failed(CouldNotStore(key))
    }
  }

  override def setEntry(key: EntryKey, entry: Entry): Future[Unit] = Future {
    redis.set(key, Json.toJson(entry)) match {
      case true => Future.successful(())
      case false => Future.failed(CouldNotStore(key))
    }
  }

  override def getReference(key: Key): Future[Reference] = Future {
    redis.get(key) match {
      case Some(jsonStr) => Json.parse(jsonStr).as[Reference]
      case None          => throw NotFound(key)
    }
  }

  override def getEntry(key: EntryKey): Future[Entry] = Future {
    redis.get(key) match {
      case Some(jsonStr) => Json.parse(jsonStr).as[Entry]
      case None          => throw NotFound(key)
    }
  }
}

case class CouldNotStore(key: String) extends Exception
