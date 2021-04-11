package com.lr.kafka.producer;

import org.apache.kafka.clients.producer.*;
import org.junit.Test;

import java.util.Properties;
import java.util.concurrent.Future;

/**
 * @author liurui
 * @date 2021/4/10 21:19
 */
public class ProducerApplication {


    private final static String TOPIC_NAME = "lr-topic";

    /**
     * Producer异步发送消息
     */
    @Test
    public void sendMessage() {

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.47.129:9092");
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.RETRIES_CONFIG, "0");
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        properties.put(ProducerConfig.LINGER_MS_CONFIG, "1");
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        //producer的主对象
        Producer<String, String> kafkaProducer = new KafkaProducer<>(properties);

        for (int i = 0; i < 10; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, "key-" + i, "value-" + i);
            kafkaProducer.send(record);
        }
        kafkaProducer.close();
    }


    /**
     * Producer同步发送消息(异步堵塞)
     */
    @Test
    public void SyncSendMessage() throws Exception{

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.47.129:9092");
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.RETRIES_CONFIG, "0");
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        properties.put(ProducerConfig.LINGER_MS_CONFIG, "1");
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        //producer的主对象
        Producer<String, String> kafkaProducer = new KafkaProducer<>(properties);

        for (int i = 0; i < 100; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, "key-" + i, "value-" + i);
            Future<RecordMetadata> send = kafkaProducer.send(record);
            RecordMetadata recordMetadata = send.get();
            System.out.println("partition:" + recordMetadata.partition() + ",offset:" + recordMetadata.offset());

        }
        kafkaProducer.close();
    }


    /**
     * Producer异步回调发送
     */
    @Test
    public void sendMessageWithCallBack() throws Exception{

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.47.129:9092");
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.RETRIES_CONFIG, "0");
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        properties.put(ProducerConfig.LINGER_MS_CONFIG, "1");
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        //producer的主对象
        Producer<String, String> kafkaProducer = new KafkaProducer<>(properties);

        for (int i = 0; i < 10; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, "key-" + i, "value-" + i);
            Future<RecordMetadata> send = kafkaProducer.send(record, (recordMetadata, exception) -> {

                System.out.println("partition:" + recordMetadata.partition() + ",offset:" + recordMetadata.offset());
            });


        }
        kafkaProducer.close();
    }


    /**
     * Producer异步回调发送和Partition负载均衡
     */
    @Test
    public void sendMessageWithCallBackAndPartition() throws Exception{

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.47.129:9092");
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.RETRIES_CONFIG, "0");
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        properties.put(ProducerConfig.LINGER_MS_CONFIG, "1");
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        //设置partition分区
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.lr.kafka.producer.MyPartition");
        //producer的主对象
        Producer<String, String> kafkaProducer = new KafkaProducer<>(properties);

        for (int i = 0; i < 10; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, "key-" + i, "value-" + i);
            Future<RecordMetadata> send = kafkaProducer.send(record, (recordMetadata, exception) -> {

                System.out.println("partition:" + recordMetadata.partition() + ",offset:" + recordMetadata.offset());
            });


        }
        kafkaProducer.close();
    }
}
