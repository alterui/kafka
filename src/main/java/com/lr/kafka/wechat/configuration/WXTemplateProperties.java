package com.lr.kafka.wechat.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author liurui
 * @date 2021/4/11 16:41
 */
@Configuration
@ConfigurationProperties(prefix = "template")
@Data
public class WXTemplateProperties {
    private List<WXTemplate> templates;
    private Integer templateResultType;
    private String templateResultFilePath;

  @Data
  public static class  WXTemplate{
        private String templateId;
        private String templateFilePath;
        private boolean active;
    }

}


