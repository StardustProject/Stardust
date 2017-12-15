package org.swsd.stardust.view.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;

import org.swsd.stardust.R;
import org.swsd.stardust.base.BaseActivity;
import org.swsd.stardust.model.bean.MeteorBean;
import org.swsd.stardust.presenter.SetLikeMeteorPresenter;
import org.swsd.stardust.presenter.SetReportMeteorPresenter;
import org.swsd.stardust.util.LoadingUtil;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author     :  骆景钊
 * time       :  2017/11/12
 * description:  流星具体详情
 * version:   :  1.0
 */

public class MeteorDetail extends BaseActivity{
    WebView meteorDetail;
    ImageView backImageView;
    TextView informTextView;
    MeteorBean meteor;
    String responseData;
    Dialog mDialog;
    AlertDialog.Builder dialog;
    LikeButton likeButton;
    Boolean isLikeMeteor;
    Boolean isLike;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meteor_detail);

        // 获取顶部状态栏的高度
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int stateBarHeight = resources.getDimensionPixelSize(resourceId);

        // 用空的TextView预留顶部状态栏高度
        TextView tvStateBar = (TextView) findViewById(R.id.tv_meteor_detail_stateBar);
        android.view.ViewGroup.LayoutParams setHeight = tvStateBar.getLayoutParams();
        setHeight.height = stateBarHeight;
        tvStateBar.setLayoutParams(setHeight);

        meteorDetail = (WebView) findViewById(R.id.wv_MeteorDetail_Message);

        //遮罩处理
        mDialog = LoadingUtil.createLoadingDialog(this,"加载中...");

        //url解析
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        meteor = (MeteorBean) bundle.getSerializable("Meteor");
        Log.d("luojingzhao",meteor.getURL());
        meteorDetail.getSettings().setJavaScriptEnabled(true);
        meteorDetail.setWebViewClient(new WebViewClient());
        sendRequestWithOkHttp(meteor.getURL());

        //举报响应
        dialog = new AlertDialog.Builder(this);
        informTextView = (TextView) findViewById(R.id.tv_MeteorDetail_inform);
        informTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("luojingzhao","onclick");
                dialog.setMessage("确定举报该内容吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SetReportMeteorPresenter setReportMeteorPresenter = new SetReportMeteorPresenter();
                        setReportMeteorPresenter.reportMeteor(meteor);
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            }
        });

        //点赞监听
        isLikeMeteor = false;
        likeButton = (LikeButton) findViewById(R.id.star_button);
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked() {
                isLikeMeteor = true;
            }

            @Override
            public void unLiked() {
                if(isLike){
                    likeButton.setLiked(true);
                    isLikeMeteor = true;
                }else{
                    isLikeMeteor = false;
                }

            }
        });

        //是否已经点赞判断
        isLike = meteor.getIsLike();
        if(isLike){
            likeButton.setLiked(true);
            isLike = true;
            isLikeMeteor = true;
        }

        backImageView = (ImageView) findViewById(R.id.iv_MeteorDetail_back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLikeMeteor && !isLike){
                    SetLikeMeteorPresenter setLikeMeteorPresenter = new SetLikeMeteorPresenter();
                    setLikeMeteorPresenter.updataMeteor(meteor);
                    MeteorBean meteorBean = new MeteorBean();
                    meteorBean.setIsLike(true);
                    meteorBean.updateAll("meteorId = ?", String.valueOf(meteor.getMeteorId()));
                }
                finish();
            }
        });
    }

    private void sendRequestWithOkHttp(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Response response = client.newCall(request).execute();
                    responseData = response.body().string();
//                    responseData = "<img src=\"http://ozcxh8wzm.bkt.clouddn.com/FkDqZ4HMKkQD0YxR2Zbo9jTkyvOv\" alt=\"dachshund\">";
                    showResponse();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void showResponse(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                meteorDetail.loadDataWithBaseURL(null,responseData,"text/html", "utf-8",null);
                meteorDetail.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

                //加载遮罩消除
                LoadingUtil.closeDialog(mDialog);
            }
        });
    }

    @Override
    public int bindLayout() {
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

}
