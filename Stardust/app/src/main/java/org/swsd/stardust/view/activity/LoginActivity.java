package org.swsd.stardust.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.swsd.stardust.R;
import org.swsd.stardust.base.BaseActivity;

/**
 * author     :  胡俊钦
 * time       :  2017/11/07
 * description:  登录模块
 * version:   :  1.0
 */
public class LoginActivity extends BaseActivity {

    // 存放用户名输入框内容的变量
    private String mStrUsername = "";
    // 存放密码输入框内容的变量
    private String mStrPassword = "";

    @Override
    public int bindLayout() {
        // 加载布局
        setContentView(R.layout.activity_login);
        return 0;
    }

    @Override
    public void initView() {
        // 沉浸式顶部栏，继承基类的方法
        steepStatusBar();
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化界面,实现沉浸式顶部栏
        initView();
        // 绑定并加载登录界面布局
        bindLayout();

        // 设置“用户名”编辑框监听对象
        final EditText etUsername = (EditText) findViewById(R.id.et_login_username);
        final TextView tvUsername = (TextView) findViewById(R.id.tv_login_name_rule);
        // 设置“密码”编辑框监听对象
        final EditText etPassword = (EditText) findViewById(R.id.et_login_password);
        final TextView tvPassword = (TextView) findViewById(R.id.tv_login_passorrd_rule);

        // 当编辑框获得焦点时，显示用户名格式的提示信息，失去焦点则隐藏提示信息
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

        // 当编辑框获得焦点时，显示密码格式的提示信息，失去焦点则隐藏提示信息
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

        // 设置“登录”按钮监听事件
        Button btnLogin = (Button) findViewById(R.id.btn_login_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击按钮后跳转到主页面
                if (checkLength(etUsername.getText()) && checkLength(etPassword.getText())
                        && checkUsernameChar(etUsername.getText()) && checkPasswordChar(etPassword.getText())) {
                    mStrUsername = etUsername.getText().toString();
                    mStrPassword = etPassword.getText().toString();
                    Intent goToMain = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(goToMain);
                    finish();
                }

            }
        });

        // 设置“注册”按钮监听事件
        Button btnRegister = (Button) findViewById(R.id.btn_login_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击按钮后跳转到注册页面
                Intent goToRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(goToRegister);
            }
        });

    }

    @Override
    protected void $log(String msg) {
        super.$log(msg);
    }

    // 判断长度
    public boolean checkLength(Editable editable) {
        boolean flag = false;
        // 统计字符数，汉字算一个字符
        int length = editable.length();
        // 统计字符长度，汉字算两个字符长度
        int count = 0;
        for (int i = 0; i < length; i++) {
            if (editable.charAt(i) < 256) {
                count++;
            } else {
                count += 2;
            }
        }

        if (count == 0) {
            Toast.makeText(LoginActivity.this, "用户名和密码不能为空！", Toast.LENGTH_SHORT).show();
        } else if (count < 6) {
            Toast.makeText(LoginActivity.this, "用户名和密码长度不能小于6！", Toast.LENGTH_SHORT).show();
        } else if (count > 20) {
            Toast.makeText(LoginActivity.this, "用户名和密码长度不能大于20！", Toast.LENGTH_SHORT).show();
        } else {
            flag = true;
        }

        return flag;
    }

    // 判断用户名字符是否合法
    public boolean checkUsernameChar(Editable editable) {
        boolean flag = false;
        // 统计字符数，汉字算一个字符
        int length = editable.length();
        char c;
        int i;
        for (i = 0; i < length; i++) {
            c = editable.charAt(i);
            if ((c <= '9' && c >= '0') || (c <= 'z' && c >= 'a') || (c <= 'A' && c >= 'Z')) {
                continue;

            } else {
                Toast.makeText(LoginActivity.this, "用户名不允许出现非法字符！", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        if (i == length) {
            flag = true;
        }
        return flag;
    }

    // 判断密码字符是否合法，缺标点符号的判断
    //…………………………………………………………
    //………………………………………………………………
    public boolean checkPasswordChar(Editable editable) {
        boolean flag = false;
        //统计字符数，汉字算一个字符
        int length = editable.length();
        char c;
        int i;
        for (i = 0; i < length; i++) {
            c = editable.charAt(i);
            if ((c <= '9' && c >= '0') || (c <= 'z' && c >= 'a') || (c <= 'A' && c >= 'Z')) {
                continue;

            } else {
                Toast.makeText(LoginActivity.this, "密码不允许出现非法字符！", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        if (i == length) {
            flag = true;
        }
        return flag;
    }
}
