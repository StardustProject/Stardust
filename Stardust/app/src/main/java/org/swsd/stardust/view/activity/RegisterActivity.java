package org.swsd.stardust.view.activity;

import android.os.Bundle;

import org.swsd.stardust.R;
import org.swsd.stardust.base.BaseActivity;

/**
 * author     :  胡俊钦
 * time       :  2017/11/08
 * description:  注册模块
 * version:   :  1.0
 */
public class RegisterActivity extends BaseActivity {

    @Override
    public int bindLayout() {
        //加载布局
        setContentView(R.layout.activity_register);
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


    }
}
