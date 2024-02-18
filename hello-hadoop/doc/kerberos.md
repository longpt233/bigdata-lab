0. Tổng quan
   Hadoop được cài đặt bảo mật bằng kerberos. Mỗi user để truy cập vào 1 service sẽ gọi đến kdc (key distributed center) để nhân 1 ticket truy cập vào server. Tên user hoặc service gọi là principal, password được lưu trong 1 file đã mã hóa gọi là keytab.  Mặc định ticket sẽ tồn tại 24h, còn keytab tồn tại vĩnh viễn.
   Đọc thêm ở link:
   https://access.redhat.com/documentation/en-US/Red_Hat_Enterprise_Linux/6/html/Managing_Smart_Cards/Using_Kerberos.html

Khái niệm cơ bản:
- keytab: viết tắt của keytable chứa password của 1 hay nhiều principal đã được mã hóa
- principal: tương tự như username hoặc tên service. ví dụ quangnd/platform@HADOOP.SECURE thì quangnd là tên , platform là domain và HADOOP.SECURE là realm


1. Cài đặt môi trường
   1.1 Cấu hình kerberos-client

   Trên centos chạy lệnh:
   yum install krb5-workstation krb5-libs krb5-auth-dialog
   Trên ubuntu chạy lệnh:
   apt-get install krb5-user
   Rồi gõ enter liên tục

   Trên mac os x:
   Cài đặt theo link http://web.mit.edu/kerberos/dist/
   Không rõ lý do nhưng khi config kdc của mac os x thay vì 192.168.23.200 phải viết tcp/192.168.23.200:88

Sau khi cài 2 gói, sửa file /etc/krb5.conf giống như trên server
trong file này chứa host kdc và kadmin của realm tương ứng

Ví dụ:

[realms]
HADOOP.SECURE = {
kdc = 192.168.23.200
kdc = 192.168.23.85
admin_server = 192.168.23.200
}

thì 2 kdc ứng với realm HADOOP.SECURE là 192.168.23.85,192.168.23.200



Có thể kiểm tra = lệnh kinit -k -t file_keytab tên_principal
Do cơ chể xin và expire của ticket cho nên lênh này sẽ tạo 1 ticket trong 24h, có thể dùng klist để kiểm tra, kết quả ra như sau:

Ticket cache: FILE:/tmp/krb5cc_1000
Default principal: quangnd/platform@HADOOP.SECURE

Valid starting       Expires              Service principal
03/08/2017 09:23:23  04/08/2017 09:23:19  krbtgt/HADOOP.SECURE@HADOOP.SECURE
renew until 03/08/2017 09:23:23






1.2 Cài đặt Jce

B1: Download jce về và giải nén
B2: copy các file .jar vào đường dẫn $JAVA_HOME/jre/lib/security
B3:Kiểm tra = lệnh:

jrunscript -e 'exit (javax.crypto.Cipher.getMaxAllowedKeyLength("RC5") >= 256);'; echo $?

Nếu kết quả =1 thì cài đặt thành công

1.3 Cài đặt trên window:
https://community.hortonworks.com/articles/28537/user-authentication-from-windows-workstation-to-hd.html
Lưu ý:
- Cài jce và đặt JAVA_HOME vào environment variable.
- KRB5CCNAME: trỏ đến file cache mà user có quyền ghi
- Sửa file hosts đến ip trong file config và ip của hadoop namenode
- Khởi động lại máy sau khi cài environment variable.

Nhập username password trong MIT là test/test@HADOOP.SECURE và test để đọc file trên hdfs

2. Chạy java và mapreduce trên secure hadoop


Đối với các job thông thường khi khởi tạo connection

```
Configuration conf=….
UserGroupInformation.setConfiguration(conf);
UserGroupInformation.loginUserFromKeytab(
"quangnd/platform@HADOOP.SECURE",
"/home/quangnd/keytab-user/quangnd.keytab");
```


Tuy nhiên theo cơ chế kerberos đối với các job chạy thời gian dài (>24h) sẽ cần khởi tạo lại ticket

```
if (UserGroupInformation.isLoginKeytabBased()) {
UserGroupInformation.getLoginUser().reloginFromKeytab();
} else if (UserGroupInformation.isLoginTicketBased()) {
UserGroupInformation.getLoginUser().reloginFromTicketCache();
}
```

Link đọc thêm:
https://stackoverflow.com/questions/34616676/should-i-call-ugi-checktgtandreloginfromkeytab-before-every-action-on-hadoop
3. Chạy spark vào cụm secure hadoop

3.1 Đọc dữ liệu từ chính cụm hadoop chạy yarn

- trong spark-env.sh sửa biến HADOOP_CONF_DIR đến thư mục config hadoop
- khi chạy job spark thêm vào  --principal và –keytab đến đúng tham số tương ứng.
  3.2 Chạy spark  từ secure ghi ra 1 cụm insecure

- trong core-site.xml thêm vào property ipc.client.fallback-to-simple-auth-allowed giá trị true

3.3 Đọc ghi giữa 2 cụm secure

- Do cơ chế xin ticket nên 2 server cần chung 1 kdc server
- Khi khởi tạo sparkConf trong code java đọc đến 1 cụm secure khác thêm tham số “spark.yarn.access.namenodes” đến “hdfs://activenamenode:port”, có thể thêm nhiều namenode cách nhau bởi dấu “,”

3.4 từ cụm insecure đọc cụm secure

- Không có cách đọc từ 1 cụm insecure vào cụm secure khi chạy mode yarn
- Có thể chạy mode local và spark-standalone  chỉ vào  conf của cụm hadoop secure và ghi lên cụm insecure

4. Chạy storm vào cụm secure hadoop
5. Cài đặt cho trình duyệt web để xem cây thư mục hadoop

Cài đặt gói kerberos và cấu hình như hướng dẫn ở trên
Chạy lệnh kinit để khởi tạo session.
Kiểm tra session bằng lệnh klist.

5.1 Vào bằng firefox

vào link about:config
search từ negotiate
sửa network.negotiate-auth.delegation-uris đến các domain tương ứng
sửa network.negotiate-auth.trusted-uris đến các domain tương ứng
khởi động lại trình duyệt

5.2 Vào bằng googlechrome
- Trên ubuntu: Sửa file /etc/opt/chrome/policies/managed/spnego.json

{
“AuthServerWhitelist”:”tên server”,
“AuthNegotiateDelegateWhitelist”:”tên server”,
}


-Trên mac os x:

Gõ 2 lệnh:
defaults write com.google.Chrome AuthNegotiateDelegateWhitelist "hadoop2397,hadoop23200"
defaults write com.google.Chrome AuthServerWhitelist "hadoop2397,hadoop23200"


https://www.jeffgeerling.com/blogs/jeff-geerling/kerberos-authentication-mac-os


6. Một số lỗi thường gặp:

Caused by: KrbException: Clock skew too great (37) - PROCESS_TGS

Lỗi do không đồng bộ giữa server kdc và server client → update đồng hồ để chạy được
Login failure for quangnd/platform@HADOOP.SECURE from keytab …

kiểm tra lại keytab file



