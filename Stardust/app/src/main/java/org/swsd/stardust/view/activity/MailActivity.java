package org.swsd.stardust.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.litepal.crud.DataSupport;
import org.swsd.stardust.R;
import org.swsd.stardust.base.BaseActivity;
import org.swsd.stardust.model.bean.MailBean;
import org.swsd.stardust.presenter.MailPresenter;
import org.swsd.stardust.presenter.adapter.MailAdapter;
import org.swsd.stardust.util.LoadingUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author  ： 林炜鸿
 * time    ： 2017/12/13
 * desc    ： 显示站内信的Activity
 * version ： 1.0
 */
public class MailActivity extends BaseActivity {
    public static final String ACTION_RELOAD = "reload";
    private List<MailBean> mailBeanList = new ArrayList<>();
    private BroadcastReceiver bcReload = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mailBeanList = DataSupport.findAll(MailBean.class);
            recyclerView.setAdapter(new MailAdapter(mailBeanList));
        }
    };
    private RecyclerView recyclerView;
    
    @Override
    public int bindLayout() {
        // 加载布局
        setContentView(R.layout.activity_mail);
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
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(bcReload);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化界面,实现沉浸式顶部栏
        initView();
        // 绑定并加载登录界面布局
        bindLayout();
        // 获取顶部状态栏的高度
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int stateBarHeight = resources.getDimensionPixelSize(resourceId);
        // 注册刷新页面的广播
        registerReceiver(bcReload, new IntentFilter(ACTION_RELOAD));

        // 用空的TextView预留顶部状态栏高度
        TextView tvStateBar = (TextView) findViewById(R.id.tv_mail_statBar);
        android.view.ViewGroup.LayoutParams setHeight = tvStateBar.getLayoutParams();
        setHeight.height = stateBarHeight;
        tvStateBar.setLayoutParams(setHeight);

        // 设置“返回”图标监听事件
        ImageView ivGoBack = (ImageView) findViewById(R.id.iv_go_back);
        ivGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击按钮后回到到个人信息页面
                finish();
            }
        });

        // 重新从服务器加载数据
        getData();

        // 初始化 RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.mail_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MailAdapter(mailBeanList));
    }


    private void getData() {
        MailPresenter mailPresenter = new MailPresenter();
        // 获取最新消息
        // 设置加载遮罩
        LoadingUtil.createLoadingDialog(this, "");
        mailPresenter.toGetMail(this, MailPresenter.ALL_MAIL);
        return;
    }
}
