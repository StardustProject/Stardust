package org.swsd.stardust.view.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.zhuge.analysis.stat.ZhugeSDK;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.swsd.stardust.R;

import java.io.IOException;
import java.util.Date;

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
    private static String ARTICLE_ID;
    private FloatingActionButton fabLike;
    private Boolean isLiked = false;
    private String html;
    private static final int REMOVE_ADS = 1;
    private long startTime;
    private long endTime;
    /**
     *     按下返回键实现后退网页功能
     */
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REMOVE_ADS:
                    //prograssBar.setVisibility(View.GONE);
                    webView.loadDataWithBaseURL(null,html,"text/html","utf-8",null);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KEYCODE_BACK && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        else if(keyCode == KEYCODE_BACK){
            Date currentDate = new Date(System.currentTimeMillis());
            endTime = currentDate.getTime();
            Log.d(TAG, "onKeyDown: endTime:" + endTime);
            long readTime = endTime - startTime;
            Log.d(TAG, "onKeyDown: readTime" + readTime);
            // TODO: 2017/12/14 阅读时间上传服务器 
            return super.onKeyDown(keyCode,event);
        }
        else {
            return super.onKeyDown(keyCode,event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZhugeSDK.getInstance().flush(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView = (WebView)findViewById(R.id.web_view);
        prograssBar = (ProgressBar)findViewById(R.id.loading_progress);
        initBudle();
        fabLike = (FloatingActionButton) findViewById(R.id.fab_like);
        fabLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLiked){
                    fabLike.setImageResource(R.mipmap.like);
                    isLiked = false;
                }
                else{
                    fabLike.setImageResource(R.mipmap.like_fill);
                    isLiked = true;
                }
            }
        });
        //初始化分析跟踪
        ZhugeSDK.getInstance().init(getApplicationContext());
        //定义与事件相关的属性信息
        try {
            JSONObject eventObject = new JSONObject();
            eventObject.put("用户事件", "点击文章");
            eventObject.put("文章ID", ARTICLE_ID);
            eventObject.put("数量", 1);
            //记录事件,以购买为例
            ZhugeSDK.getInstance().track(getApplicationContext(), "浏览文章", eventObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //Toast.makeText(WebViewActivity.this, "加载中", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                Date currentDate = new Date(System.currentTimeMillis());
                startTime = currentDate.getTime();
                Log.d(TAG, "handleMessage: timeStart" + currentDate.getTime());
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
        //webView.loadUrl(url);
        webView.canGoBack();
        webView.canGoForward();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBlockNetworkImage(false);
        //不可滑动
        webView.setFitsSystemWindows(true);
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
        ARTICLE_ID = bundle.getString("articleID");
        removeADs(url);
        Log.d(TAG, "initBudle: url is " + url);
        Log.d(TAG, "initBudle: id is " + ARTICLE_ID);
    }

    private void removeADs(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(url).get();
                    Element element = doc.getElementById("js_article");
                    html = element.html();
                    html = formateHtml(html);
                    Log.d(TAG, "removeADs: " + html);
                    Message msg = new Message();
                    msg.what = REMOVE_ADS;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public String formateHtml(String htmlContent) {
        Document doc_Dis = Jsoup.parse(htmlContent);
        Elements ele_Img = doc_Dis.getElementsByTag("img");
        // 相当于标签htmlData = htmlData.replace("<img", "<img style='max-width:90%;height:auto;'");
        if (ele_Img.size() != 0) {
            for (Element e_Img : ele_Img) {
                e_Img.attr("style", "max-width:100%");
                e_Img.attr("height", "auto");
            }
        }
        Elements ele_p = doc_Dis.getElementsByTag("p");
        if(ele_p.size() != 0){
            for (Element e_p : ele_p){
                e_p.attr("text-align","center");
            }
        }

        Elements ele_a = doc_Dis.getElementsByTag("a");
        if(ele_a.size() != 0){
            for (Element e_a : ele_a){
                e_a.remove();//删除标签
                //e_a.removeAttr("href");
            }
        }
        return doc_Dis.toString();
    }
}