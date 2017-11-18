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

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.swsd.stardust.R;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.presenter.ArticlePresenter.Article;
import org.swsd.stardust.presenter.ArticlePresenter.ArticleAdapter;
import org.swsd.stardust.presenter.ArticlePresenter.JsoupWeiXin;
import org.swsd.stardust.presenter.ArticlePresenter.PutArticle;
import org.swsd.stardust.presenter.UserPresenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *     author : 熊立强
 *     time : 2017/11/11
 *     description : 文章类碎片
 *     version : 1.0
 */
public class ArticleFragment extends Fragment {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "熊立强：ArticleFragment";
    private RecyclerView recyclerView;
    private static List<Article> mArticleList = new ArrayList<>();
    List<String> articleUrls = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.article_recycler_view);
        getArticle();
        while (true){
            refreshRv();
            if (mArticleList.size() == 10) break;
        }
        return view;

    }

    private void refreshRv(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        ArticleAdapter adapter = new ArticleAdapter(mArticleList);
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
                        .url("http://119.29.179.150:81/api/users/"+ userBean.getUserId()+"/articles?all_random=1")
                        .addHeader("Authorization",userBean.getToken())
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "run: Get response()" + responseData);
                    JSONObject jsonObject = new JSONObject(responseData);
                    if(jsonObject.getInt("error_code") == 200){
                        Log.d(TAG, "error_code == 200");
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("articles"));
                        Log.d(TAG, "run: JsonArray is " + jsonArray);
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject article = jsonArray.getJSONObject(i);
                            String id = article.getString("id");
                            String url = article.getString("url");
                            String status = article.getString("need_dedication");
                            Log.d(TAG, "run: id is " + id);
                            Log.d(TAG, "run: url is "+url);
                            Log.d(TAG, "run: status is " + status);

                            if(status == "true"){
                                // 需要发送的文章
                                putArticle(url,id);
                            }
                            else{
                                // 需要解析的文章
                                parseArticle(url);
                            }
                        }
                    }
                    // // TODO: 2017/11/18 解析json 获得url，根据url填充适配器
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }).start();


    }

    /**
     *  解析文章，不需要put形式
     * @param url
     */
    private void parseArticle(final String url){
            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("h2[class=rich_media_title]");
                Article ListItem = new Article();
                Log.d("熊立强", "title" + elements.text());
                ListItem.setArticleTitle(elements.text());
                elements = doc.select("em[id=post-date]");
                Log.d("熊立强", "publishTime:" + elements.text());
                ListItem.setArtilePublishTime(elements.text());
                elements = doc.select("span[class=rich_media_meta rich_media_meta_text rich_media_meta_nickname]");
                Log.d("熊立强", "author" + elements.text());
                ListItem.setArticleAuthor(elements.text());
                Element content = doc.getElementById("js_content");
                elements = content.getElementsByTag("p");
                StringBuffer sb = new StringBuffer();
                for (int j = 5; j < elements.size(); j++){
                    sb.append(elements.get(j).text());
                    //Log.d("熊立强", "run: " + elements.get(i).text());
                }
                Log.d("熊立强", "content all ：" + sb.toString());
                ListItem.setArticleAbstract(sb.toString());
                ListItem.setArticleUrl(url);
                Log.d(TAG, "getArticle is " + ListItem.getArticleUrl());
                Log.d(TAG, "getArticle is " + ListItem.getArticleTitle());
                if(mArticleList.size() < 10){
                    mArticleList.add(ListItem);
                }
                Log.d(TAG, "parseArticle: List size is " + mArticleList.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private void putArticle(final String url,final String articleId){
        // TODO: 2017/11/18  上传url
            // 获取当前用户id
            UserBean userBean;
            UserPresenter userPresenter = new UserPresenter();
            userBean = userPresenter.toGetUserInfo();
            Log.d(TAG, "userBean" + userBean.getToken());
            Log.d(TAG, "userBean" + userBean.getUserId());
            // 解析url
            try {
                Document doc = Jsoup.connect(url).get();

                PutArticle putArticle = new PutArticle();
                Elements elements = doc.select("h2[class=rich_media_title]");
                Log.d("熊立强", "title" + elements.text());
                putArticle.setTitle(elements.text());
                elements = doc.select("em[id=post-date]");
                Log.d("熊立强", "publishTime:" + elements.text());
                putArticle.setPublish_time(elements.text());
                elements = doc.select("span[class=rich_media_meta rich_media_meta_text rich_media_meta_nickname]");
                Log.d("熊立强", "author" + elements.text());
                putArticle.setAuthor(elements.text());
                Element content = doc.getElementById("js_content");
                elements = content.getElementsByTag("p");
                StringBuffer sb = new StringBuffer();
                for (int j = 5; j < elements.size(); j++){
                sb.append(elements.get(j).text());
                //Log.d("熊立强", "run: " + elements.get(i).text());
                }
                Log.d("熊立强", "content all ：" + sb.toString());
                putArticle.setContent(sb.toString());
                putArticle.setUrl(url);
                Gson gson = new Gson();
                String json = gson.toJson(putArticle);
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = RequestBody.create(JSON,json);
                Request request = new Request.Builder()
                        .url("http://119.29.179.150:81/api/users/"+ userBean.getUserId()+"/articles/" + articleId)
                        .addHeader("Content-Type","application/json")
                        .addHeader("Authorization",userBean.getToken())
                        .put(requestBody)
                        .build();
                Response response = client.newCall(request).execute();
                Log.d(TAG, "putResponse +" + response.body().string());
                Article temp  = new Article();
                temp.setArticleAbstract(putArticle.getContent());
                temp.setArticleTitle(putArticle.getTitle());
                temp.setArticleUrl(url);
                temp.setArticleAuthor(putArticle.getAuthor());
                temp.setArtilePublishTime(putArticle.getPublish_time());
                if(mArticleList.size() < 10){
                    mArticleList.add(temp);
                }
                Log.d(TAG, "List size " + mArticleList.size());
            } catch (IOException e) {
                e.printStackTrace();
            }

    }



}
