for node in 192.168.56.2 192.168.56.3; do
	echo $node
    ssh trino@$node 'sudo apt install python3'
    scp ./trino-server-424.tar.gz trino@$node:/home/trino
    ssh trino@$node 'tar -xvf trino-server-424.tar.gz'
done