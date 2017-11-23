package org.swsd.stardust.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.swsd.stardust.model.bean.UserBean;
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
 * time    ： 2017/11/17
 * desc    ： 修改密码presenter
 * version ： 1.0
 */
public class SetPswPresenter {
    int errorCode = 0;
    String responseData;
    UserBean userBean = new UserBean();
    private Context mContext;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private Handler uiHandler = new Handler() {
        // 覆写这个方法，接收并处理消息。
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 200:
                    Toast.makeText(mContext, "修改密码成功", Toast.LENGTH_SHORT).show();
                    break;
                case 403:
                    Toast.makeText(mContext, "您输入的旧密码不正确！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(mContext, "修改密码失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void checkBeforeSetPsw(Context context, Editable oldPassword,
                                     Editable newPassword, Editable confirmPassword) {
        boolean correct = false;
        CommonFunctions check = new CommonFunctions();
        mContext = context;
        // 判断新密码长度是否符合
        switch (check.checkLength(newPassword)) {
            case 1:
                Toast.makeText(context, "新密码不能为空！", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(context, "新密码长度不能小于6！", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(context, "新密码长度不能大于20！", Toast.LENGTH_SHORT).show();
                break;
            case 0:
                correct = true;
                break;
            default:
        }

        // 如果新密码长度合法
        if (correct == true) {
            // 检查新密码是否存在非法字符
            if (!check.checkPasswordChar(newPassword)) {
                correct = false;
                Toast.makeText(context, "密码不允许出现非法字符！", Toast.LENGTH_SHORT).show();
            }
        }

        // 如果上诉检查通过，检查新密码与旧密码是否相同
        if (correct == true) {
            // 判断旧密码与新密码是否相同
            if (newPassword.toString().equals(oldPassword.toString())) {
                correct = false;
                Toast.makeText(context, "新密码不能与原密码相同！", Toast.LENGTH_SHORT).show();
            }
        }

        // 如果格式检查全部通过
        if (correct == true) {
            // 判断确认密码与新密码是否相同
            if (!newPassword.toString().equals(confirmPassword.toString())) {
                Toast.makeText(context, "新密码与确认密码不一致！", Toast.LENGTH_SHORT).show();
            } else {
                // 如果检查全部通过
                resetPassword(oldPassword.toString(), newPassword.toString());
            }
        }
    }

    private void resetPassword(final String oldPassword, final String newPassword) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    userBean = DataSupport.findLast(UserBean.class);
                    // 创建OkHttpClient实例
                    OkHttpClient client = new OkHttpClient();
                    // 将密码设为Json格式
                    String json = getJsonString(oldPassword, newPassword);
                    RequestBody requestBody = RequestBody.create(JSON, json);
                    // 创建Request对象
                    Request request = new Request.Builder().
                            url("http://119.29.179.150:81/api/users/"
                                    + userBean.getUserId() + "/password")
                            .header("Authorization", userBean.getToken())
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
                            try {
                                JSONObject jsonObject = new JSONObject(responseData);
                                errorCode = jsonObject.getInt("error_code");
                                Message msg = new Message();
                                msg.what = errorCode;
                                uiHandler.sendMessage(msg);
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

    // 生成Json格式的字符串
    private static String getJsonString(String oldPassword, String newPassword) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("old_password", oldPassword);
            obj.put("new_password", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }
}

