package org.swsd.stardust.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import org.swsd.stardust.R;
import org.swsd.stardust.base.BaseActivity;
import org.swsd.stardust.model.bean.MeteorBean;

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
        meteorDetail.loadUrl(meteor.getURL());
        backImageView = (ImageView) findViewById(R.id.iv_MeteorDetail_back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
