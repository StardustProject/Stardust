package org.swsd.stardust.presenter;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.swsd.stardust.model.bean.MeteorBean;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.util.UpdateTokenUtil;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * author     :  骆景钊
 * time       :  2017/12/15
 * description:  流星的举报上传
 * version:   :  1.0
 */

public class SetReportMeteorPresenter {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public void reportMeteor(final MeteorBean meteorBean) {

        final UserBean userBean = DataSupport.findFirst(UserBean.class);

        UpdateTokenUtil.updateUserToken(userBean);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //流星点赞数上传服务器
                    OkHttpClient client = new OkHttpClient();
                    String json = getJsonString(String.valueOf(userBean.getUserId()));
                    RequestBody requestBody = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url("http://119.29.179.150:81/api/users/"+userBean.getUserId()+"/meteors/"
                                    + meteorBean.getMeteorId() + "/report" )
                            .addHeader("Content-Type","application/json")
                            .addHeader("Authorization",userBean.getToken())
                            .put(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("luojingzhao",responseData);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 生成Json格式的字符串
    private static String getJsonString(String username) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("account", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }
}
