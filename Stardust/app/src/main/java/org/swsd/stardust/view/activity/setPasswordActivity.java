package org.swsd.stardust.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.swsd.stardust.R;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.presenter.ButtonNavigationBarPresenter.tools.CommonFunctions;
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

        // 从数据库获取用户名并显示在页面上
        UserBean userBean;
        UserPresenter userPresenter = new UserPresenter();
        userBean = userPresenter.toGetUserInfo();
        TextView tvUsername = (TextView) findViewById(R.id.tv_username);
        tvUsername.setText(userBean.getUserName().toString());

        final Button btnFinish = (Button) findViewById(R.id.btn_finish);

        // 设置编辑框监听
        EditText etOldPassword = (EditText) findViewById(R.id.et_oldPassword);
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
                btnFinish.setEnabled(true);
                btnFinish.setBackgroundColor(getResources().getColor(R.color.green));

            }

            @Override
            public void afterTextChanged(Editable s) {
                //编辑框内容改变后
            }
        });

        // 设置“保存”按钮监听事件
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean correct = false;
                CommonFunctions check = new CommonFunctions();
                // 点击按钮后

               /* // 判断旧密码是否正确
                if(正确){

                }else{
                    Toast.makeText(SetPasswordActivity.this, "您输入的旧密码不正确！", Toast.LENGTH_SHORT).show();
                }
                */
                // 判断密码长度是否符合
                switch (check.checkLength(etNewPassword.getText())) {
                    case 1:
                        correct = false;
                        Toast.makeText(setPasswordActivity.this, "新密码不能为空！", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        correct = false;
                        Toast.makeText(setPasswordActivity.this, "新密码长度不能小于6！", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        correct = false;
                        Toast.makeText(setPasswordActivity.this, "新密码长度不能大于20！", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        correct = true;
                        break;
                    default:
                }

                // 如果用密码长度合法
                if (correct == true) {
                    // 检查密码是否存在非法字符
                    if (check.checkUsernameChar(etNewPassword.getText())) {
                        correct = true;
                    } else {
                        correct = false;
                        Toast.makeText(setPasswordActivity.this, "密码不允许出现非法字符！", Toast.LENGTH_SHORT).show();
                    }
                }

                // 如果格式检查全部通过
                if (correct == true) {
                    // 判断确认密码与新密码是否相同
                    if (etNewPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                        correct = true;
                    } else {
                        correct = false;
                        Toast.makeText(setPasswordActivity.this, "新密码与确认密码不一致！", Toast.LENGTH_SHORT).show();
                    }
                }

                // 如果检查全部通过，(更新服务器，缺)
                if (correct == true) {

                    finish();
                }
            }
        });
    }
}
