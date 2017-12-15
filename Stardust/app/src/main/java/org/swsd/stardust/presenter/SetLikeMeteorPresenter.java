package org.swsd.stardust.presenter;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.swsd.stardust.model.bean.MeteorBean;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.util.ErrorCodeJudgment;
import org.swsd.stardust.util.UpdateTokenUtil;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * author     :  骆景钊
 * time       :  2017/12/15
 * description:  流星点赞更新
 * version:   :  1.0
 */

public class SetLikeMeteorPresenter {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public void updataMeteor(final MeteorBean meteorBean) {

        final UserBean userBean = DataSupport.findFirst(UserBean.class);

        UpdateTokenUtil.updateUserToken(userBean);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //流星点赞数上传服务器
                    OkHttpClient client = new OkHttpClient();
                    String json = getJsonString(String.valueOf(userBean.getUserId()));
                    RequestBody requestBody = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url("http://119.29.179.150:81/api/users/"+userBean.getUserId()+"/meteors/"
                            + meteorBean.getMeteorId() + "/upvotation" )
                            .addHeader("Content-Type","application/json")
                            .addHeader("Authorization",userBean.getToken())
                            .put(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("luojingzhao",responseData);

                    //更新数据库信息
                    updateDatabase(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //处理返回的json格式的流星信息，并保存到数据库
    private void updateDatabase(String responseData){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(responseData);
            Log.d("luojingzhao",responseData);

            //检验是否成功
            if(ErrorCodeJudgment.errorCodeJudge(responseData) == "Ok"){
                jsonObject = new JSONObject(responseData);
                String error = jsonObject.getString("error_code");
                Log.d("luojingzhao",error);
                JSONArray jsonArray = null;
                jsonArray = jsonObject.getJSONArray("meteor");
                JSONObject Meteor = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    Meteor = jsonArray.getJSONObject(i);
                    String meteorId = Meteor.getString("id");
                    String meteorUpvoteQuantity = Meteor.getString("upvote_quantity");
                    MeteorBean meteorBean = new MeteorBean();
                    meteorBean.setUpvoteQuantity(Integer.valueOf(meteorUpvoteQuantity));
                    meteorBean.updateAll("meteorId = ?", meteorId);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
