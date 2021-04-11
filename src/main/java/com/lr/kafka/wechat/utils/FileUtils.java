package com.lr.kafka.wechat.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Optional;

/**
 * @author liurui
 * @date 2021/4/11 16:34
 */
@Slf4j
public class FileUtils {
    public static String readFile(String filePath) throws Exception {
        @Cleanup
        BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));

        String lineStr = "";
        StringBuilder stringBuilder = new StringBuilder();
        while ((lineStr = reader.readLine()) != null) {
            stringBuilder.append(lineStr);
        }

        return stringBuilder.toString();
    }

    public static Optional<JSONObject> readFile2JsonObject(String filePath) {
        try {
            String s = readFile(filePath);
            return Optional.ofNullable(JSON.parseObject(s));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public static Optional<JSONArray> readFile2JsonArray(String filePath) {
        try {
            String s = readFile(filePath);
            return Optional.ofNullable(JSON.parseArray(s));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

}
