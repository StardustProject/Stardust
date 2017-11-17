package org.swsd.stardust.util;

import android.util.Log;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import org.swsd.stardust.model.bean.UserBean;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * author     :  骆景钊
 * time       :  2017/11/15
 * description:  更新用户Token
 *               更新七牛云Token
 * version:   :  1.0
 */

public class UpdateTokenUtil {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "luojingzhao";

    //更新用户的Token
    public static void updateUserToken(UserBean user){

        final UserBean userBean = user;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    String json = getJsonSrting(userBean);
                    Log.d("luojingzhao",json);
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url("http://111.231.18.37/api/user/"+userBean.getId()+"/access_token")
                            .addHeader("Content-Type","application/json")
                            .addHeader("Authorization",userBean.getToken())
                            .put(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    updateDatabase(responseData);
                    Log.d("luojingzhao",responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //获取七牛云Token
    public static void getQiniuToken(UserBean user){

        final UserBean userBean = user;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    String json = getJsonSrting(userBean);
                    Log.d("luojingzhao", json);
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url("http://111.231.18.37/api/user/"+userBean.getId()+"/qiniu_token")
                            .addHeader("Authorization", userBean.getToken())
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    updateQiniuDatabase(responseData);
                    Log.d("luojingzhao", responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //更新数据库用户Token
    private static void updateDatabase(String responseData){
        try {
            JSONObject jsonObject = new JSONObject(responseData);

            //检验是否成功
            if(ErrorCodeJudgment.errorCodeJudge(responseData) == "Ok"){
                String user_id = jsonObject.getString("user_id");
                String refresh_token = jsonObject.getString("qiniu_token");
                String expire_time = jsonObject.getString("expire_time");
                UserBean userBean = new UserBean();
                userBean.setId(Integer.parseInt(user_id));
                userBean.setToken(refresh_token);
                userBean.setTokenTime(expire_time);
                userBean.updateAll("id = ?", user_id);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //更新数据库七牛云Token
    private static void updateQiniuDatabase(String responseData){
        try {
            JSONObject jsonObject = new JSONObject(responseData);

            //检验是否成功
            if(ErrorCodeJudgment.errorCodeJudge(responseData) == "Ok"){
                String user_id = jsonObject.getString("user_id");
                String refresh_token = jsonObject.getString("refresh_token");
                String expire_time = jsonObject.getString("expire_time");
                UserBean userBean = new UserBean();
                userBean.setId(Integer.parseInt(user_id));
                userBean.setQiniuToken(refresh_token);
                userBean.setQiniuTime(expire_time);
                userBean.updateAll("id = ?", user_id);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private static String getJsonSrting(Object object){
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return json;
    }
}
