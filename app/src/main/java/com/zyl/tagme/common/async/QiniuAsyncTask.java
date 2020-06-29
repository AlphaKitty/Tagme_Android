package com.zyl.tagme.common.async;

import android.os.AsyncTask;

import com.zyl.tagme.common.constant.FileType;
import com.zyl.tagme.common.dto.TagmeResponse;
import com.zyl.tagme.common.http.HttpService;
import com.zyl.tagme.common.util.ResponseUtil;

import okhttp3.Response;

public class QiniuAsyncTask extends AsyncTask<Void, Void, String> {

    private FileType profile;

    @Override
    protected String doInBackground(Void... voids) {
        Response response = HttpService.getUpToken(profile);
        TagmeResponse tagmeResponse = ResponseUtil.getServiceResponse(response, TagmeResponse.class);
        assert tagmeResponse != null;
        return ResponseUtil.getServiceResponseData(tagmeResponse, String.class);
    }

    public QiniuAsyncTask getUpToken(FileType profile) {
        this.profile = profile;
        return this;
    }
}
