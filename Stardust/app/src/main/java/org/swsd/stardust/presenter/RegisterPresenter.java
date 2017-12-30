package org.swsd.stardust.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.swsd.stardust.presenter.ButtonNavigationBarPresenter.tools.CommonFunctions;
import org.swsd.stardust.view.activity.LoginActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * author  ： 胡俊钦
 * time    ： 2017/11/16
 * desc    ： 注册逻辑
 * version ： 1.0
 */
public class RegisterPresenter {
    // 存放用户名输入框内容的变量
    private String mStrUsername = "";
    // 存放密码输入框内容的变量
    private String mStrPassword = "";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String responseData;
    private int mErrorCode = 0;
    private Context mContext;


    private Handler uiHandler = new Handler(){
        // 覆写这个方法，接收并处理消息。
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 200:
                    Toast.makeText(mContext, "注册成功！", Toast.LENGTH_SHORT).show();
                    Intent goToLogin = new Intent(mContext, LoginActivity.class);
                    goToLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(goToLogin);
                    break;
                case 409:
                        Toast.makeText(mContext, "此用户名已被注册！", Toast.LENGTH_SHORT).show();
                    break;
                case 403:
                    Toast.makeText(mContext, "注册失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                default:
                        Toast.makeText(mContext, "注册失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    // 注册前的检查
    public void checkBeforeRegister(Context context, Editable username, Editable password, Editable confirm) {
        CommonFunctions fCheck = new CommonFunctions();
        if (fCheck.check(context, username, password)) {
            mStrUsername = username.toString();
            mStrPassword = password.toString();
            mContext=context;
            // 判断确认密码和输入密码是否一致
            if (confirm.toString().equals(mStrPassword)) {
                // 上传用户名和密码到服务器
                sendRequestWithOkHttp(mStrUsername, mStrPassword);
            } else {
                Toast.makeText(mContext, "确认密码与输入密码不一致！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 向服务器发送请求
    public void sendRequestWithOkHttp(final String username, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 创建OkHttpClient实例
                    OkHttpClient client = new OkHttpClient();
                    String json = getJsonString(username, password);
                    RequestBody requestBody = RequestBody.create(JSON, json);
                    // 创建Request对象
                    Request request = new Request.Builder()
                            .url("http://119.29.179.150:81/api/user/registration")
                            .post(requestBody)
                            .build();
                    // 发送请求并获取服务器返回的数据
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            responseData = response.body().string();
                            parseJson(responseData);
                            Message msg = new Message();
                            msg.what = mErrorCode;
                            uiHandler.sendMessage(msg);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 将用户名密码设为json格式
    private static String getJsonString(String username, String password) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("account", username);
            obj.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    // 解析json
    private void parseJson(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            mErrorCode = jsonObject.getInt("error_code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
