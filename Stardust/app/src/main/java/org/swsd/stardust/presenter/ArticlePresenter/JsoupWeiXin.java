package org.swsd.stardust.presenter.ArticlePresenter;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * author : 熊立强
 * time   : 2017/11/17
 * desc   :
 * version: 1.0
 */
public class JsoupWeiXin {
    public static String contentArticle;
    public static String title;
    public static String publishTime;
    public static String author;
    public static String htmlContent;
    public static void parseContent(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String sourceUrl = url;
                    Document doc = Jsoup.connect(sourceUrl).get();
                    Elements elements = doc.select("h2[class=rich_media_title]");
                    Log.d("熊立强", "title" + elements.text());
                    title = elements.text();
                    elements = doc.select("em[id=post-date]");
                    Log.d("熊立强", "publishTime:" + elements.text());
                    publishTime = elements.text();
                    elements = doc.select("span[class=rich_media_meta rich_media_meta_text rich_media_meta_nickname]");
                    Log.d("熊立强", "author" + elements.text());
                    author = elements.text();
                    Element content = doc.getElementById("js_content");
                    elements = content.getElementsByTag("p");
                    //String contentAll = "";
                    StringBuffer sb = new StringBuffer();
/*                    for (Element temp : elements) {
                        contentAll += temp.text();
                        Log.d("熊立强", "run: " + temp.text());
                    }*/
                    for (int i = 5; i < elements.size(); i++){
                        sb.append(elements.get(i).text());
                        //Log.d("熊立强", "run: " + elements.get(i).text());
                    }
                    Log.d("熊立强", "content all ：" + sb.toString());
                    contentArticle = sb.toString();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void parseHtml(final String htmlCode){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = Jsoup.parse(htmlCode);
                Elements elements = doc.getAllElements();
                StringBuffer sb = new StringBuffer();
/*                for (Element element: elements) {
                    sb.append(element.text());
                }*/
                Log.d("熊立强", "run: " + doc.body().text());
                htmlContent = doc.body().text();
            }
        }).start();
    }
}
