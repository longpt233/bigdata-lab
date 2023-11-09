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
```

- attach machine. mặc định sẽ là ssh vào user vagrant, có thể thay đổi bằng cách thêm config config.ssh.username tuy nhiên chưa được quyền ssh từ local sang
```agsl
vagrant ssh machine1
```

- thực thi lệnh 
```agsl
mkdir .ssh && \
echo "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDH21d4cu32i7EIKut9zcgrov5PiZfKATWdkrOUPSb9CbbaNjVKs/pntDMZuycwIDoIwVxmzyhUNx5tuflwRHUwwDaLEYv17AaIYTnZtL+IN4G1BAnFqqNTBVnrEVzjQqh+AVsI/G8fvUz56zM+N3Zonpz3o4C9KcHXHlMDFwu/yxiaH57QmL0+t2Unly3nOhjwqsZR1JFy+d71jYcWzadEgenjDX9AU4xgVGJDFgwBJ+eR3L31QmBd9A/uTylhXikn0oYVce1vFOx7VyTjD3Qxrwo74ABi9tE8ThRKy+LNsc79qYFHJobz7GUM3MWTvxlQzY3kZYtS2v2wFBiQzDGP long@Long-Laptop" > .ssh/authorized_keys && \
chmod 700 .ssh && \
chmod 600 .ssh/authorized_keys && \
$file_path=".ssh/authorized_keys" && \
echo "cat file $file_path after make change" && \
cat $file_path

```

