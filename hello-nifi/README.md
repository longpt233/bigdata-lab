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