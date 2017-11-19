package org.swsd.stardust.view.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.swsd.stardust.R;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 *     author : 熊立强
 *     time :  2017/11/17
 *     description : 查看文章界面
 *     version : 1.0
 */
public class WebViewActivity extends AppCompatActivity {

    private static final String TAG = "熊立强";
    private WebView webView;
    private String url = "https://www.baidu.com/";
    private ProgressBar prograssBar;
    private int resourceCount = 0;

    /**
     *     按下返回键实现后退网页功能
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KEYCODE_BACK && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        else {
            return super.onKeyDown(keyCode,event);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView = (WebView)findViewById(R.id.web_view);
        initBudle();
        prograssBar = (ProgressBar)findViewById(R.id.loading_progress);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //Toast.makeText(WebViewActivity.this, "开始加载", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                //Toast.makeText(WebViewActivity.this, "结束加载" + resourceCount, Toast.LENGTH_SHORT).show();
            }
            // 作用：在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次。
            @Override
            public void onLoadResource(WebView view, String url) {
                resourceCount++;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                /*view.loadUrl(url_2);*/
                return true;
            }

        });
        // String
        webView.loadUrl(url);
        webView.canGoBack();
        webView.canGoForward();

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                //Toast.makeText(WebViewActivity.this, title, Toast.LENGTH_SHORT).show();
            }
            // 读取进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress <= 100) {
                    prograssBar.setProgress(newProgress);
                    if (newProgress == 100){
                        prograssBar.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    /**
     *  清除内存
     */
    private void clean(){
        webView.clearCache(true);
        webView.clearHistory();
        webView.clearFormData();
    }

    /**
     * 初始化数据
     */
    private void initBudle(){
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        url = bundle.getString("url");
        Log.d(TAG, "initBudle: url is " + url);
    }
}