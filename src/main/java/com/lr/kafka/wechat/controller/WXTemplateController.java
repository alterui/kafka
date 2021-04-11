package com.lr.kafka.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.lr.kafka.wechat.configuration.WXTemplateProperties;
import com.lr.kafka.wechat.service.WXTemplateService;
import com.lr.kafka.wechat.utils.BaseResponseVO;
import com.lr.kafka.wechat.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author liurui
 * @date 2021/4/11 16:21
 */
@RestController
@RequestMapping("/v1")
public class WXTemplateController {

    @Autowired
    private WXTemplateProperties properties;

    @Autowired
    private WXTemplateService wxTemplateService;

   @GetMapping("/template")
   public BaseResponseVO<Map<String, Object>> getWXTemplate(){
       WXTemplateProperties.WXTemplate wxTemplate = wxTemplateService.getWXTemplate();
       Map<String, Object> map = Maps.newHashMap();
       map.put("templateId", wxTemplate.getTemplateId());
       map.put("template", FileUtils.readFile2JsonArray(wxTemplate.getTemplateFilePath()));
       return BaseResponseVO.success(map);
   }

    @GetMapping("/template/result")
    public BaseResponseVO<JSONObject> getTemplateStatistics(@RequestParam(value = "templateId",required = false) String templateId) {

        JSONObject jsonObject = wxTemplateService.templateStatistics(templateId);
        System.out.println(jsonObject.toJSONString());
        System.out.println(jsonObject);
        System.out.println(jsonObject.get("totalNumber"));
        return BaseResponseVO.success(jsonObject);
    }

    @PostMapping("/template/report")
    public BaseResponseVO<Integer> dataReport(@RequestBody String reportData) {
        wxTemplateService.templateReported(JSON.parseObject(reportData));
        return BaseResponseVO.success(1);
    }


}


