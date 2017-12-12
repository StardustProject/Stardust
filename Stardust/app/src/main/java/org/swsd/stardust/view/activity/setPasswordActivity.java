package org.swsd.stardust.view.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.swsd.stardust.R;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.presenter.SetPswPresenter;
import org.swsd.stardust.presenter.UserPresenter;

/**
 * author     :  胡俊钦
 * time       :  2017/11/15
 * description:  修改用户密码
 * version:   :  1.0
 */
public class setPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.pwdToolbar);
        setSupportActionBar(toolbar);

        // 获取顶部状态栏的高度
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int stateBarHeight = resources.getDimensionPixelSize(resourceId);

        // 用空的TextView预留顶部状态栏高度
        TextView tvStateBar = (TextView) findViewById(R.id.tv_setPassword_stateBar);
        android.view.ViewGroup.LayoutParams setHeight = tvStateBar.getLayoutParams();
        setHeight.height = stateBarHeight;
        tvStateBar.setLayoutParams(setHeight);

        // 从数据库获取用户名并显示在页面上
        UserBean userBean;
        UserPresenter userPresenter = new UserPresenter();
        userBean = userPresenter.toGetUserInfo();
        TextView tvUsername = (TextView) findViewById(R.id.tv_username);
        tvUsername.setText(userBean.getUserName().toString());

        final Button btnFinish = (Button) findViewById(R.id.btn_finish);

        // 设置编辑框监听
        final EditText etOldPassword = (EditText) findViewById(R.id.et_oldPassword);
        final EditText etNewPassword = (EditText) findViewById(R.id.et_newPassword);
        final EditText etConfirmPassword = (EditText) findViewById(R.id.et_confirmPassword);
        etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 编辑框内容改变前
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 编辑框内容改变时，设置“完成”按钮可按
                if(s.length()>0){
                    btnFinish.setEnabled(true);
                    btnFinish.setBackgroundColor(getResources().getColor(R.color.green));
                }else{
                    btnFinish.setEnabled(false);
                    btnFinish.setBackgroundColor(getResources().getColor(R.color.gray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //编辑框内容改变后
            }
        });

        // 设置“返回”图标监听
        ImageView ivGoBack = (ImageView) findViewById(R.id.iv_setPassword_back);
        ivGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 设置“完成”按钮监听事件
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetPswPresenter namePresenter = new SetPswPresenter();
                namePresenter.checkBeforeSetPsw(getApplicationContext(),
                        etOldPassword.getText(), etNewPassword.getText(), etConfirmPassword.getText());
                finish();
            }
        });
    }
}
