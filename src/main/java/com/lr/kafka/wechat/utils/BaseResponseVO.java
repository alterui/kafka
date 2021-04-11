package com.lr.kafka.wechat.utils;

import lombok.Data;

import java.util.UUID;

/**
 * @author liurui
 * @date 2021/4/11 16:25
 */
@Data
public class BaseResponseVO<T> {
    private String requestId;
    private T result;

    public static <T> BaseResponseVO<T> success() {
        BaseResponseVO<T> baseResponseVO = new BaseResponseVO<>();
        baseResponseVO.setRequestId(genRequestId());
        return baseResponseVO;
    }

    public static <T> BaseResponseVO<T> success(T result) {
        BaseResponseVO<T> baseResponseVO = new BaseResponseVO<>();
        baseResponseVO.setRequestId(genRequestId());
        baseResponseVO.setResult(result);

        return baseResponseVO;

    }

    private static String genRequestId() {
        return UUID.randomUUID().toString();
    }

}
