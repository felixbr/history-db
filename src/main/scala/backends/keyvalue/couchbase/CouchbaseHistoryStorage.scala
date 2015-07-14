package backends.keyvalue.couchbase

import backends.keyvalue.KeyValueHistoryStorageMixin

class CouchbaseHistoryStorage(bucketName: String = "default") extends KeyValueHistoryStorageMixin {
  val backend = new CouchbaseBackend(bucketName)
}
