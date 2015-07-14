package backends.keyvalue.redis

import backends.keyvalue.KeyValueHistoryStorageMixin

class RedisHistoryStorage extends KeyValueHistoryStorageMixin {
  val backend = new RedisBackend
}
