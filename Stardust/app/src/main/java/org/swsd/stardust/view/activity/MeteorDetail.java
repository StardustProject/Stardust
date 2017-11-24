package org.swsd.stardust.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import org.swsd.stardust.R;
import org.swsd.stardust.base.BaseActivity;
import org.swsd.stardust.model.bean.MeteorBean;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author     :  骆景钊
 * time       :  2017/11/12
 * description:  流星具体详情
 * version:   :  1.0
 */
public class MeteorDetail extends BaseActivity {
    WebView meteorDetail;
    ImageView backImageView;
    MeteorBean meteor;
    String responseData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meteor_detail);
        meteorDetail = (WebView) findViewById(R.id.wv_MeteorDetail_Message);
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        meteor = (MeteorBean) bundle.getSerializable("Meteor");
        Log.d("luojingzhao",meteor.getURL());
        meteorDetail.getSettings().setJavaScriptEnabled(true);
        meteorDetail.setWebViewClient(new WebViewClient());
        sendRequestWithOkHttp(meteor.getURL());
        backImageView = (ImageView) findViewById(R.id.iv_MeteorDetail_back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void sendRequestWithOkHttp(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Response response = client.newCall(request).execute();
                    responseData = response.body().string();
//                    responseData = "<img src=\"http://ozcxh8wzm.bkt.clouddn.com/FkDqZ4HMKkQD0YxR2Zbo9jTkyvOv\" alt=\"dachshund\">";
                    showResponse();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void showResponse(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                meteorDetail.loadDataWithBaseURL(null,responseData,"text/html", "utf-8",null);
                meteorDetail.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            }
        });
    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
    }
}
