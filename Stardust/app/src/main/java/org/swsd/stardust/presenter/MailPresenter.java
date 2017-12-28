package org.swsd.stardust.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.swsd.stardust.model.bean.MailBean;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.util.LoadingUtil;
import org.swsd.stardust.util.UpdateTokenUtil;
import org.swsd.stardust.view.activity.InfoSettingActivity;
import org.swsd.stardust.view.activity.MailActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author  ： 林炜鸿
 * time    ： 2017/12/14
 * desc    ： 获取站内信的presenter
 * version ： 1.0
 */
public class MailPresenter {
    private UserPresenter userPresenter;
    private UserBean userBean;

    Context mContext;

    // 决定获取全部消息还是未读消息
    private int action;
    public static final int ALL_MAIL = 1;
    public static final int LATEST_MAIL = 0;

    private JSONObject jsResponse;

    // 获取消息
    public void toGetMail(Context mContext, int action) {
        userPresenter = new UserPresenter();
        userBean = userPresenter.toGetUserInfo();
        this.mContext = mContext;
        this.action = action;
        sendRequestWithOkHttp();
    }

    // 向服务器发送获取消息的请求
    private void sendRequestWithOkHttp() {
        // 刷新用户 Token
        UpdateTokenUtil.updateUserToken(userBean);
        final Handler uiHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 200:
                        // 获取成功，刷新数据库
                        updateDatabase();
                        Log.d("MailPresenter@uiHandler", "Success");
                        mContext.sendBroadcast(new Intent(MailActivity.ACTION_RELOAD));
                        break;
                    case 401:
                        Toast.makeText(mContext, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(mContext, "未知错误，请稍后再试", Toast.LENGTH_SHORT).show();
                        break;
                }
                // 关闭加载遮罩
                LoadingUtil.closeDialog();
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    // 获取被举报和点赞的流星的消息
                    Request request = new Request.Builder()
                            .url("http://119.29.179.150:81/api/users/" + userBean.getUserId() + "/messages"
                                    + "?all=" + action)
                            .addHeader("Authorization", userBean.getToken())
                            .build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            // 将 Request Body 转为 json
                            try {
                                jsResponse = new JSONObject(response.body().string());
                                Message msg = new Message();
                                msg.what = jsResponse.getInt("error_code");
                                uiHandler.sendMessage(msg);
                            } catch(JSONException e) {
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

    // 服务器返回以后更新数据库
    private void updateDatabase() {
        try{
            JSONArray jsArray = jsResponse.getJSONArray("messages");
            for (int i = 0; i < jsArray.length(); i++) {
                JSONObject jsMail = jsArray.getJSONObject(i);
                MailBean mailBean = new MailBean();
                mailBean.setId(jsMail.getInt("id"));
                mailBean.setSender("From: System");
                mailBean.setContent(praseContent(jsMail));
                mailBean.setCreateTime(jsMail.getString("create_time"));
                // 当只获取最新消息或者消息未被存过，则存入数据库
                if (action == LATEST_MAIL || !mailBean.isSaved()) {
                    mailBean.save();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String praseContent(final JSONObject jsMail) {
        try{
            switch (jsMail.getString("type")) {
                case "meteor_reported":
                    return "Σ(っ°Д°;)っ\n"+"您的一颗流星(编号 "+ jsMail.getInt("note_id") +
                            " )因涉及非法信息已被多人举报。请咨询管理员以获得更多信息。";
                case "meteor_upvoted":
                    return "(〃'▽'〃)\n"+"您的一颗流星(编号 "+ jsMail.getInt("note_id") +
                            " )被点赞了一次";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
