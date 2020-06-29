package com.zyl.tagme.common.async;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zyl.tagme.common.dto.TagmeResponse;
import com.zyl.tagme.common.http.HttpService;
import com.zyl.tagme.common.util.ResponseUtil;
import com.zyl.tagme.service.pojo.Blog;

import java.util.List;
import java.util.Objects;

import okhttp3.Response;

public class BlogAsyncTask extends AsyncTask<Void, Void, List<Blog>> {

    private int pageIndex;
    private int pageSize;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected List<Blog> doInBackground(Void... voids) {
        try {
            Response response = HttpService.getBlogs(pageIndex, pageSize);
            TagmeResponse tagmeResponse = ResponseUtil.getServiceResponse(response, TagmeResponse.class);
            assert tagmeResponse != null;
            if (tagmeResponse.isOk()) {
                String data = tagmeResponse.getData().toString();
                JSONObject jsonObject = JSONObject.parseObject(data);
                return JSONArray.parseArray(Objects.requireNonNull(jsonObject.get("records")).toString(), Blog.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public BlogAsyncTask getBlogs(int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        return this;
    }
}
