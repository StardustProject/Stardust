package org.swsd.stardust.presenter;

import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
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
 * desc    ：
 * version ： 1.0
 */

public class SetNamePresenter {
    int errorCode=0;
    String responseData;
    UserPresenter userPresenter = new UserPresenter();
    UserBean userBean;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public boolean checkBeforeSetName(Context context,Editable name){
        boolean correct = false;
        CommonFunctions check = new CommonFunctions();

        // 点击按钮后,进行用户名长度检查
        switch (check.checkLength(name)){
            case 1:
                correct = false;
                Toast.makeText(context, "用户名不能为空！", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                correct = false;
                Toast.makeText(context, "用户名长度不能小于6！", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                correct = false;
                Toast.makeText(context, "用户名长度不能大于20！", Toast.LENGTH_SHORT).show();
                break;
            case 0:
                correct = true;
                break;
            default:
        }

        // 如果用户名长度合法
        if (correct == true) {
            // 检查用户名是否存在非法字符
            if (check.checkUsernameChar(name)) {
                correct = true;
            } else {
                correct = false;
                Toast.makeText(context, "用户名不允许出现非法字符！", Toast.LENGTH_SHORT).show();
            }
        }

        // 如果格式检查全部通过,更新服务器,更新数据库
        if (correct == true) {
            Log.i("hujunqin0:","begin");
            resetName(name.toString());
            while(errorCode==0){
                Log.i("hujunqin:","waiting");
            }
            if (errorCode == 200) {
                userBean = userPresenter.toGetUserInfo();
                userBean.setUserName(name.toString());
                userBean.updateAll("userName=?", name.toString());
                correct=true;
            }else if(errorCode==409){
                Toast.makeText(context, "用户名已存在！", Toast.LENGTH_SHORT).show();
                correct=false;
            }else if(errorCode==401){
                Toast.makeText(context, "修改用户名失败，请重试！", Toast.LENGTH_SHORT).show();
            }
        }
        return correct;
    }

    // 更新服务器
    public void resetName(final String strName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 创建OkHttpClient实例
                    OkHttpClient client = new OkHttpClient();
                    // 将用户名和密码设为Json格式
                    String json = getJsonString(strName);
                    RequestBody requestBody = RequestBody.create(JSON, json);
                    // 创建Request对象
                    String strId=String.valueOf(userBean.getId());
                    Request request = new Request.Builder().
                            url("http://111.231.18.37/learnlaravel5/public/index.php/api//users/"
                                    + strId + "/account")
                            .header("access_token", userBean.getToken())
                            .put(requestBody)
                            .build();
                    // 发送请求并获取服务器返回的数据
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.i("hujunqin1:","fail");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            responseData = response.body().toString();
                            Log.i("hujunqin1:",responseData);
                            try{
                                JSONObject jsonObject = new JSONObject(responseData);
                                errorCode = jsonObject.getInt("error_code");
                                String str=String.valueOf(errorCode);
                                Log.i("hujunqin2:",str);
                            }catch (JSONException e){
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
