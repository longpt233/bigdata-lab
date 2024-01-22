#for node in 192.168.56.2 192.168.56.3 192.168.56.4; do
#	echo $node
#    scp -r ./apache-zookeeper-3.7.2-bin nifi@$node:/home/nifi/zookeeper/
#done

for node in 192.168.56.2 192.168.56.3 192.168.56.4; do
#for node in 192.168.56.4; do
	echo $node
    scp -r ./nifi-1.18.0-bin nifi@$node:/home/nifi/nifi/
done

# chú ý phải để mấy cái host này là địa chỉ vì nó sẽ dùng để vote chứ k phải để chơi chơi đâu hic (không để 0.0.0.0)
ssh nifi@192.168.56.2 "sed -i  's/nifi.cluster.node.address=0.0.0.0/nifi.cluster.node.address=192.168.56.2/g' /home/nifi/nifi/nifi-1.18.0-bin/nifi-1.18.0/conf/nifi.properties"
ssh nifi@192.168.56.3 "sed -i  's/nifi.cluster.node.address=0.0.0.0/nifi.cluster.node.address=192.168.56.3/g' /home/nifi/nifi/nifi-1.18.0-bin/nifi-1.18.0/conf/nifi.properties"
ssh nifi@192.168.56.4 "sed -i  's/nifi.cluster.node.address=0.0.0.0/nifi.cluster.node.address=192.168.56.4/g' /home/nifi/nifi/nifi-1.18.0-bin/nifi-1.18.0/conf/nifi.properties"

ssh nifi@192.168.56.2 "sed -i  's/nifi.web.http.host=0.0.0.0/nifi.web.http.host=192.168.56.2/g' /home/nifi/nifi/nifi-1.18.0-bin/nifi-1.18.0/conf/nifi.properties"
ssh nifi@192.168.56.3 "sed -i  's/nifi.web.http.host=0.0.0.0/nifi.web.http.host=192.168.56.3/g' /home/nifi/nifi/nifi-1.18.0-bin/nifi-1.18.0/conf/nifi.properties"
ssh nifi@192.168.56.4 "sed -i  's/nifi.web.http.host=0.0.0.0/nifi.web.http.host=192.168.56.4/g' /home/nifi/nifi/nifi-1.18.0-bin/nifi-1.18.0/conf/nifi.properties"
