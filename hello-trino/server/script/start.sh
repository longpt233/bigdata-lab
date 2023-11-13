for node in 192.168.56.2 192.168.56.3; do
	  echo $node
    ssh trino@$node "trino-server-424/bin/launcher start"
    ssh trino@$node "tail -n 1000  /var/trino/data/var/log/launcher.log"
done
