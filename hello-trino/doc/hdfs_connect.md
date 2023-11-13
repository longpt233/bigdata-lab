# HDFS connect 

# cài HMS
- cài hive metastore (k cần cài hive). từ bước này cài trên con server nội bộ (để có đủ krb.conf , host,...)

```
bash ~/gw-send /home/long/Documents/vccorp/hive-standalone-metastore-3.0.0-bin.tar.gz longpt@10.5.0.242:/home/longpt/trino/
bash ~/gw-send /home/long/Documents/vccorp/trino-server-424.tar.gz longpt@10.5.0.242:/home/longpt/trino/
bash ~/gw-send /home/long/Documents/vccorp/hadoop-3.2.0.tar.gz longpt@10.5.0.242:/home/longpt/trino/
```

- cấu hình file core-site, hdfs-site của hadoop

```

```
- cấu hình metastore-site. (mẫu tại hello-trino/config/metastore-site.xml)

```
nano apache-hive-metastore-3.0.0-bin/conf/metastore-site.xml
```
- trỏ HADOOP_HOME của hms  
```
nano ./apache-hive-metastore-3.0.0-bin/bin/metastore-config.sh
# thêm path
export HADOOP_HOME="/home/trino/hadoop-3.2.0"
```
- init db  (lần đầu)
```
./apache-hive-metastore-3.0.0-bin/bin/schematool -initSchema -dbType mysql
```

- khời chạy 
```
./apache-hive-metastore-3.0.0-bin/bin/start-metastore
```


# cấu hình catalog

- tạo thư mục etc/catalog 
```
vi ./trino-server-424/etc/catalog/hadoop202.properties 
```

# add conection 

- trên trino, tạo db connection > trino, điền url
```
trino://ADMIN@10.3.106.254:8081/hadoop202 (hadoop202 trùng với tên file .properties ở bên trên)
```
- tab advanced: add các quyền cho SQL lab

# add schema

```
CREATE TABLE hadoop202.default.admatic_gdsp (
  bannerId int,
  zoneId int,
  campaignId int,
  ip bigint,
  guid bigint,
  domain varchar,
  url varchar,
  ssp_zone_id int,
  time_group row(time_create bigint,cookie_create bigint), 
  browser_group row(browser_code int, browser_ver varchar ), 
  os_group row(os_code int,os_ver varchar ),
  date_time varchar
) WITH (
  partitioned_by = ARRAY['date_time'],
  format = 'parquet',
  external_location = 'hdfs://172.18.5.86:9000/data/Parquet/AdmaticGDspLog/'
);

CALL hadoop202.system.register_partition('default', 'admatic_gdsp', array['date_time'],array['2023_10_15'], 'hdfs://172.18.5.86:9000/data/Parquet/AdmaticGDspLog/2023_10_15')
```

- kerberos
```
- tại sao cần principal -> vì trong keytab (key table) có thể xác thực cho nhiều principal 
- tại sao nn, dn cần xác thực thực kerberos -> để nó đăng kí với keberos để sau đó có thể nói chuyện với nhau
```

- lỗi 
```
Caused by: java.io.IOException: List files for hdfs://hadoopbigdata/data/platform/ssp_logs/2021-11-25/192.168.23.115 failed: User: hive/hive@HADOOP.SECURE is not allowed to impersonate ADMIN

do cấu hình catalog #hive.hdfs.impersonation.enabled=true 
```