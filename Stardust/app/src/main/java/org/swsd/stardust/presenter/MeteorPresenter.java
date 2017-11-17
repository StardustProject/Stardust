package org.swsd.stardust.presenter;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.swsd.stardust.model.bean.MeteorBean;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.util.ErrorCodeJudgment;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * author  ： 骆景钊
 * time    ： 2017/11/16
 * desc    ： 流星的presenter层
 * version ： 1.0
 */

public class MeteorPresenter implements IMeteorPresenter{

    @Override
    public List<MeteorBean> getMeteorList() {
        return DataSupport.findAll(MeteorBean.class);
    }

    @Override
    public void updataMeteor() {
        final UserBean userBean = DataSupport.findFirst(UserBean.class);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://www.cxpzz.com/learnlaravel5/public/index.php/api/users/aaa/meteors")
                            .addHeader("Authorizations","NWEwZDMzYmIzOTJkZjUuNTA2NzQ4ODQsMTIzNDU2Nzg5LDIwMTctMTEtMjMgMTQ6NDQ6MTE=")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    updateDatabase(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updateDatabase(String responseData){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(responseData);

            //检验是否成功
            if(ErrorCodeJudgment.errorCodeJudge(responseData) == "Ok"){
                JSONArray jsonArray = null;
                jsonArray = jsonObject.getJSONArray("meteors");
                JSONObject Meteor = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    Meteor = jsonArray.getJSONObject(i);
                    String userid = Meteor.getString("user_id");
                    String meteorContent = jsonObject.getString("content");
                    String url = jsonObject.getString("url");
                    MeteorBean meteorBean = new MeteorBean();
                    meteorBean.setNoteId(Integer.parseInt(userid));
                    meteorBean.setURL(url);
                    if(meteorContent == null){
                        meteorBean.setIsPureMedia(true);
                    }else {
                        meteorBean.setIsPureMedia(false);
                        meteorBean.setMeteorContent(meteorContent);
                    }
                    meteorBean.save();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
