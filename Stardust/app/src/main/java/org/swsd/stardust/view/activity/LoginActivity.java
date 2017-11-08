package org.swsd.stardust.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.swsd.stardust.R;
import org.swsd.stardust.base.BaseActivity;

public class LoginActivity extends BaseActivity {

    @Override
    public int bindLayout() {
        //加载布局
        setContentView(R.layout.activity_login);
        return 0;
    }

    @Override
    public void initView() {
        //沉浸式顶部栏，继承基类的方法
        //steepStatusBar();
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

        //设置“登录”按钮监听事件
        Button btn_login=(Button)findViewById(R.id.btn_login_login);
        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //点击按钮后……
               //逻辑待实现
            }
        });

        //设置“注册”按钮监听事件
        Button btn_register=(Button)findViewById(R.id.btn_login_register);
        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //点击按钮后跳转到注册页面
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void $log(String msg) {
        super.$log(msg);
    }
}
