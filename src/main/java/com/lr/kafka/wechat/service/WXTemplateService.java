package com.lr.kafka.wechat.service;


import com.alibaba.fastjson.JSONObject;
import com.lr.kafka.wechat.configuration.WXTemplateProperties;

/**
 * @author liurui
 * @date 2021/4/11 16:22
 */
public interface WXTemplateService {
    WXTemplateProperties.WXTemplate getWXTemplate();

    void templateReported(JSONObject reportInfo);

    JSONObject templateStatistics(String templateId);
}
