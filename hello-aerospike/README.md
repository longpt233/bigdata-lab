
```
namespace ssd_hadoop {
        replication-factor 2
        stop-writes-pct 90
        memory-size 30G
        high-water-memory-pct 80
        default-ttl 0 # 30 days, use 0 to never expire/evict.

        storage-engine device {
                device /dev/sdb1    # raw device. Maximum size is 2 TiB
                write-block-size 128K   # adjust block size to make it efficient for SSDs.
       }
}
namespace hdd_hadoop {
        replication-factor 2
        stop-writes-pct 90
        memory-size 10G
        default-ttl 0 # 30 days, use 0 to never expire/evict.

        storage-engine device {
                file /storage/aerospike/data/data.dat
                filesize 1T
                data-in-memory false # Store data in memory in addition to file.
       }
}
namespace mem_hadoop {
       memory-size 25G   # Maximum memory allocation for data and primary and
                          # secondary indexes.
      storage-engine memory # Configure the storage-engine to not use persistence.
}
```