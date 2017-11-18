package org.swsd.stardust.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.swsd.stardust.model.bean.MeteorBean;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.presenter.adapter.MeteorAdapter;
import org.swsd.stardust.util.ErrorCodeJudgment;
import org.swsd.stardust.view.activity.MainActivity;
import org.swsd.stardust.view.fragment.MeteorFragment;

import java.util.List;
import java.util.logging.Handler;

import okhttp3.MediaType;
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
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public List<MeteorBean> getMeteorList() {
        return DataSupport.findAll(MeteorBean.class);
    }

    @Override
    public void updataMeteor(final UserBean userBean, final Activity mActivity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("luojingzhao","thead1");
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://111.231.18.37/learnlaravel5/public/index.php/api/users/"+userBean.getUserId()+"/meteors")
                            .addHeader("Authorizations",userBean.getToken())
                            .build();
                    Log.d("luojingzhao","success");
                    Response response = client.newCall(request).execute();
                    Log.d("luojingzhao","3");
                    String responseData = response.body().string();
                    Log.d("luojingzhao",responseData);
                    DataSupport.deleteAll(MeteorBean.class);
                    updateDatabase(responseData);
//                    Message message = new Message();
//                    message.what = 1001;
                    MeteorFragment.meteorList.addAll(DataSupport.findAll(MeteorBean.class));
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mActivity, "toshi", Toast.LENGTH_SHORT).show();
                            MeteorFragment.meteorAdapter.notifyDataSetChanged();
                        }
                    });


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
            Log.d("luojingzhao",responseData);

            //检验是否成功
            if(ErrorCodeJudgment.errorCodeJudge(responseData) == "Ok"){
                jsonObject = new JSONObject(responseData);
                String error = jsonObject.getString("error_code");
                Log.d("luojingzhao",error);
                JSONArray jsonArray = null;
                jsonArray = jsonObject.getJSONArray("meteors");
//                Log.d("luojingzhao",jsonArray.length()+"");
                JSONObject Meteor = null;
                for (int i = 0; i < jsonArray.length(); i++) {
//                    Log.d("luojingzhao",i+"");
                    Meteor = jsonArray.getJSONObject(i);
                    String meteorContent = Meteor.getString("content");
                    String url = Meteor.getString("url");
//                    Log.d("luojingzhao",meteorContent);
//                    Log.d("luojingzhao",url);
                    MeteorBean meteorBean = new MeteorBean();
                    meteorBean.setURL(url);
                    if(meteorContent == null){
                        meteorBean.setIsPureMedia(true);
                    }else {
                        meteorBean.setIsPureMedia(false);
                        meteorBean.setMeteorContent(meteorContent);
                    }
                    meteorBean.save();
//                    Log.d("luojingzhao",meteorBean.getURL());
                }
                Log.d("luojingzhao","the end");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
