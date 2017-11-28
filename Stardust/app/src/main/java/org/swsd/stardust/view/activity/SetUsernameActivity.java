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
import org.swsd.stardust.presenter.SetNamePresenter;
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

        // 获取顶部状态栏的高度
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int stateBarHeight = resources.getDimensionPixelSize(resourceId);

        // 用空的TextView预留顶部状态栏高度
        TextView tvStateBar = (TextView) findViewById(R.id.tv_setUserName_stateBar);
        android.view.ViewGroup.LayoutParams setHeight = tvStateBar.getLayoutParams();
        setHeight.height = stateBarHeight;
        tvStateBar.setLayoutParams(setHeight);

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
                SetNamePresenter namePresenter = new SetNamePresenter();
                namePresenter.checkBeforeSetName(getApplicationContext(), etSetName.getText());
                finish();
            }
        });
    }
}
