```commandline
sudo apt update && sudo apt upgrade -y
sudo apt install rabbitmq-server -y
sudo systemctl enable rabbitmq-server
sudo systemctl start rabbitmq-server
sudo systemctl status rabbitmq-server
sudo rabbitmq-plugins enable rabbitmq_management
sudo rabbitmqctl add_user airflow airflow
sudo rabbitmqctl set_user_tags airflow administrator
sudo rabbitmqctl list_permissions -p / 
sudo rabbitmqctl set_permissions -p "/" "airflow" ".*" ".*" ".*"
```