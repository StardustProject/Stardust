package org.swsd.stardust.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.swsd.stardust.base.ActivityCollector;
import org.swsd.stardust.model.bean.MailBean;
import org.swsd.stardust.model.bean.NoteBean;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.util.UpdateTokenUtil;
import org.swsd.stardust.view.activity.MainActivity;

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
 * author  ： 胡俊钦，林炜鸿
 * time    ： 2017/11/14
 * desc    ： 用户的presenter层
 * version ： 1.0
 */
public class UserPresenter{
    private Context mContext;
    // 网络请求所需变量
    private String responseData;
    private int errorCode = 0;

    private boolean ready = false;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public UserBean toGetUserInfo() {
        UserBean userBean;
        userBean = DataSupport.findLast(UserBean.class);
        if (userBean == null) {
            // userBean = useDefaultData();
            // TODO: 抛出错误表明未登录
        }
        return userBean;
    }


    // 默认数据，防止数据库没有数据时导致程序崩溃
    public UserBean useDefaultData() {
        UserBean userBean = new UserBean();

        // 设置默认用户userId
        userBean.setUserId(0);
        // 设置默认用户名
        userBean.setUserName("Username");
        // 获取当前时间作为默认注册时间值
        Date date = new Date();
        Long time = date.getTime();
        userBean.setRegisterTime(time);
        userBean.save();
        return userBean;
    }


    public int toGetStarNum() {
        int num;
        num = DataSupport.count(NoteBean.class);
        return num;
    }

    public void toLogin(Context context, String username, String password) {
        mContext = context;
        // 发送登录请求
        Log.d("UserPresenter", "Begin to send request");
        sendRequestWithOkHttp(username, password);
    }

    public void toLogout() {
        // 用户信息的本地存储
        DataSupport.deleteAll(UserBean.class);
        // 日记的本地存储
        DataSupport.deleteAll(NoteBean.class);
        // 站内信的本地存储
        DataSupport.deleteAll(MailBean.class);
    }

    // 向服务器发送登录请求
    private void sendRequestWithOkHttp(final String username, final String password) {
        final Handler uiHandler = new Handler(){
            // 覆写这个方法，接收并处理消息。
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 200:
                        Log.d("UserPresenter", "get Response");
                        Intent goToMain = new Intent(mContext, MainActivity.class);
                        goToMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        // 登录结束后销毁登录页面
                        ActivityCollector.finishAll();
                        mContext.startActivity(goToMain);
                        break;
                    case 403:
                        Toast.makeText(mContext, "用户名不存在！", Toast.LENGTH_SHORT).show();
                    case 401:
                        Toast.makeText(mContext, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(mContext, "登录失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

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
                            .url("http://119.29.179.150:81/api/user/login")
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
                            Message msg = new Message();
                            msg.what = errorCode;
                            uiHandler.sendMessage(msg);
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
        UserBean userBean = new UserBean();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            errorCode = jsonObject.getInt("error_code");

            // 如果返回的错误代码为200，将用户信息存入数据库
            if (errorCode == 200) {
                DataSupport.deleteAll(UserBean.class);

                JSONObject innerObject = jsonObject.getJSONObject("data");

                // 用户id
                int userId = innerObject.getInt("user_id");
                userBean.setUserId(userId);

                // 用户名
                String userName = innerObject.getString("account");
                userBean.setUserName(userName);

                // 用户token
                String token = innerObject.getString("access_token");
                userBean.setToken(token);

                // token过期时间
                String expireTime = innerObject.getString("expire_time");
                userBean.setTokenTime(expireTime);

                // 用户刷新token
                String refreshToken = innerObject.getString("refresh_token");
                userBean.setRefreshToken(refreshToken);

                // 注册时间
                String registerTime = innerObject.getString("create_time");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                try {
                    date = sdf.parse(registerTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                userBean.setRegisterTime(date.getTime());
                // 保存数据库
                userBean.save();
                userBean = DataSupport.findLast(UserBean.class);
                Log.d("UserPresenter", "Login finish: " + userBean.getUserName());
                // 获取用户头像url
                getPhoto(userBean);
            } else {
                ready = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 获取用户头像url
    private void getPhoto(final UserBean userBean) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UpdateTokenUtil.updateUserToken(userBean);
                    while(!UpdateTokenUtil.refreshOk&&UpdateTokenUtil.isUpdate){

                    }
                    // 创建OkHttpClient实例
                    OkHttpClient client = new OkHttpClient();
                    // 创建Request对象
                    Request request = new Request.Builder()
                            .url("http://119.29.179.150:81/api/users/" + userBean.getUserId() + "/avatar")
                            .header("Authorization", userBean.getToken())
                            .build();
                    // 发送请求并获取服务器返回的数据
                    errorCode = 0;
                    Response response = client.newCall(request).execute();
                    responseData = response.body().string();
                    Log.d("luojingzhao", responseData);
                    // 解析错误代码
                    JSONObject jsonObject = new JSONObject(responseData);
                    errorCode = jsonObject.getInt("error_code");
                    // 如果错误代码为200
                    if (errorCode == 200) {
                        JSONObject innerObject = jsonObject.getJSONObject("data");
                        String avatarPath = innerObject.getString("avatar_url");
                        userBean.setAvatarPath(avatarPath);
                        userBean.updateAll();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
