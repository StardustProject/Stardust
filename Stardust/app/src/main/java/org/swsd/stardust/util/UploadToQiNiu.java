package org.swsd.stardust.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

/**
 * author  ： 胡俊钦
 * time    ： 2017/11/18
 * desc    ： 上传图片到七牛云
 * version ： 1.0
 */
public class UploadToQiNiu {
    public String url;
    public boolean uploadFinished=false;
    // 上传头像到七牛云
    public void uploadQiNiu(final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String localFilePath = path;
                String key = null;
                // 七牛机房设置，构造一个带指定Zone对象的配置类
                Configuration cfg = new Configuration(Zone.zone0());
                UploadManager uploadManager = new UploadManager(cfg);
                // 生成上传凭证，然后准备上传

                String accessKey = "zV8eZYiEVp-Akvx_wGlUtfhjB3VmDd4mvl9u-s6O";
                String secretKey = "ij9yPsXMxznSeifiblhNTVpiGAvnhhhsXS4gdenc";
                String bucket = "thousfeet";

                //魔法变量设置回调格式
                Auth auth = Auth.create(accessKey, secretKey);
                StringMap putPolicy = new StringMap();
                putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
                long expireSeconds = 3600;
                String upToken = auth.uploadToken(bucket, null, expireSeconds, putPolicy);
                try {
                    Response response = uploadManager.put(localFilePath, key, upToken);
                    //解析上传成功的结果
                    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                    url = "http://oziec3aec.bkt.clouddn.com/" + putRet.key;
                } catch (QiniuException ex) {
                    Response r = ex.response;
                    System.err.println(r.toString());
                    try {
                        System.err.println(r.bodyString());
                    } catch (QiniuException ex2) {
                        //ignore
                    }
                }
                uploadFinished=true;
            }
        }).start();
    }
}
