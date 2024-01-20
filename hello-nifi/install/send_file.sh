for node in 192.168.56.2 192.168.56.3 192.168.56.4; do
	echo $node
    scp -r ./apache-zookeeper-3.7.2-bin nifi@$node:/home/nifi/zookeeper/
done