package org.swsd.stardust.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.swsd.stardust.R;
import org.swsd.stardust.base.BaseActivity;

/**
 * author     :  骆景钊
 * time       :  2017/11/12
 * description:  流星具体详情
 * version:   :  1.0
 */
public class MeteorDetail extends BaseActivity {
    TextView meteorDetail;
    ImageView backImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meteor_detail);
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
