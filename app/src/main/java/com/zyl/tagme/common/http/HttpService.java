package com.zyl.tagme.common.http;

import com.zyl.tagme.common.constant.FileType;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpService {
    public static Response getBlogs(int pageIndex, int pageSize) {
        // Resources.getSystem().getString(R.string.url);
        Response response = null;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://47.94.251.18:9092/blog/page?pageIndex=" + pageIndex + "&pageSize=" + pageSize + "")
                .method("GET", null)
                .build();
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 根据文件类型获取upToken
     *
     * @param fileType 文件类型枚举
     * @return upToken
     */
    public static Response getUpToken(FileType fileType) {
        Response response = null;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.199.224:9092/qiniu/up_token?fileType=" + fileType.toString())
                .method("GET", null)
                .build();
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
