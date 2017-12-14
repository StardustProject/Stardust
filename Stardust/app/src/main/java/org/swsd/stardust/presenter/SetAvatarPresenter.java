package org.swsd.stardust.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.util.UpdateTokenUtil;
import org.swsd.stardust.util.UploadToQiNiu;
import org.swsd.stardust.view.activity.InfoSettingActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * author  ： 胡俊钦，林炜鸿
 * time    ： 2017/11/17
 * desc    ： 修改头像presenter
 * version ： 1.0
 */
public class SetAvatarPresenter {
    UserBean userBean = new UserBean();
    String responseData;
    int errorCode = 0;
    UploadToQiNiu upload = new UploadToQiNiu();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private Context mContext;

    private Handler uiHandler = new Handler() {
        // 覆写这个方法，接收并处理消息。
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 200:
                    Toast.makeText(mContext, "更换头像成功！", Toast.LENGTH_SHORT).show();
                    userBean.setAvatarPath(upload.url);
                    userBean.updateAll();
                    // 发送广播提醒设置页面修改成功
                    mContext.sendBroadcast(new Intent(InfoSettingActivity.ACTION_RELOAD));
                    break;
                case 403:
                    Toast.makeText(mContext, "头像路径太长，请换一张图片！", Toast.LENGTH_SHORT).show();
                    break;
                case 401:
                    Toast.makeText(mContext, "修改用户名失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(mContext, "修改用户名失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void afterChangeAvatar(final Context context, String imagePath) {
        mContext = context;
        upload.uploadQiNiu(imagePath);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (upload.url == null) {

                }
                if (upload.url == null) {
                    Toast.makeText(context, "上传头像失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                } else {
                    resetAvatar(upload.url);
                }
            }
        }).start();


    }

    // 将七牛云的头像链接发给服务器
    private void resetAvatar(final String imagePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    userBean = DataSupport.findLast(UserBean.class);
                    UpdateTokenUtil.updateUserToken(userBean);
                    while(!UpdateTokenUtil.refreshOk&&UpdateTokenUtil.isUpdate){

                    }
                    // 创建OkHttpClient实例
                    OkHttpClient client = new OkHttpClient();
                    // 将用户名设为Json格式
                    String json = getJsonString(imagePath);
                    RequestBody requestBody = RequestBody.create(JSON, json);
                    // 创建Request对象
                    Request request = new Request.Builder().
                            url("http://119.29.179.150:81/api/users/" + userBean.getUserId() + "/avatar")
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
    private static String getJsonString(String imagePath) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("avatar_url", imagePath);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }
}


