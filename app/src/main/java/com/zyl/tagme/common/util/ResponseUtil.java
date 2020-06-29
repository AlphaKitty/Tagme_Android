package com.zyl.tagme.common.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyl.tagme.common.dto.TagmeResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import okhttp3.Response;

public class ResponseUtil {
    /**
     * 获取返回的okhttp3.Response中的业务Response 后面可能会有多种业务Response 所以用了泛型
     *
     * @param response okhttp3.Response
     * @param clazz    业务Response泛型
     * @param <T>      泛型
     * @return 业务Response
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static <T> T getServiceResponse(Response response, Class<T> clazz) {
        if (response.isSuccessful()) {

            String resultStr = null;

            try {
                resultStr = new String(Objects.requireNonNull(response.body()).bytes(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return JSON.parseObject(resultStr, clazz);
        }
        return null;
    }

    /**
     * 根据泛型从业务Response中获取data内容
     *
     * @param tagmeResponse 业务Response
     * @param clazz         泛型
     * @param <T>           泛型
     * @return 泛型
     */
    public static <T> T getServiceResponseData(TagmeResponse tagmeResponse, Class<T> clazz) {
        assert tagmeResponse != null;
        T result = null;
        if (tagmeResponse.isOk()) {
            String data = tagmeResponse.getData().toString();
            if (clazz.equals(String.class))
                return (T) data;
            JSONObject jsonObject = JSONObject.parseObject(data);
            try {
                result = JSON.toJavaObject(jsonObject, clazz);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return result;
    }
}
