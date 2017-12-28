package org.swsd.stardust.presenter.ArticlePresenter;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;
import org.swsd.stardust.model.bean.ArticleBean;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.presenter.UserPresenter;
import org.swsd.stardust.util.UpdateTokenUtil;
import org.swsd.stardust.view.fragment.ArticleFragment;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * author : 熊立强
 * time   : 2017/11/20
 * desc   : 获取文章Presenter层实例
 * version: 2.0
 */
public class ArticlePresenter implements IArticlePresenter{
    private static int ThreadId = 0;
    @Override
    public void refreshToken() {
        // 更新token
        UserBean userBean;
        UserPresenter userPresenter = new UserPresenter();
        userBean = userPresenter.toGetUserInfo();
        UpdateTokenUtil.updateUserToken(userBean);
    }

    private static final String TAG = "熊立强 ArticlePresenter";
    private static String ARTICLE_ID;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public List<ArticleBean> getArticleList() {
        return DataSupport.findAll(ArticleBean.class);
    }

    @Override
    public void getArticle(final UserBean userBean, final Activity mActivity) {
        refreshToken();
        //向服务器发送请求，并且存入数据库
        Thread thread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                //清空之前的数据库
                DataSupport.deleteAll(ArticleBean.class);
                Log.d(TAG, "文章数据库清空完成");
                Log.d(TAG, "userBean" + userBean.getToken());
                Log.d(TAG, "userBean" + userBean.getUserId());
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://119.29.179.150:81/api/users/" + userBean.getUserId() + "/articles?all_random=0")
                        .addHeader("Authorization", userBean.getToken())
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "run: Get response()" + responseData);
                    JSONObject jsonObject = new JSONObject(responseData);
                    if (jsonObject.getInt("error_code") == 200) {
                        Log.d(TAG, "error_code == 200");
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("articles"));
                        Log.d(TAG, "run: JsonArray is " + jsonArray);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject article = jsonArray.getJSONObject(i);
                            String id = article.getString("id");
                            String url = article.getString("url");
                            String status = article.getString("need_dedication");
                            Log.d(TAG, Thread.currentThread().getName()+"run: id is " + id);
                            Log.d(TAG, Thread.currentThread().getName()+"run: url is " + url);
                            Log.d(TAG, Thread.currentThread().getName()+"run: status is " + status);
                            ARTICLE_ID = id;
                            if (status == "true") {
                                // 需要发送的文章
                                putArticle(url, id);
                            } else {
                                // 需要解析的文章
                                parseArticle(url);
                            }
                            //回主线程更新UI
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ArticleFragment.adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                    // // TODO: 2017/11/18 解析json 获得url，根据url填充适配器
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
/*                    //更新数据库信息

                    updateDatabase(responseData);
                    MeteorFragment.meteorList.addAll(DataSupport.findAll(MeteorBean.class));

                    //回主线程更新UI
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MeteorFragment.meteorAdapter.notifyDataSetChanged();
                        }
                    });*/
        });
        thread.setName("线程" + ThreadId);
        ThreadId++;
        thread.start();
    }

    /**
     *  解析文章，不需要put形式
     * @param url
     */
    private void parseArticle(final String url){
        try {
            Document doc = Jsoup.connect(url).get();
            Log.d("熊立强", "parseArticle:  connect success");
            Elements elements = doc.select("h2[class=rich_media_title]");
            ArticleBean temp = new ArticleBean();
            temp.setArticleId(ARTICLE_ID);
            Log.d("熊立强", "title" + elements.text());
            temp.setTitle(elements.text());
            elements = doc.select("em[id=post-date]");
            Log.d("熊立强", "publishTime:" + elements.text());
            temp.setCreateTime(elements.text());
            elements = doc.select("span[class=rich_media_meta rich_media_meta_text rich_media_meta_nickname]");
            Log.d("熊立强", "author" + elements.text());
            temp.setAuthor(elements.text());
            Element content = doc.getElementById("js_content");
            elements = content.getElementsByTag("p");
            StringBuffer sb = new StringBuffer();
            for (int j = 5; j < elements.size(); j++){
                sb.append(elements.get(j).text());
                //Log.d("熊立强", "run: " + elements.get(i).text());
            }
            Log.d("熊立强", "content all ：" + sb.toString());
            temp.setContent(sb.toString());
            temp.setArticleUrl(url);
            Log.d(TAG, "getArticle is " + temp.getArticleUrl());
            Log.d(TAG, "getArticle is " + temp.getTitle());

            // 获取文章封面
            Element element = doc.getElementById("js_article");
            String coverUrl = "";
            Elements imgs = element.select("img[src]");
            Element e = imgs.get(3);
            coverUrl = e.attr("src");
            Log.d(TAG, "imgSRC" + coverUrl);
            temp.setArticleCover(coverUrl);
            //解析完成，保存数据库
            temp.save();
            ArticleFragment.mArticleList.add(temp);
            Log.d("熊立强", "parseArticle 解析文章完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void putArticle(final String url,final String articleId) {
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
            for (int j = 5; j < elements.size(); j++) {
                sb.append(elements.get(j).text());
                //Log.d("熊立强", "run: " + elements.get(i).text());
            }
            Log.d("熊立强", "content all ：" + sb.toString());
            putArticle.setContent(sb.toString());
            putArticle.setUrl(url);
            Gson gson = new Gson();
            String json = gson.toJson(putArticle);
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url("http://119.29.179.150:81/api/users/" + userBean.getUserId() + "/articles/" + articleId)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", userBean.getToken())
                    .put(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            Log.d(TAG, "putResponse +" + response.body().string());
            // 上传文章并保存
            ArticleBean temp = new ArticleBean();
            temp.setArticleId(ARTICLE_ID);
            temp.setContent(putArticle.getContent());
            temp.setTitle(putArticle.getTitle());
            temp.setArticleUrl(url);
            temp.setAuthor(putArticle.getAuthor());
            temp.setCreateTime(putArticle.getPublish_time());

            // 获取文章封面
            String coverUrl = "";
            Elements imgs = doc.select("img[src]");
            Element e = imgs.get(2);
            coverUrl = e.attr("src");
            Log.d(TAG, "imgSRC" + coverUrl);
            temp.save();
            ArticleFragment.mArticleList.add(temp);
            Log.d(TAG, "putArticle: 上传文章完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
