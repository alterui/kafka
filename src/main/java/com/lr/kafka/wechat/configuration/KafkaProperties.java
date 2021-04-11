package com.lr.kafka.wechat.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author liurui
 * @date 2021/4/11 20:56
 */
@Configuration
@ConfigurationProperties(prefix = "wechat.kafka")
@Data
public class KafkaProperties {
    private String bootstrapServersConfig;
    private String acksConfig;
}
