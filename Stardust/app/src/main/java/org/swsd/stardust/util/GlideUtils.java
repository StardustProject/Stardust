package org.swsd.stardust.util;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.orhanobut.hawk.Hawk;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author  ： 胡俊钦
 * time    ： 2017/11/18
 * desc    ： glide加载七牛云图片
 * version ： 1.0
 */
public class GlideUtils {
     //加载七牛时用本分隔符
    public static String slipTag = "it";

    public  void loadImage(final Context context, final String url, final CircleImageView imageView) {

        if (url != null) {
            String[] urls = url.split(slipTag);
            if (urls.length > 1) {
                if (Hawk.get(getCacheUrl(url)) == null) {
                    //用图片URL固定的部分获取第一次缓存图片时的全URL
                    Hawk.put(getCacheUrl(url), url);
                    //如果没有缓存过，将第图片第一次加载时的URL缓存起来
                    //第一次加载用全路径从网络上加载
                    Glide.with(context)
                            .load(url)
                            .into(imageView);

                } else {
                    //第二次以后通过 url 加载本地缓存图片
                    // 设置加载失败监听，假如由于缓存过期或
                    // 者被清等客观原因导致加载失败，从最新的url 加载，并更新本地缓存
                    Glide.with(context)
                            .load(String.valueOf(Hawk.get(getCacheUrl(url))))
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model,
                                                           Target<GlideDrawable> target, boolean isFirstResource) {
                                    Glide.with(context)
                                            .load(url)
                                            .into(imageView);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model,
                                                               Target<GlideDrawable> target, boolean isFromMemoryCache,
                                                               boolean isFirstResource) {
                                    //更新缓存
                                    Hawk.put(getCacheUrl(url), url);
                                    return false;
                                }
                            })
                            .into(imageView);
                }
            } else {
                //如果非动态URL 则直接加载即可
                Glide.with(context)
                        .load(url)
                        .into(imageView);
            }
        }
    }


    public static String getCacheUrl(String url) {
        if (url != null) {
            String[] urls = url.split(slipTag);
            if (urls.length > 0) {
                return urls[0];
            } else {
                return "";
            }
        } else {
            return "";
        }
    }
}