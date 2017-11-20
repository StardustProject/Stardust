package org.swsd.stardust.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.swsd.stardust.R;
import org.swsd.stardust.model.bean.ArticleBean;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.presenter.ArticlePresenter.Article;
import org.swsd.stardust.presenter.ArticlePresenter.ArticleAdapter;
import org.swsd.stardust.presenter.ArticlePresenter.ArticlePresenter;
import org.swsd.stardust.presenter.ArticlePresenter.JsoupWeiXin;
import org.swsd.stardust.presenter.UserPresenter;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;

/**
 *     author : 熊立强
 *     time : 2017/11/11
 *     description : 文章类碎片
 *     version : 1.0
 */
public class ArticleFragment extends Fragment {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "熊立强：ArticleFragment";
    public RecyclerView recyclerView;
    public static List<ArticleBean> mArticleList = new ArrayList<>();
    private static boolean isNotFirst;
    public static ArticleAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.article_recycler_view);

        //流星更新
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("lanchtime", Context.MODE_PRIVATE);
        long lastLanchTime = sharedPreferences.getLong("isfisttoday", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        isNotFirst = (0 != lastLanchTime);

        // 获取当前用户id
        UserBean userBean;
        UserPresenter userPresenter = new UserPresenter();
        userBean = userPresenter.toGetUserInfo();
        ArticlePresenter presenter = new ArticlePresenter();
        if (isNotFirst) {
            long now = System.currentTimeMillis();
            //超过时间更新
            int day = (int) ((now - lastLanchTime)/(24*60*60*1000));
            if (day > 0) {
                mArticleList.clear();
                Log.d(TAG, "onCreateView: + 超过时间" );
                presenter.getArticle(userBean,getActivity());
                editor.putLong("isfisttoday", now);
                editor.commit();
            }
        }else{
            //第一次点击更新
            mArticleList.clear();
            presenter.getArticle(userBean,getActivity());
            editor.putLong("isfisttoday", System.currentTimeMillis());
            editor.commit();
            Log.d(TAG,"back");
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mArticleList = presenter.getArticleList();
        adapter = new ArticleAdapter(mArticleList);
        for(ArticleBean article:mArticleList){
            Log.d(TAG, "Article id is " + article.getArticleId());
        }
        recyclerView.setAdapter(adapter);
        return view;

    }

    private void refreshRv(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //mArticleList = presenter.getArticleList();
        recyclerView.setAdapter(adapter);
    }
    /**
     *  获得测试测试的适配器
     * @return List类型的文章列表
     */
    List<Article> getTestAdapter(List<String> urls){
        List<Article> list = new ArrayList<>();
        for (String url: urls) {
            String articleUrl = url;
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

}