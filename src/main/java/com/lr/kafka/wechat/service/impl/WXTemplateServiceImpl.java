package com.lr.kafka.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lr.kafka.wechat.configuration.WXTemplateProperties;
import com.lr.kafka.wechat.service.WXTemplateService;
import com.lr.kafka.wechat.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author liurui
 * @date 2021/4/11 16:22
 */
@Service
@Slf4j
public class WXTemplateServiceImpl implements WXTemplateService {

    @Autowired
    private WXTemplateProperties properties;

    @Autowired
    private KafkaProducer<String,Object> producer;

    @Override
    public WXTemplateProperties.WXTemplate getWXTemplate() {
        List<WXTemplateProperties.WXTemplate> templates = properties.getTemplates();
        Optional<WXTemplateProperties.WXTemplate> template = templates.stream().filter(e -> e.isActive()).findFirst();
        return template.orElse(null);
    }

    @Override
    public void templateReported(JSONObject reportInfo) {
        //推送消息到kafka
        log.info("templateReported:{}", reportInfo);

            String topicName = "lr-topic";
            // 发送Kafka数据
            String templateId = reportInfo.getString("templateId");
            JSONArray reportData = reportInfo.getJSONArray("result");

            // 如果templateId相同，后续在统计分析时，可以考虑将相同的id的内容放入同一个partition，便于分析使用
            ProducerRecord<String,Object> record =
                    new ProducerRecord<>(topicName,templateId,reportData);

        /**
         *  1、Kafka Producer是线程安全的，建议多线程复用，如果每个线程都创建，出现大量的上下文切换或争抢的情况，影响Kafka效率
         *  2、Kafka Producer的key是一个很重要的内容：
         *                 2.1 我们可以根据Key完成Partition的负载均衡
         *                 2.2 合理的Key设计，可以让Flink、Spark Streaming之类的实时分析工具做更快速处理
         *
         *  3、ack - all， kafka层面上就已经有了只有一次的消息投递保障，但是如果想真的不丢数据，最好自行处理异常
         */
            try{
                producer.send(record);
            }catch (Exception e){
                // 将数据加入重发队列， redis，es，...
            }
    }

    @Override
    public JSONObject templateStatistics(String templateId) {
        return FileUtils.readFile2JsonObject(properties.getTemplateResultFilePath()).orElse(null);
    }

}
