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
nifi@ubuntu-bionic:~/zookeeper/apache-zookeeper-3.7.2-bin$ ./bin/zkServer.sh status
/usr/bin/java
ZooKeeper JMX enabled by default
Using config: /home/nifi/zookeeper/apache-zookeeper-3.7.2-bin/bin/../conf/zoo.cfg
Client port found: 2181. Client address: localhost. Client SSL: false.
Mode: follower

- đọc dữ liệu node
./bin/zkCli.sh -server 192.168.56.2:2181

- kiểm tra port đang chạy
nifi@ubuntu-bionic:~/zookeeper/apache-zookeeper-3.7.2-bin$ netstat -tulpn
(Not all processes could be identified, non-owned process info
 will not be shown, you would have to be root to see it all.)
Active Internet connections (only servers)
Proto Recv-Q Send-Q Local Address           Foreign Address         State       PID/Program name    
tcp        0      0 0.0.0.0:8001            0.0.0.0:*               LISTEN      2774/java           
tcp        0      0 127.0.0.1:36547         0.0.0.0:*               LISTEN      2774/java           
tcp        0      0 0.0.0.0:6342            0.0.0.0:*               LISTEN      2774/java           
tcp        0      0 192.168.56.4:8081       0.0.0.0:*               LISTEN      2774/java           
tcp        0      0 0.0.0.0:8021            0.0.0.0:*               LISTEN      2774/java           
tcp        0      0 127.0.0.53:53           0.0.0.0:*               LISTEN      -                   
tcp        0      0 0.0.0.0:22              0.0.0.0:*               LISTEN      -                   
tcp6       0      0 :::36095                :::*                    LISTEN      26373/java          
tcp6       0      0 127.0.0.1:36453         :::*                    LISTEN      2762/java           
tcp6       0      0 :::2181                 :::*                    LISTEN      26373/java          
tcp6       0      0 192.168.56.4:3888       :::*                    LISTEN      26373/java          
tcp6       0      0 :::8080                 :::*                    LISTEN      26373/java          
tcp6       0      0 :::22                   :::*                    LISTEN      -                   
udp        0      0 127.0.0.53:53           0.0.0.0:*                           -                   
udp        0      0 10.0.2.15:68            0.0.0.0:*                           - 

- xóa dữ liệu node
ls "/nifi/leaders/Cluster Coordinator"
deleteall /nifi
```


# nifi setup

```
bash send_file.sh

./bin/nifi.sh start && tail -f ./logs/nifi-app.log
./bin/nifi.sh stop # chú ý phài stop thì mới ăn config
```


