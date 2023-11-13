# Trino intro

https://engineering.admicro.vn/gioi-thieu-trino-presto-ap-dung-cho-bai-toan-lam-report-tai-team-platform/

# Trino install

- preinstall
```
sudo apt-get install vagrant
vagrant version
```

- run vm 
```
cd hello-trino/server
vagrant init
vagrant up

vagrant destroy
```

- attach machine. mặc định sẽ là ssh vào user vagrant, có thể thay đổi bằng cách thêm config config.ssh.username tuy nhiên chưa được quyền ssh từ local sang
```
vagrant ssh machine1
```

- thực thi lệnh để add quyền ssh vào user trino
```
sudo su - trino -c $'\
whoami && \
mkdir .ssh && \
echo "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDH21d4cu32i7EIKut9zcgrov5PiZfKATWdkrOUPSb9CbbaNjVKs/pntDMZuycwIDoIwVxmzyhUNx5tuflwRHUwwDaLEYv17AaIYTnZtL+IN4G1BAnFqqNTBVnrEVzjQqh+AVsI/G8fvUz56zM+N3Zonpz3o4C9KcHXHlMDFwu/yxiaH57QmL0+t2Unly3nOhjwqsZR1JFy+d71jYcWzadEgenjDX9AU4xgVGJDFgwBJ+eR3L31QmBd9A/uTylhXikn0oYVce1vFOx7VyTjD3Qxrwo74ABi9tE8ThRKy+LNsc79qYFHJobz7GUM3MWTvxlQzY3kZYtS2v2wFBiQzDGP long@Long-Laptop" > .ssh/authorized_keys && \
chmod 700 .ssh && \
chmod 600 .ssh/authorized_keys && \
file_path=".ssh/authorized_keys" && \
echo "cat file $file_path after make change" && \
cat $file_path '
```

- lệnh trên là chuyển user chạy lệnh, lệnh dưới là ssh user chạy lệnh 
```
vagrant ssh machine2 -c $'whoami'
```

- check ssh lên 2 server
```
ssh trino@192.168.56.2
ssh trino@192.168.56.3
```

- tải trino và cài đặt
```
wget https://repo1.maven.org/maven2/io/trino/trino-server/424/trino-server-424.tar.gz

for node in 192.168.56.2 192.168.56.3; do
	echo $node
    ssh trino@$node 'sudo apt install python3'
    scp ./trino-server-424.tar.gz trino@$node:/home/trino
    ssh trino@$node 'tar -xvf trino-server-424.tar.gz'
    ssh trino@$node 'sudo apt update && sudo apt install openjdk-17-jdk openjdk-17-jre -y'
    ssh trino@$node  'sudo mkdir /var/trino && sudo chown trino:trino /var/trino'
done
```

- cấu hình coordinator
```
ssh trino@192.168.56.2 "mkdir trino-server-424/etc"

echo ' 
coordinator=true 
node-scheduler.include-coordinator=false
http-server.http.port=8080
discovery.uri=http://192.168.56.2:8080
' | ssh trino@192.168.56.2 "cat > trino-server-424/etc/config.properties"  

ssh trino@192.168.56.2 "cat trino-server-424/etc/config.properties"

echo ' 
node.environment=production
node.id=c1c2c357-a192-4b69-8a84-e86509c7d0d6
node.data-dir=/var/trino/data
' | ssh trino@192.168.56.2 "cat > trino-server-424/etc/node.properties" 

```

- cấu hình worker

```
ssh trino@192.168.56.3 "mkdir trino-server-424/etc"

echo ' 
coordinator=false
http-server.http.port=8080
discovery.uri=http://192.168.56.2:8080
' | ssh trino@192.168.56.3 "cat > trino-server-424/etc/config.properties"  

ssh trino@192.168.56.3 "cat trino-server-424/etc/config.properties"

echo ' 
node.environment=production
node.id=da1ee33a-3c84-46e7-9921-b4492e9ebf24
node.data-dir=/var/trino/data
' | ssh trino@192.168.56.3 "cat > trino-server-424/etc/node.properties" 
```

- cấu hình chung
```
for node in 192.168.56.2 192.168.56.3; do
	echo $node
    echo ' 
-server
-Xmx1G
-XX:InitialRAMPercentage=80
-XX:MaxRAMPercentage=80
-XX:G1HeapRegionSize=32M
-XX:+ExplicitGCInvokesConcurrent
-XX:+ExitOnOutOfMemoryError
-XX:+HeapDumpOnOutOfMemoryError
-XX:-OmitStackTraceInFastThrow
-XX:ReservedCodeCacheSize=512M
-XX:PerMethodRecompilationCutoff=10000
-XX:PerBytecodeRecompilationCutoff=10000
-Djdk.attach.allowAttachSelf=true
-Djdk.nio.maxCachedBufferSize=2000000
-Dfile.encoding=UTF-8
# Reduce starvation of threads by GClocker, recommend to set about the number of cpu cores (JDK-8192647)
-XX:+UnlockDiagnosticVMOptions
-XX:GCLockerRetryAllocationCount=32
' | ssh trino@$node "cat > trino-server-424/etc/jvm.config && " 
    ssh trino@$node "cat trino-server-424/etc/jvm.config"
done
```

- chạy corrdinator

```
ssh trino@192.168.56.2 "trino-server-424/bin/launcher start"
ssh trino@192.168.56.3 "trino-server-424/bin/launcher start"

ssh trino@192.168.56.3 "tail -n 1000  /var/trino/data/var/log/launcher.log"
```

- view ui: http://192.168.56.2:8080/ui/

- chạy superset https://superset.apache.org/docs/installation/installing-superset-from-scratch/
```
   19  sudo apt-get install build-essential libssl-dev libffi-dev python3-dev python3-pip libsasl2-dev libldap2-dev default-libmysqlclient-dev
   20  pip install virtualenv
   21  sudo apt install python-pip
   22  pip install virtualenv
   23  python3 -m venv venv
   24  sudo apt-get install python3-venv  # lỗi chưa có venv module
   25  python3 -m venv venv
   26  . venv/bin/activate
   27  pip install apache-superset
   29  pip install --upgrade pip   # lỗi pip ver thấp
   32  superset db upgrade
   33  pip install sqlalchemy==1.3.24 # ImportError: cannot import name '_ColumnEntity'
   34  superset db upgrade
   37  pip install dataclasses # ModuleNotFoundError: No module named 'dataclasses'
   38  superset db upgrade
   39  pip uninstall pyopenssl
   40  pip install pyopenssl==22.1.0   # ModuleNotFoundError: No module named 'cryptography.hazmat.backends.openssl.x509'
   41  superset db upgrade   # no err -> oke
   43  export FLASK_APP=superset
   44  superset fab create-admin
   45  superset load_examples
   46  superset init
   51  superset run -p 8088 -h 0.0.0.0 --with-threads --reload --debugger

```