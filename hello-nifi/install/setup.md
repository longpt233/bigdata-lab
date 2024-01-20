# add quyền ssh tới các con server


# setup zk

```
- chuyển file lên: 
bash send_file.sh

- trên từng con tạo thư mục và thay myid bằng id tương ứng trong file zoo.conf
mkdir /home/nifi/zookeeper/data && echo 3 | tee -a /home/nifi/zookeeper/data/myid

- bật trên từng con
/home/nifi/zookeeper/apache-zookeeper-3.7.2-bin/bin/zkServer.sh start

- test chạy được
```


