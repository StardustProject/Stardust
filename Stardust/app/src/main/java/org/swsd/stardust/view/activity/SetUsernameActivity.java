package org.swsd.stardust.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.swsd.stardust.R;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.presenter.ButtonNavigationBarPresenter.tools.CommonFunctions;
import org.swsd.stardust.presenter.UserPresenter;

/**
 * author     :  胡俊钦
 * time       :  2017/11/15
 * description:  修改用户名
 * version:   :  1.0
 */
public class SetUsernameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_username);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 设置“返回”图标监听事件
        ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击按钮后回到个人信息设置页面
                finish();
            }
        });

        final Button btnSave = (Button) findViewById(R.id.btn_save);
        final EditText etSetName = (EditText) findViewById(R.id.et_setName);
        final UserBean userBean;
        UserPresenter userPresenter = new UserPresenter();
        userBean = userPresenter.toGetUserInfo();

        // 在编辑框内显示用户名
        final String name = userBean.getUserName();
        etSetName.setText(name);
        etSetName.setSelection(etSetName.getText().length());

        // 设置编辑框监听
        etSetName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 编辑框内容改变前
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 编辑框内容改变时，设置“保存”按钮可按
                btnSave.setEnabled(true);
                btnSave.setBackgroundColor(getResources().getColor(R.color.green));

            }

            @Override
            public void afterTextChanged(Editable s) {
                //编辑框内容改变后
            }
        });

        // 设置“保存”按钮监听事件
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean correct = false;
                CommonFunctions check = new CommonFunctions();

                // 点击按钮后,进行用户名长度检查
                switch (check.checkLength(etSetName.getText())) {
                    case 1:
                        correct = false;
                        Toast.makeText(SetUsernameActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        correct = false;
                        Toast.makeText(SetUsernameActivity.this, "用户名长度不能小于6！", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        correct = false;
                        Toast.makeText(SetUsernameActivity.this, "用户名长度不能大于20！", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        correct = true;
                        break;
                    default:
                }

                // 如果用户名长度合法
                if (correct == true) {
                    // 检查用户名是否存在非法字符
                    if (check.checkUsernameChar(etSetName.getText())) {
                        correct = true;
                    } else {
                        correct = false;
                        Toast.makeText(SetUsernameActivity.this, "用户名不允许出现非法字符！", Toast.LENGTH_SHORT).show();
                    }
                }

                // 如果格式检查全部通过，(更新服务器，缺)，更新数据库
                if (correct == true) {
                    userBean.setUserName(etSetName.getText().toString());
                    userBean.updateAll("userName=?", name);
                    finish();
                }
            }
        });
    }
}
