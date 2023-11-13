package com.company.bigdata.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.Cell;
import org.janusgraph.diskstorage.ScanBuffer;
import org.janusgraph.graphdb.database.serialize.StandardSerializer;
import org.janusgraph.graphdb.database.serialize.attribute.UUIDSerializer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class HbaseConn {
    private static final String HBASE_GUID_TO_PII = "guid_to_pii_v2";
    private static Connection connection;

    private HbaseConn() {
        connection = initConnection();
    }


    private Connection initConnection() {
        if (connection == null) {
            Configuration conf = HBaseConfiguration.create();
            conf.set("hbase.zookeeper.quorum", "192.168.23.37,192.168.23.39,192.168.23.41");
            conf.set("hbase.zookeeper.property.clientPort", "2181");
            conf.set("hbase.client.scanner.caching", "5000");

            try {
                connection = ConnectionFactory.createConnection(conf);
                System.out.println("INIT HBASE CONNECTION !!!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public Table getTable(String name) throws IOException {
        return connection.getTable(TableName.valueOf(name));
    }

    private String getValueFromKey(String tableName, String familyName, String qualifierName, String key) throws IOException {
        Table table = getTable(tableName);

        byte[] rowkey = Bytes.toBytes(key);
        Get get = new Get(rowkey);

        Result result;
        try {
            result = table.get(get);
        } catch (IOException e) {
            return null;
        }
        byte[] family = Bytes.toBytes(familyName);
        byte[] qualifier = Bytes.toBytes(qualifierName);
        byte[] value = result.getValue(family, qualifier);

        return Bytes.toString(value);
    }

    private void  testScanId() throws IOException {

        Table table = getTable("pii_dev");

        Get get = new Get(Bytes.toBytes(112640L));
        Result result = table.get(get);

        byte[] row = result.getRow();
        byte[] value = result.getValue(Bytes.toBytes("f"), null);

        long id = Bytes.toLong(row);

        System.out.println("id " + id);
        for (Cell cell : result.rawCells()) {
//                System.out.println("version: " + count++);
            System.out.println("Cell: " + cell.toString() + ", Value: " +
                    Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                            cell.getValueLength()));

            StandardSerializer standardSerializer = new StandardSerializer();

//            UUID uuid = standardSerializer.convert(UUID.class, cell.getValueArray());

            String st = standardSerializer.convert(String.class, cell.getValueArray());

            System.out.println(  st.toString());

            System.out.println("Value: " + new String(st.getBytes(), StandardCharsets.UTF_8));
//                System.out.println("\tCell: " + cell + ", Value: " +
//                        Bytes.toLong(cell.getValueArray(), cell.getValueOffset(),
//                                cell.getValueLength())+", Version: "+versionCount++);
//            }
//                System.out.println("Value: " + Bytes.toString(value));
        }
    }

    private void  testScan() throws IOException {
        Scan scan = new Scan();
        scan.setLimit(1000); // Đặt giới hạn số lượng dòng cần quét
//        scan.setTimeRange(1685033389, 1685333421);

        Table table = getTable("pii_dev");

        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            // Xử lý dữ liệu từ mỗi Result
            byte[] row = result.getRow();
            byte[] value = result.getValue(Bytes.toBytes("f"), null);

            long id = Bytes.toLong(row);

            System.out.println("id " + id);
            for (Cell cell : result.rawCells()) {
//                System.out.println("version: " + count++);
                System.out.println("Cell: " + cell.toString() + ", Value: " +
                        Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                                cell.getValueLength()));
//                System.out.println("\tCell: " + cell + ", Value: " +
//                        Bytes.toLong(cell.getValueArray(), cell.getValueOffset(),
//                                cell.getValueLength())+", Version: "+versionCount++);
//            }
//                System.out.println("Value: " + Bytes.toString(value));
            }

            scanner.close();
        }

    }

//    public long getTimePublishById(long id) {
//
//        Get get = new Get(Bytes.toBytes(id));
//        int count = 0;
//        get.addColumn(ConfigInfo.Hbase.familyTime, ConfigInfo.Hbase.qualifierTimePublish);
//        Result result = null;
//        try {
//            result = table.get(get);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        byte[] valueBytes = result.getValue(ConfigInfo.Hbase.familyTime, ConfigInfo.Hbase.qualifierTimePublish);
//        long value = Bytes.toLong(valueBytes);
//
//        return value;
//    }

//    public void getAllVersionById(long id, long time_start, long time_end) {
//        Get get = new Get(Bytes.toBytes(id));
//        try {
//            get.readAllVersions();
//            get.setTimeRange(time_start, time_end);
////            get.addColumn(ConfigInfo.Hbase.familyTime, ConfigInfo.Hbase.qualifierTimeCreate);
////            get.addColumn(ConfigInfo.Hbase.familyTime, ConfigInfo.Hbase.qualifierTimeUpdate);
////            get.addColumn(ConfigInfo.Hbase.familyTime, ConfigInfo.Hbase.qualifierTimePublish);
//            get.addColumn(ConfigInfo.Hbase.familyTime, ConfigInfo.Hbase.qualifierTimeGet);
////            get.addColumn(ConfigInfo.Hbase.familyInfo, ConfigInfo.Hbase.qualifierHash);
//            int versionCount = 0;
//            Result result = table.get(get);
//            for (Cell cell : result.rawCells()) {
//                System.out.println("version: " + count++);
//                System.out.println("Cell: " + cell + ", Value: " +
//                        Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
//                                cell.getValueLength()));
//                System.out.println("\tCell: " + cell + ", Value: " +
//                        Bytes.toLong(cell.getValueArray(), cell.getValueOffset(),
//                                cell.getValueLength())+", Version: "+versionCount++);
//            }
//
//
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

//    public int getNumberOfCellByTime(long time_start, long time_end){
//        int count=0;
//        Scan scan = new Scan();
//        try {
//            scan.setTimeRange(time_start, time_end);
//            scan.readAllVersions();
////            scan.addColumn(ConfigInfo.Hbase.familyTime, ConfigInfo.Hbase.qualifierTimeCreate);
////            scan.addColumn(ConfigInfo.Hbase.familyTime, ConfigInfo.Hbase.qualifierTimeUpdate);
////            scan.addColumn(ConfigInfo.Hbase.familyTime, ConfigInfo.Hbase.qualifierTimePublish);
//            scan.addColumn(ConfigInfo.Hbase.familyTime, ConfigInfo.Hbase.qualifierTimeGet);
////            scan.addColumn(ConfigInfo.Hbase.familyInfo, ConfigInfo.Hbase.qualifierHash);
//            ResultScanner scanner = table.getScanner(scan);
//            for (Result result : scanner) {
//                // Process each row
//                int cellCount = result.size();
//                if(cellCount>1) {
//                    count++;
//                    // Print the row key and version count
//                    System.out.println("Row: " + Bytes.toLong(result.getRow()) + ", Versions: " + cellCount);
//                    long id = Bytes.toLong(result.getRow());
//                    Get get = new Get(Bytes.toBytes(id));
//                    get.addColumn(ConfigInfo.Hbase.familyTime, ConfigInfo.Hbase.qualifierTimeUpdate);
//                    getAllVersionById(id, time_start, time_end);
//                    if (count == 100) break;
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        return count;
//    }


    public static void main(String[] args) throws IOException {
        HbaseConn conn = new HbaseConn();
        conn.testScanId();
    }

}
