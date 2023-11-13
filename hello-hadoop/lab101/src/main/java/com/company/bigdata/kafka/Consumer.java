package com.company.bigdata.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.Arrays;
import java.util.Properties;

public class Consumer {

    public static void main(String[] args) {
        // Cấu hình Kafka Consumer
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        // Tạo Kafka Consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        // Subscribe vào topic
        consumer.subscribe(Arrays.asList("my-topic"));

        try {
            while (true) {
                // Tiêu thụ các tin nhắn từ Kafka topic
                ConsumerRecords<String, String> records = consumer.poll(100); // thời gian chờ 100ms

                for (ConsumerRecord<String, String> record : records) {
                    // Xử lý dữ liệu tiêu thụ
                    System.out.printf("Topic: %s, Partition: %d, Offset: %d, Key: %s, Value: %s\n",
                            record.topic(), record.partition(), record.offset(), record.key(), record.value());
                }

                // Commit offset tự động sau khi tiêu thụ xong batch của tin nhắn
                consumer.commitSync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Đóng Kafka Consumer khi kết thúc
            consumer.close();
        }
    }
}
