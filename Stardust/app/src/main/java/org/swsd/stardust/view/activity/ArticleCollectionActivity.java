package org.swsd.stardust.view.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.litepal.crud.DataSupport;
import org.swsd.stardust.R;
import org.swsd.stardust.base.BaseActivity;
import org.swsd.stardust.model.bean.ArticleBean;
import org.swsd.stardust.model.bean.ArticleCollectedBean;
import org.swsd.stardust.presenter.ArticlePresenter.ArticleAdapter;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;

/**
 * author  ： 林炜鸿
 * time    ： 2017/12/16
 * desc    ： 收藏文章的 Activity
 * version ： 1.0
 */
public class ArticleCollectionActivity extends BaseActivity {
    // TODO: 创建布局
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public RecyclerView recyclerView;
    public static List<ArticleBean> mArticleList;
    public static ArticleAdapter adapter;

    @Override
    public int bindLayout() {
        setContentView(R.layout.activity_article_collection);
        return 0;
    }

    @Override
    public void initView() {
        // 沉浸式顶部栏，继承基类的方法
        steepStatusBar();
    }

    @Override
    public void initData() {
        List<ArticleCollectedBean> tempList = DataSupport.findAll(ArticleCollectedBean.class);
        mArticleList = new ArrayList<>();
        for (ArticleCollectedBean acb : tempList) {
            mArticleList.add(new ArticleBean(acb));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化界面,实现沉浸式顶部栏
        initView();
        // 绑定并加载登录界面布局
        bindLayout();

        // 获取顶部状态栏的高度
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int stateBarHeight = resources.getDimensionPixelSize(resourceId);

        // 用空的TextView预留顶部状态栏高度

        TextView tvStateBar = (TextView) findViewById(R.id.tv_article_collection_statBar);
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

        initData();

        // 初始化收藏文章的 RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.article_collection_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ArticleAdapter(mArticleList);
        recyclerView.setAdapter(adapter);
    }

}
