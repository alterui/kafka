package com.lr.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * @author liurui
 * @date 2021/4/11 21:15
 */
public class ConsumerApplication {
    private final static String TOPIC_NAME = "lr-topic";

    /**
     * 工作里这种用法，有，但是不推荐
     */
    @Test
    public   void consumerMessage(){
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "192.168.47.129:9092");
        props.setProperty("group.id", "test");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String,String> consumer = new KafkaConsumer<>(props);
        // 消费订阅哪一个Topic或者几个Topic
        consumer.subscribe(Collections.singletonList(TOPIC_NAME));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("partition = %d , offset = %d, key = %s, value = %s%n",
                        record.partition(),record.offset(), record.key(), record.value());
        }
    }


    /**
     * 手动提交
     */
    @Test
    public  void commitOffsetMessage(){
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "192.168.47.129:9092");
        props.setProperty("group.id", "test");
        //自动提交改为false
        props.setProperty("enable.auto.commit", "false");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String,String> consumer = new KafkaConsumer<>(props);





        // 消费订阅哪一个Topic或者几个Topic
        consumer.subscribe(Collections.singletonList(TOPIC_NAME));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                //比如讲数据放到数据库中，如果失败要进行回滚
                System.out.printf("partition = %d , offset = %d, key = %s, value = %s%n",
                        record.partition(), record.offset(), record.key(), record.value());
            }
            //手动通知offerSet提交
            consumer.commitAsync();
        }
    }


}
