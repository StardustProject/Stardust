package org.swsd.stardust.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import org.swsd.stardust.R;
import org.swsd.stardust.base.BaseActivity;

/**
 * author     :  胡俊钦
 * time       :  2017/11/07
 * description:  个人信息设置模块
 * version:   :  1.0
 */
public class InformationSettingActivity extends BaseActivity {

    @Override
    public int bindLayout() {
        setContentView(R.layout.activity_information_setting);
        return 0;
    }

    @Override
    public void initView() {
        //沉浸式顶部栏，继承基类的方法
        steepStatusBar();
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化界面,实现沉浸式顶部栏
        initView();
        //绑定并加载登录界面布局
        bindLayout();

        //设置“返回”图标监听事件
        ImageView ivGoBack =(ImageView) findViewById(R.id.iv_go_back);
        ivGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击按钮后回到到个人信息页面
                finish();
            }
        });
    }
}
