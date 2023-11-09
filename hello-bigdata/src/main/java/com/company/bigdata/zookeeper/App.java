package com.company.bigdata.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class App implements Watcher {


    private ZooKeeper zooKeeper;

    public void connect() throws Exception {
        String zkAddress = "192.168.23.37:2181"; // Địa chỉ ZooKeeper server
        int sessionTimeout = 5000; // Thời gian timeout cho kết nối ZooKeeper

        zooKeeper = new ZooKeeper(zkAddress, sessionTimeout, this);
    }

    public void readData(String path) throws Exception {
        Stat stat = zooKeeper.exists(path, false);

        if (stat != null) {
            byte[] data = zooKeeper.getData(path, false, stat);
//            String dataString = new String(data, StandardCharsets.ISO_8859_1);


            String base64EncodedData = new String(data);
            byte[] decodedData = Base64.getDecoder().decode(base64EncodedData);
            String dataString = new String(decodedData, StandardCharsets.UTF_8);

            System.out.println("Data at " + path + ": " + dataString);
        } else {
            System.out.println("Node does not exist: " + path);
        }
    }

    public void readNode(String path) throws Exception {
        byte[] data = zooKeeper.getData(path, false, null);
        String dataString = new String(data);

        System.out.println("Node: " + path);
        System.out.println("Data: " + dataString);
        System.out.println("-----------------------------");

        // Đọc nội dung của các node con
        List<String> children = zooKeeper.getChildren(path, false);
        for (String child : children) {
            String childPath = path + "/" + child;
            readNode(childPath);
        }
    }


    @Override
    public void process(WatchedEvent event) {
        // Xử lý sự kiện từ ZooKeeper (nếu có)
    }

    public void close() throws Exception {
        zooKeeper.close();
    }

    public static void main(String[] args) {
        try {
            App reader = new App();
            reader.connect();

            String path = "/hbase"; // Đường dẫn node trong ZooKeeper
            reader.readNode(path);

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
