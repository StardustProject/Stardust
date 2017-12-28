package org.swsd.stardust.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhuge.analysis.stat.ZhugeSDK;

import org.swsd.stardust.R;
import org.swsd.stardust.base.BaseActivity;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.presenter.LoginPresenter;
import org.swsd.stardust.presenter.UserPresenter;
import org.swsd.stardust.util.LoadingUtil;
import org.swsd.stardust.util.LoginActivityJudgment;
import org.swsd.stardust.util.UpdateTokenUtil;
import org.swsd.stardust.view.guideActivity.GuideActivity;

/**
 * author     :  胡俊钦，林炜鸿
 * time       :  2017/11/07
 * description:  登录模块
 * version:   :  1.0
 */
public class LoginActivity extends BaseActivity {

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

        SharedPreferences sharedPreferences = this.getSharedPreferences("Guide", Context.MODE_PRIVATE);
        int firstUsed = sharedPreferences.getInt("isFirst", -1);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(firstUsed == -1){
            editor.putInt("isFirst", 1);
            editor.commit();
            Intent goToGuide = new Intent(getApplicationContext(), GuideActivity.class);
            startActivity(goToGuide);
            finish();
        }

        //判断是否需要登录
        if(!LoginActivityJudgment.loginActivityJudgment()){
            // 更新用户token
            UserBean userBean = (new UserPresenter()).toGetUserInfo();
            UpdateTokenUtil.updateUserToken(userBean);
            Intent goToMain = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(goToMain);
            finish();
        }

        super.onCreate(savedInstanceState);
        //初始化分析跟踪
        ZhugeSDK.getInstance().init(getApplicationContext());
        // 初始化界面,实现沉浸式顶部栏
        initView();
        // 绑定并加载登录界面布局
        bindLayout();

        // 设置“用户名”编辑框监听对象
        final EditText etUsername = (EditText) findViewById(R.id.et_login_username);
        // 设置“密码”编辑框监听对象
        final EditText etPassword = (EditText) findViewById(R.id.et_login_password);

        // 设置“登录”按钮监听事件
        Button btnLogin = (Button) findViewById(R.id.btn_login_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击按钮后
                // 检查网络状态
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isAvailable()) {
                    //若当前无网络则提醒用户
                    Toast.makeText(LoginActivity.this, "network is unavailable", Toast.LENGTH_SHORT).show();
                } else {
                    // 若网络可用,进行格式检查
                    LoginPresenter login = new LoginPresenter();
                    // 设置加载遮罩
                    LoadingUtil.createLoadingDialog(LoginActivity.this,"登录中...");
                    login.checkBeforeLogin(getApplicationContext(), etUsername.getText(), etPassword.getText());
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
}
