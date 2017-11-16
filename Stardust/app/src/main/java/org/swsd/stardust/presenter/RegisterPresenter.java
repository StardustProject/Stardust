package org.swsd.stardust.presenter;

import android.content.Context;
import android.text.Editable;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.swsd.stardust.presenter.ButtonNavigationBarPresenter.tools.CommonFunctions;

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
    String responseData;
    int errorCode = 0;

    // 注册前的检查
    public int checkBeforeRegister(Context context, Editable username, Editable password, Editable confirm) {
        CommonFunctions fCheck = new CommonFunctions();
        if (fCheck.check(context, username, password)) {
            mStrUsername = username.toString();
            mStrPassword = password.toString();

            // 判断确认密码和输入密码是否一致
            if (confirm.toString().equals(mStrPassword)) {
                // 上传用户名和密码到服务器
                sendRequestWithOkHttp(mStrUsername, mStrPassword);
                while (errorCode == 0) {

                }
                if (errorCode == 200) {
                    Toast.makeText(context, "注册成功！", Toast.LENGTH_SHORT).show();
                    return 1;
                } else if (errorCode == 409) {
                    Toast.makeText(context, "此用户名已被注册！", Toast.LENGTH_SHORT).show();
                }
            } else {
                return 2;
            }
        }
        return 0;
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
                            .url("http://111.231.18.37/learnlaravel5/public/index.php/api/user/registration")
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
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


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

    private void parseJson(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            errorCode = jsonObject.getInt("error_code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
