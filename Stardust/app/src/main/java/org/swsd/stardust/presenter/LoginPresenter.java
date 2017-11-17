package org.swsd.stardust.presenter;

import android.content.Context;
import android.text.Editable;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.presenter.ButtonNavigationBarPresenter.tools.CommonFunctions;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
 * desc    ： 登录逻辑
 * version ： 1.0
 */
public class LoginPresenter {
    String responseData;
    boolean parseOk = false;
    int errorCode = 0;
    int id;
    String userName;
    String token;
    String avatarPath;
    String registerTime;
    String expireTime;
    UserBean userBean;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public boolean checkBeforeLogin(final Context context, Editable username, Editable password) {
        // 创建工具类对象
        final CommonFunctions fCheck = new CommonFunctions();

        if (fCheck.check(context, username, password)) {
            String strUsername = username.toString();
            String strPassword = password.toString();
            // 向服务器发送用户名和密码
            sendRequestWithOkHttp(strUsername, strPassword);
            // 用户名密码是否匹配
            if (isMatch(strUsername, strPassword)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                    }
                }).start();
                return true;
            } else {
                Toast.makeText(context, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    // 判断用户名和密码是否正确
    private boolean isMatch(String strUsername, String strPassword) {
        while (errorCode == 0) {

        }
        if (errorCode == 200) {
            return true;
        }
        return false;
    }

    // 向服务器发送请求
    public void sendRequestWithOkHttp(final String username, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 创建OkHttpClient实例
                    OkHttpClient client = new OkHttpClient();
                    // 将用户名和密码设为Json格式
                    String json = getJsonString(username, password);
                    RequestBody requestBody = RequestBody.create(JSON, json);
                    // 创建Request对象
                    Request request = new Request.Builder()
                            .url("http://111.231.18.37/learnlaravel5/public/index.php/api/user/login")
                            .put(requestBody)
                            .build();
                    // 发送请求并获取服务器返回的数据
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            responseData = response.body().string();
                            // 解析返回的Json
                            parseJson(responseData);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 生成Json格式的字符串
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

    // 解析Json
    private void parseJson(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            errorCode = jsonObject.getInt("error_code");
            if (errorCode == 200) {
                id = jsonObject.getInt("user_id");
                userBean.setId(id);
                userName = jsonObject.getString("account");
                userBean.setUserName(userName);
                token = jsonObject.getString("access_token");
                userBean.setToken(token);
                expireTime = jsonObject.getString("expire_time");
              /*  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date expireTimeDate=new Date();
                try{
                    expireTimeDate= sdf.parse(expireTime);
                }catch (ParseException e){
                    e.printStackTrace();
                }
                userBean.setExpireTime(expireTimeDate.getTime());*/
                // 注册时间
                registerTime = jsonObject.getString("create_time");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                try {
                    date = sdf.parse(registerTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                userBean.setRegisterTime(date.getTime());
                userBean.save();
                getPhoto();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 获取用户头像
    private void getPhoto() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 创建OkHttpClient实例
                    OkHttpClient client = new OkHttpClient();
                    // 创建Request对象
                    Request request = new Request.Builder().
                            url("http://111.231.18.37/learnlaravel5/public/index.php/api//users/" + id + "/avater")
                            .header("token", token)
                            .build();
                    // 发送请求并获取服务器返回的数据
                    errorCode = 0;
                    Response response = client.newCall(request).execute();
                    responseData = response.body().toString();
                    JSONObject jsonObject = new JSONObject(responseData);
                    errorCode = jsonObject.getInt("error_code");
                    if (errorCode == 200) ;
                    {
                        avatarPath = jsonObject.getString("avatar_url");
                        userBean.setAvatarPath(avatarPath);
                        userBean.save();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}







