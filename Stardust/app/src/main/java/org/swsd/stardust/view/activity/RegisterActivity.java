package org.swsd.stardust.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.swsd.stardust.R;
import org.swsd.stardust.base.BaseActivity;
import org.swsd.stardust.presenter.RegisterPresenter;

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

        // 设置“用户名”编辑框监听对象
        final EditText etUsername = (EditText) findViewById(R.id.et_register_username);
        final TextView tvUsername = (TextView) findViewById(R.id.tv_register_name_rule);
        // 设置“输入密码”编辑框监听对象
        final EditText etPassword = (EditText) findViewById(R.id.et_register_password);
        final TextView tvPassword = (TextView) findViewById(R.id.tv_register_password_rule);
        //设置“确认密码”编辑框对象
        final EditText etConfirmPassword = (EditText) findViewById(R.id.et_register_confirmPassword);

        // 当用户名编辑框获得焦点时，显示用户名格式的提示信息，失去焦点则隐藏提示信息
        etUsername.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点，显示提示信息
                    tvUsername.setVisibility(View.VISIBLE);
                } else {
                    // 失去焦点，显示提示信息
                    tvUsername.setVisibility(View.GONE);
                }
            }
        });

        // 当输入密码编辑框获得焦点时，显示密码格式的提示信息，失去焦点则隐藏提示信息
        etPassword.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点，显示提示信息
                    tvPassword.setVisibility(View.VISIBLE);
                } else {
                    // 失去焦点，隐藏提示信息
                    tvPassword.setVisibility(View.GONE);
                }
            }
        });

        // 设置“注册”按钮监听事件
        Button btnRegister = (Button) findViewById(R.id.btn_register_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击按钮后检查网络状态
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isAvailable()) {
                    //若当前无网络则提醒用户
                    Toast.makeText(RegisterActivity.this, "network is unavailable", Toast.LENGTH_SHORT).show();
                } else {
                    // 若网络可用，则检查用户名密码长度及格式
                    RegisterPresenter register = new RegisterPresenter();
                    switch (register.checkBeforeRegister(getApplicationContext(), etUsername.getText(),
                            etPassword.getText(), etConfirmPassword.getText())) {
                        case 1:
                            // 注册成功则跳转到登录页面
                            Intent goToRegister = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(goToRegister);
                            finish();
                            break;
                        case 2:
                            Toast.makeText(RegisterActivity.this, "确认密码与输入密码不一致！", Toast.LENGTH_SHORT).show();
                            break;
                        case 0:
                            break;
                        default:

                    }

                }
            }
        });
    }
}
