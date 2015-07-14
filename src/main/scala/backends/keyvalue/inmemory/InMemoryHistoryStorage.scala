package backends.keyvalue.inmemory

import backends.keyvalue.KeyValueHistoryStorageMixin

class InMemoryHistoryStorage extends KeyValueHistoryStorageMixin {
  val backend = new InMemoryBackend
}
