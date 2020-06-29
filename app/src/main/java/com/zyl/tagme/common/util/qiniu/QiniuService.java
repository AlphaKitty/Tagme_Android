package com.zyl.tagme.common.util.qiniu;

import android.content.Context;
import android.util.Log;

import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.zyl.tagme.common.async.QiniuAsyncTask;
import com.zyl.tagme.common.constant.FileType;
import com.zyl.tagme.common.dto.TagmeResponse;
import com.zyl.tagme.common.http.HttpService;
import com.zyl.tagme.common.util.MyToast;
import com.zyl.tagme.common.util.ResponseUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.ExecutionException;

import okhttp3.Response;

public class QiniuService {

    public static String getUpToken(FileType profile) {
        Response response = HttpService.getUpToken(profile);
        TagmeResponse tagmeResponse = ResponseUtil.getServiceResponse(response, TagmeResponse.class);
        assert tagmeResponse != null;
        return ResponseUtil.getServiceResponseData(tagmeResponse, String.class);
    }

    /**
     * 上传头像 头像在云上的存放和其他图片不是同一个bucket
     *
     * @param context     Context
     * @param profilePath 头像路径
     * @param userId      用户id 作为命名依据
     * @return 存放在七牛云上的文件名
     */
    public static String uploadProfile(final Context context, String profilePath, String userId) {

        String upToken;
        try {
            upToken = new QiniuAsyncTask().getUpToken(FileType.PROFILE).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            MyToast.showToast(context, "上传失败");
            return null;
        }

        Configuration config = new Configuration.Builder()
                .connectTimeout(10)           // 链接超时。默认10秒
                .useHttps(false)               // 是否使用https上传域名
                .responseTimeout(60)          // 服务器响应超时。默认60秒
                .zone(FixedZone.zone1)        // 设置区域，不指定会自动选择。指定不同区域的上传域名、备用域名、备用IP。
                .build();
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        // 配置3个线程数并发上传；不配置默认为3，只针对file.size>4M生效。线程数建议不超过5，上传速度主要取决于上行带宽，带宽很小的情况单线程和多线程没有区别
        UploadManager uploadManager = new UploadManager(config, 1);

        File file = new File(profilePath);
        // 用户id作为头像在云上的命名
        String profileName = userId + file.getName();
        uploadManager.put(file, profileName, upToken,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if (info.isOK()) {
                            MyToast.showToast(context, "上传成功");
                        } else {
                            MyToast.showToast(context, "上传失败");
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }
                        Log.e("error", key + ",\r\n " + info + ",\r\n " + res);
                    }
                }, null);
        return userId + file.getName();
    }
}
