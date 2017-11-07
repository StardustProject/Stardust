package org.swsd.stardust.view.activity;

import android.os.Bundle;

import org.swsd.stardust.R;
import org.swsd.stardust.base.BaseActivity;

public class LoginActivity extends BaseActivity {

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    public void initView() {
        //沉浸式顶部栏
        steepStatusBar();
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化界面
        initView();
        //加载登录界面布局
        setContentView(R.layout.activity_login);
        }

    @Override
    protected void $log(String msg) {
        super.$log(msg);
    }
}
