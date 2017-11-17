package org.swsd.stardust.presenter.NotePresenter;

import android.accounts.Account;
import android.util.Log;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * author : 熊立强
 * time   : 2017/11/17
 * desc   : 发送json
 * version: 1.0
 */


public class HttpPresenter {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * 给定一个类，生成Json格式。
     *
     * @param object 需要被解析成Json的类
     * @return
     */
    private String getJsonString(Object object){
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return json;
    }

    /**
     * 向服务器发送一个json文件
     */
    private void postJson(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建一个Client对象
                OkHttpClient okHttpClient = new OkHttpClient();
                //json为String类型的json数据
                // 使用Gson生成
                Account account = new Account("sue","1234561");
                String json = getJsonString(account);
                Log.d(TAG, "json is " + json);
                RequestBody requestBody = RequestBody.create(JSON,json);
                Request request = new Request.Builder()
                        .url("http://111.231.18.37/learnlaravel5/public/index.php/api/login")
                        .addHeader("Content-Type","application/json")
                        .post(requestBody)
                        .build();
                try{
                    Response response = okHttpClient.newCall(request).execute();
                    String responseData = response.body().string();
                    // 发送完之后接受到的数据
                    Log.d("熊立强", "responseData" + responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
