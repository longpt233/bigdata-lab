# cai zoo 

# cai nifi 

# cac config van chu y 

```
    nifi.flowfile.repository.directory=${path-to-flowfile-repo}
    nifi.content.repository.directory.default=${path-to-content-repo}
    nifi.content.repository.archive.max.retention.period=1 day
    nifi.content.repository.archive.max.usage.percentage=50%
    nifi.provenance.repository.directory.default=${path-to-provenance-repo}
    nifi.provenance.repository.max.storage.time=1 day
    nifi.provenance.repository.max.storage.size=10 GB
    nifi.remote.input.host={$hostname}
    nifi.remote.input.secure=true
    nifi.remote.input.socket.port=9999
    nifi.web.https.host={$hostname}
    nifi.web.https.port=8780
    nifi.security.keystore=${path-to-keystore}
    nifi.security.keystoreType=jks
    nifi.security.keystorePasswd=${keystore-password}
    nifi.security.keyPasswd=${key-password}
    nifi.security.truststore=${path-to-trusstore}
    nifi.security.truststoreType=jks
    nifi.security.truststorePasswd=${truststore-password}
    nifi.cluster.is.node=true
    nifi.cluster.node.address={$hostname}
    nifi.cluster.node.protocol.port=8001
    nifi.cluster.load.balance.host={$hostname}
    nifi.zookeeper.connect.string={$hostname1:port},{$hostname2:port},{$hostname3:port}
    nifi.zookeeper.root.node=/nifi

```

# cai 1 node

```
wget https://dlcdn.apache.org/nifi/1.25.0/nifi-1.25.0-bin.zip
unzip nifi-1.25.0-bin.zip 
cd nifi-1.25.0-bin/nifi-1.25.0

# cần chú ý các file sau: nifi.properties, state-management.xml, zookeeper.properties (khi cài embeded)

sed -i 's/nifi.zookeeper.connect.string=$/nifi.zookeeper.connect.string=localhost:2181/g' ./conf/nifi.properties
sed -i 's/server.1=$/server.1=localhost:2888:3888;2181/g' ./conf/zookeeper.properties
sed -i 's/nifi.state.management.embedded.zookeeper.start=false$/nifi.state.management.embedded.zookeeper.start=true/g' ./conf/nifi.properties
sed  -i 's/<property name="Connect String"><\/property>$/<property name="Connect String">localhost:2181<\/property>/g' ./conf/state-management.xml


mkdir ./state
mkdir ./state/zookeeper
echo 1 > ./state/zookeeper/myid

./bin/nifi.sh set-single-user-credentials admin admin12345678
./bin/nifi.sh start && tail -f ./logs/nifi-app.log

```

# chú ý:

- không nên dùng embeded zoo của nó vì bản chất con nifi này chạy khá nặng -> tràn ram, chết node là có thế. nếu cài con zoo trên đây thì khả năng cao 1 node chết sẽ kéo theo con zoo chết -> việc bầu lại trên 2 node. 

- khi deploy product thì nên dùng external zoo: chẳng hạn node nó down thì ko ảnh hưởng đến việc leader election của cụm
