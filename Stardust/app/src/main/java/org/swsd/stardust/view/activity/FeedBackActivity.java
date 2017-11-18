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
import org.swsd.stardust.util.SendEmail;

/**
 * author  ： 胡俊钦
 * time    ： 2017/11/18
 * desc    ： 用户反馈功能
 * version ： 1.0
 */
public class FeedBackActivity extends AppCompatActivity {
    EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feed_back);
        Toolbar toolbar = (Toolbar) findViewById(R.id.feedback_toolbar);
        setSupportActionBar(toolbar);
        // 设置“返回”图标监听事件
        ImageView ivBack = (ImageView) findViewById(R.id.iv_return);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击按钮后回到个人信息设置页面
                finish();
            }
        });

        final Button btnSend = (Button) findViewById(R.id.btn_send);
        etMessage = (EditText) findViewById(R.id.et_editFeedBack);
        // 设置编辑框监听
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 编辑框内容改变前
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 编辑框内容改变时，设置“发送”按钮可按
                if (s.length() > 0) {
                    btnSend.setEnabled(true);
                    btnSend.setBackgroundColor(getResources().getColor(R.color.green));
                } else {
                    btnSend.setEnabled(false);
                    btnSend.setBackgroundColor(getResources().getColor(R.color.gray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //编辑框内容改变后
            }
        });

        // 设置“发送”按钮监听事件
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail("848804259@qq.com");
                Toast.makeText(FeedBackActivity.this, "发送邮件中，请不要进行任何操作", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 发送邮件
    private void sendEmail(final String eMailAddress) {
        // 启用一个线程 发送邮件
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SendEmail.send(etMessage.getText().toString(), eMailAddress);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(FeedBackActivity.this, "邮件发送成功！", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        }).start();
    }
}
