package org.swsd.stardust.presenter.ArticlePresenter;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 *     author : 熊立强
 *     time   : 2017/11/12
 *     desc   :
 *     version: 1.0
 */
public class HttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
