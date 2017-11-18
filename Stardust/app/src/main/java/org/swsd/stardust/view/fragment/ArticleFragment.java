package org.swsd.stardust.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.swsd.stardust.R;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.presenter.ArticlePresenter.Article;
import org.swsd.stardust.presenter.ArticlePresenter.ArticleAdapter;
import org.swsd.stardust.presenter.ArticlePresenter.JsoupWeiXin;
import org.swsd.stardust.presenter.UserPresenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *     author : 熊立强
 *     time : 2017/11/11
 *     description : 文章类碎片
 *     version : 1.0
 */
public class ArticleFragment extends Fragment {
    private static final String TAG = "熊立强：ArticleFragment";
    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.article_recycler_view);
        refreshRv();
        return view;

    }

    private void refreshRv(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        ArticleAdapter adapter = new ArticleAdapter(getTestAdapter());
        recyclerView.setAdapter(adapter);
        getArticle();
    }
    /**
     *  获得测试测试的适配器
     * @return List类型的文章列表
     */
    List<Article> getTestAdapter(){
        List<Article> list = new ArrayList<>();
        for (int i = 0; i < 30 ; i++) {
            String articleUrl = "https://mp.weixin.qq.com/s/zfRFqZmk-AVtZ9BdQkllPA";
            Article article = new Article();
            JsoupWeiXin.parseContent(articleUrl);
            article.setArticleTitle(JsoupWeiXin.title);
            article.setArticleAuthor(JsoupWeiXin.author);
            article.setArtilePublishTime(JsoupWeiXin.publishTime);
            article.setArticleAbstract(JsoupWeiXin.contentArticle);
            article.setArticleUrl(articleUrl);
            list.add(article);
        }
        return list;
    }


    // 使用Gson解析json格式的数据
    private void parseJSONWithGSON(String jsonData){
        Gson gson = new Gson();
        List<Article> articleList = gson.fromJson(jsonData, new TypeToken<List<Article>>(){}.getType());
        for(Article article : articleList){
            Log.d(TAG, "name is  " + article.getArticleUrl());
        }
    }

    /**
     * 获取文章
     */
    private void getArticle(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取当前用户id
                UserBean userBean;
                UserPresenter userPresenter = new UserPresenter();
                userBean = userPresenter.toGetUserInfo();
                Log.d(TAG, "userBean" + userBean.getToken());
                Log.d(TAG, "userBean" + userBean.getUserId());
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "run: Get response()" + responseData);
                    // // TODO: 2017/11/18 解析json 获得url，根据url填充适配器
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();


    }

    private void putArticle(){
        // TODO: 2017/11/18  上传url
    }
}
