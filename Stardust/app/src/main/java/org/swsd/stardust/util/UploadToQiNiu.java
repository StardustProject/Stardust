package org.swsd.stardust.util;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.swsd.stardust.model.bean.UserBean;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * author  ： 胡俊钦
 * time    ： 2017/11/18
 * desc    ： 上传图片到七牛云
 * version ： 1.0
 */
public class UploadToQiNiu {
    public String url;
    private String upToken;
    private String mLocalFilePath;
    public Message msg = new Message();

    private Handler uiHandler = new Handler() {
        // 覆写这个方法，接收并处理消息。
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case 1:
                    // 上传头像到七牛云
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String key = null;
                            // 七牛机房设置，构造一个带指定Zone对象的配置类
                            Configuration cfg = new Configuration(Zone.zone0());
                            UploadManager uploadManager = new UploadManager(cfg);
                            try {
                                Response response = uploadManager.put(mLocalFilePath, key, upToken);
                                //解析上传成功的结果
                                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                                url = "http://ozcxh8wzm.bkt.clouddn.com/" + putRet.key;

                            } catch (QiniuException ex) {
                                Response r = ex.response;
                                System.err.println(r.toString());
                                try {
                                    System.err.println(r.bodyString());
                                } catch (QiniuException ex2) {
                                    //ignore
                                }
                            }
                        }
                    }).start();
                    break;
                default:
                    break;
            }
        }
    };

    // 上传头像到七牛云
    public void uploadQiNiu(final String path) {
        mLocalFilePath = path;
        // 生成上传凭证，然后准备上传
        getQiniuToken();
    }

    int errorCode = 0;

    //获取七牛云Token
    public void getQiniuToken() {
        final UserBean userBean = DataSupport.findLast(UserBean.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://119.29.179.150:81/api/users/" + userBean.getUserId() + "/qiniu_token")
                            .header("Authorization", userBean.getToken())
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, okhttp3.Response response) throws IOException {
                            String responseData = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(responseData);
                                errorCode = jsonObject.getInt("error_code");
                                // 如果返回的错误代码为200
                                if (errorCode == 200) {
                                    JSONObject innerObject = jsonObject.getJSONObject("data");
                                    upToken = innerObject.getString("qiniu_token");
                                    msg.what = 1;
                                    uiHandler.sendMessage(msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}



