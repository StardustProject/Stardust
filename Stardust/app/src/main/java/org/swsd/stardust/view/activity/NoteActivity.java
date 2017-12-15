package org.swsd.stardust.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.mr5.icarus.Callback;
import com.github.mr5.icarus.Icarus;
import com.github.mr5.icarus.TextViewToolbar;
import com.github.mr5.icarus.entity.Options;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.zhuge.analysis.stat.ZhugeSDK;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;
import org.swsd.stardust.R;
import org.swsd.stardust.model.bean.NoteBean;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.presenter.NotePresenter.Note;
import org.swsd.stardust.presenter.NotePresenter.NotePresenter;
import org.swsd.stardust.presenter.NotePresenter.putNote;
import org.swsd.stardust.presenter.UserPresenter;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * author : 熊立强
 * time : 2017/11/16
 * description : 记录模块
 * version : 1.0
 */
public class NoteActivity extends AppCompatActivity {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final int SAVE_NOTE = 1;
    public static final int DELETE_NOTE = 2;
    public static final int SHARE_NOTE = 3;
    public static final int CANCEL_SHARE_NOTE = 4;
    public static final int UPLOADED_IMAGE = 5;
    public static final int UPLOADED_AUDIO = 6;
    private boolean isEdited = false;
    private boolean isEmpty = true;
    private static final String TAG = "熊立强";
    private static final int CHOOSE_PHOTO = 2;
    private String imagePath;
    private static String URL = null;
    private static int NoteId;
    private static int UserId;
    private static long createTime;
    private static boolean isNew = false;
    private static boolean isShare = false;
    private static NoteBean noteTemp;
    private static String NOTE_ID;
    private android.support.v7.widget.Toolbar toolbar;
    private WebView webView;
    protected Icarus icarus;
    private String imageUrl;
    private String audioUrl;
    private String htmlContent;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SAVE_NOTE:
                    Log.d(TAG, "handleMessage: 保存成功");
                    break;
                case DELETE_NOTE:
                    Log.d(TAG, "handleMessage: " + "删除成功");
                    finish();
                    break;
                case SHARE_NOTE:
                    // TODO: 2017/12/12 分享成功修改状态 变成取消分享状态
                    Log.d(TAG, "handleMessage: ");
                    break;
                case CANCEL_SHARE_NOTE:
                    // TODO: 2017/12/12 取消分享成功桩体,变成分享按钮
                    break;
                case UPLOADED_IMAGE:
                    String imagTag =
                            "<img alt=\"图片加载中\" src=\""+imageUrl+"\">\n" +
                            "<br>";
                    icarus.insertHtml(imagTag);
                    //icarus.insertHtml("<img alt=\"图片加载中\" src="+imageUrl+">");
                    //icarus.insertHtml("<a>hhhhhh</a>");
                    break;
                case UPLOADED_AUDIO:
                    String audioTag =
                            "<audio controls=\"controls\" src=\""+audioUrl+"\">\n" +
                                    "您的浏览器不支持 audio 标签。\n" +
                                    "</audio>";
                    icarus.insertHtml(audioTag);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        createTime = new Date().getTime();
        toolbar = (Toolbar) findViewById(R.id.note_toolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.go_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Save 正在保存");
                if (!isEmpty) {
                    if (isNew) {
                        //定义与事件相关的属性信息
                        try {
                            JSONObject eventObject = new JSONObject();
                            eventObject.put("用户事件", "新建记录");
                            eventObject.put("数量", 1);
                            //记录事件,以购买为例
                            ZhugeSDK.getInstance().track(getApplicationContext(), "新建记录", eventObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        saveNote();
                    } else {
                        updateNote();
                    }
                } else {
                    Toast.makeText(NoteActivity.this, "未输入文字不保存", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

        webView = (WebView) findViewById(R.id.editor);
        // I offered a toolbar to manage editor buttons which implements TextView that with icon fonts.
        // It's just a collection, not an Android View implementation.
        // TextViewToolbar will listen click events on all buttons that added to it.
        // You can implement your own `Toolbar`, to prevent these default behaviors.
        TextViewToolbar textViewToolbar = new TextViewToolbar();
        Options options = new Options();
        options.setPlaceholder("请输入文字");
        icarus = new Icarus(textViewToolbar, options, webView);
        options.addAllowedAttributes("a", Arrays.asList("class", "src", "alt", "data-type"));
        options.addAllowedAttributes("body", Arrays.asList("src"));
        /*TextView boldButton = new TextViewButton();
        boldButton.setName(Button.NAME_BOLD);
        textViewToolbar.addButton(boldButton);*/
        icarus.render();
        initBundle();
        final ImageView insertImage = (ImageView)findViewById(R.id.action_insert_image);
        insertImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSystemImage();
            }
        });
    }

    /**
     * 填充toolbar菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        MenuItem share = menu.findItem(R.id.note_share);
        MenuItem cancelShare = menu.findItem(R.id.note_cancel_share);
        if (!isShare) {
            share.setVisible(true);
            cancelShare.setVisible(false);
        } else {
            share.setVisible(false);
            cancelShare.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.note_share: {
                Log.d(TAG, "Share 正在分享");
                icarus.getContent(new Callback() {
                    @Override
                    public void run(String params) {
                        Log.d(TAG, "content:" + params);
                        if(params.length() == 14){
                            isEmpty = true;
                        }
                        else{
                            htmlContent = params;
                            htmlContent = formatContent(htmlContent);
                            isEmpty = false;
                        }
                    }
                });
                if (!isEmpty) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(NoteActivity.this);
                    dialog.setTitle("确定将此记录匿名分享为流星吗？");
                    dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //定义与事件相关的属性信息
                            try {
                                JSONObject eventObject = new JSONObject();
                                eventObject.put("用户事件", "分享");
                                eventObject.put("数量", 1);
                                //记录事件,以购买为例
                                ZhugeSDK.getInstance().track(getApplicationContext(), "用户分享", eventObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // TODO: 2017/12/14  传入html字符串
                            shareHtml(htmlContent);
                            isShare = true;
                            invalidateOptionsMenu();
                        }
                    });
                    dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(this, "请输入文字", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            case R.id.note_save: {
                    if (icarus == null) {
                        return true;
                    }
                icarus.getContent(new Callback() {
                    @Override
                    public void run(String params) {
                        Log.d(TAG, "content:" + params);
                        if(params.length() == 14){
                            isEmpty = true;
                        }
                        else{
                            htmlContent = params;
                            htmlContent = formatContent(htmlContent);
                            Log.d(TAG, "记录内容是" + htmlContent);
                            isEmpty = false;
                        }
                        Log.d(TAG, "Save 正在保存");
                        if (!isEmpty) {
                            if (isNew) {
                                //定义与事件相关的属性信息
                                try {
                                    JSONObject eventObject = new JSONObject();
                                    eventObject.put("用户事件", "新建记录");
                                    eventObject.put("数量", 1);
                                    //记录事件,以购买为例
                                    ZhugeSDK.getInstance().track(getApplicationContext(), "新建记录", eventObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                saveNote();
                            } else {
                                updateNote();
                            }
                        } else {
                            Toast.makeText(NoteActivity.this, "请输入文字", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                return true;
            }
            case R.id.note_delete: {
                AlertDialog.Builder dialog = new AlertDialog.Builder(NoteActivity.this);
                dialog.setTitle("是否删除?");
                dialog.setMessage("删除将无法恢复，请谨慎操作！");
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!isNew) {
                            deleteNote();
                        } else {
                            finish();
                        }
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
                return  true;
            }
            case R.id.note_cancel_share: {

                // 2017/12/12 取消分享按钮功能 日记已经在服务器有状态，直接修改即可
                AlertDialog.Builder dialog = new AlertDialog.Builder(NoteActivity.this);
                dialog.setTitle("确定取消分享此记录吗？");
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //定义与事件相关的属性信息
                        try {
                            JSONObject eventObject = new JSONObject();
                            eventObject.put("用户事件", "取消分享");
                            eventObject.put("数量", 1);
                            //记录事件,以购买为例
                            ZhugeSDK.getInstance().track(getApplicationContext(), "用户取消分享", eventObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        cancelShareNote(URL);
                        isShare = false;
                        invalidateOptionsMenu();
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
                item.setVisible(false);
                /* Menu menu = null;
                getMenuInflater().inflate(R.menu.menu_note, menu);
                MenuItem menuItem = menu.findItem(R.id.note_share);
                menuItem.setVisible(true);*/
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 保存笔记函数
     */
    private void saveNote() {
        // 2017/11/16 保存内容到本地，上传七牛云，上传服务器url
        // TODO: 2017/12/14  传入html 
        String htmlCode = htmlContent;
        Log.d(TAG, "saveNote: " + htmlCode);
        // 上传html  上传html到七牛云和服务器在上传完之后保存本地数据库，之后关闭Activity
        uploadHtml(htmlCode);
    }

    /**
     * 获取文件的路径
     *
     * @return
     */
    private String getSystemImage() {
        //  2017/11/16  调用系统相册返回路径
        //查看权限
        if (ContextCompat.checkSelfPermission(NoteActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NoteActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbum();
        }
        Log.d(TAG, "getSystemImage: " + imagePath);
        return imagePath;

    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        //打开相册
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    /**
     * 权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.
                        PERMISSION_GRANTED) {
                    // 获取权限，跳转选择照片
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_PHOTO:
                //判断手机系统版本号
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        //手机系统在4.4及以上的才能使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        //手机系统在4.4以下的使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }


    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        Uri uri = data.getData();
        //如果是document类型的Uri，，则通过document id处理
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //解析出数字格式的id
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:" +
                        "//downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri，直接获取图片路径
            imagePath = uri.getPath();
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri，使用普通方式处理
            imagePath = getImagePath(uri, null);
        }
        Log.d("熊立强", "6.0 handler" + imagePath);
        uploadQiniu(imagePath);
        //根据图片路径显示图片
        //displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        Log.d("熊立强", "before 6.0 handler" + imagePath);
        uploadQiniu(imagePath);
        //根据图片路径显示图片
        //displayImage(imagePath);
    }

    /**
     * 根据Uri获取，文件路径
     *
     * @param uri
     * @param selection
     * @return
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
            }
        }
        return path;
    }

    /**
     * 根据路径上传七牛云
     *
     * @param path
     */
    private String uploadQiniu(final String path) {
        NotePresenter notePresenter = new NotePresenter();
        notePresenter.refreshToken();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String localFilePath = path;
                String key = null;
                // 七牛机房设置，构造一个带指定Zone对象的配置类
                Configuration cfg = new Configuration(Zone.zone0());
                UploadManager uploadManager = new UploadManager(cfg);
                //...生成上传凭证，然后准备上传

                String accessKey = "zV8eZYiEVp-Akvx_wGlUtfhjB3VmDd4mvl9u-s6O";
                String secretKey = "ij9yPsXMxznSeifiblhNTVpiGAvnhhhsXS4gdenc";
                String bucket = "thousfeet";
                //普通上传
                /*Auth auth = Auth.create(accessKey, secretKey);
                String upToken = auth.uploadToken(bucket);*/

                //魔法变量设置回调格式
                Auth auth = Auth.create(accessKey, secretKey);
                StringMap putPolicy = new StringMap();
                putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
                long expireSeconds = 3600;
                String upToken = auth.uploadToken(bucket, null, expireSeconds, putPolicy);
                Log.d(TAG, "Token is " + upToken);
                try {
                    Response response = uploadManager.put(localFilePath, key, upToken);
                    //解析上传成功的结果
                    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                    Log.d(TAG, "成功上传" + putRet.key);
                    Log.d(TAG, "成功上传" + putRet.hash);
                    imageUrl = "http://ozcxh8wzm.bkt.clouddn.com/" + putRet.key;
                    Log.d(TAG, "url is " + imageUrl);
                    // 上传过后的url需要插入编辑器
                    Message msg = new Message();
                    msg.what = UPLOADED_IMAGE;

                    handler.sendMessage(msg);
                } catch (QiniuException ex) {
                    Response r = ex.response;
                    System.err.println(r.toString());
                    try {
                        System.err.println(r.bodyString());
                    } catch (QiniuException ex2) {
                        //ignore
                    }
                }
            }
        }).start();
        return URL;
    }

    /**
     * 根据路径上传七牛云
     */
    private String uploadHtml(final String htmlCode) {
        NotePresenter notePresenter = new NotePresenter();
        notePresenter.refreshToken();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String Code = htmlCode;
                String key = null;
                // 七牛机房设置，构造一个带指定Zone对象的配置类
                Configuration cfg = new Configuration(Zone.zone0());
                UploadManager uploadManager = new UploadManager(cfg);
                //...生成上传凭证，然后准备上传
                String accessKey = "zV8eZYiEVp-Akvx_wGlUtfhjB3VmDd4mvl9u-s6O";
                String secretKey = "ij9yPsXMxznSeifiblhNTVpiGAvnhhhsXS4gdenc";
                String bucket = "thousfeet";
                //普通上传
                /*Auth auth = Auth.create(accessKey, secretKey);
                String upToken = auth.uploadToken(bucket);*/
                //魔法变量设置回调格式
                Auth auth = Auth.create(accessKey, secretKey);
                StringMap putPolicy = new StringMap();
                putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
                long expireSeconds = 3600;
                String upToken = auth.uploadToken(bucket, null, expireSeconds, putPolicy);
                Log.d(TAG, "Token is " + upToken);
                try {
                    byte[] uploadBytes = Code.getBytes("utf-8");
                    ByteArrayInputStream byteInputStream = new ByteArrayInputStream(uploadBytes);
                    Response response = uploadManager.put(byteInputStream, key, upToken, null, null);
                    //解析上传成功的结果
                    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                    Log.d(TAG, "成功上传" + putRet.key);
                    Log.d(TAG, "成功上传" + putRet.hash);
                    String url = "http://ozcxh8wzm.bkt.clouddn.com/" + putRet.key;
                    URL = url;
                    Log.d(TAG, "url is " + url);
                    // 上传服务器
                    int serverId = 0;
                    Log.d(TAG, "upLoadServer Url" + URL);
                    // 获取当前用户id
                    UserBean userBean;
                    UserPresenter userPresenter = new UserPresenter();
                    userBean = userPresenter.toGetUserInfo();
                    Log.d(TAG, "userBean" + userBean.getToken());
                    Log.d(TAG, "userBean" + userBean.getUserId());
                    Document doc = Jsoup.parse(htmlCode);
                    String content = doc.body().text();
                    Log.d(TAG, " Html Content is " + content);

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date currentDate = new Date(System.currentTimeMillis());
                    String dTime = formatter.format(currentDate);
                    // 上传服务器
                    sendNote(URL, dTime, false, content);
                    //
                    Connector.getDatabase();
                    NoteBean note = new NoteBean();
                    note.setContent(htmlCode);
                    note.setNoteId(NoteId);
                    note.setCreateTime(createTime);
                    note.setShareStatus(false);
                    note.setUserId(userBean.getUserId());
                    note.save();
                    noteTemp = note;
                    Log.d(TAG, "note 保存成功");
                    //Toast.makeText(NoteActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    // 结束上传
                    Message message = new Message();
                    message.what = SAVE_NOTE;
                    handler.sendMessage(message);
                } catch (QiniuException ex) {
                    Response r = ex.response;
                    System.err.println(r.toString());
                    try {
                        System.err.println(r.bodyString());
                    } catch (QiniuException ex2) {
                        //ignore
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "run: " + "expireSeconds");
                }
            }
        }).start();
        return URL;
    }

    private void sendNote(String url, String createTime, boolean share, String content) {
        //创建一个Client对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //json为String类型的json数据
        // 使用Gson生成
        Log.d(TAG, "sendNote: " + url);
        Log.d(TAG, "sendNote: " + createTime);
        Log.d(TAG, "sendNote: " + share);
        Log.d(TAG, "sendNote: " + content);
        Note note = new Note(url, createTime, share, content);
        String json = getJsonString(note);
        Log.d(TAG, "json is " + json);
        RequestBody requestBody = RequestBody.create(JSON, json);
        // 获取当前用户id
        UserBean userBean;
        UserPresenter userPresenter = new UserPresenter();
        userBean = userPresenter.toGetUserInfo();
        Log.d(TAG, "userBean" + userBean.getToken());
        Log.d(TAG, "userBean" + userBean.getUserId());
        // "http://www.cxpzz.com/learnlaravel5/public/index.php/api/users/" + userBean.getUserId() +"/notes"
        Request request = new Request.Builder()
                .url("http://119.29.179.150:81/api/users/" + userBean.getUserId() + "/notes")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", userBean.getToken())
                .post(requestBody)
                .build();
        try {
            okhttp3.Response response = okHttpClient.newCall(request).execute();
            String responseData = response.body().string();
            Log.d(TAG, "sendNote: response" + responseData);
            JSONObject jsonObject = new JSONObject(responseData);
            Log.d(TAG, "sendNote: errorCode is " + jsonObject.getInt("error_code"));
            if (jsonObject.getInt("error_code") == 200) {
                Log.d(TAG, "sendNote: noteJson" + jsonObject.getString("note"));
                JSONObject getNoteId = new JSONObject(jsonObject.getString("note"));
                NoteId = getNoteId.getInt("id");
                NOTE_ID = String.valueOf(NoteId);
                Log.d(TAG, "sendNote: Noteid " + NoteId);
                // 之后保存数据库
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 给定一个类，生成Json格式。
     *
     * @param object 需要被解析成Json的类
     * @return
     */
    private String getJsonString(Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return json;
    }

    /**
     * 初始化数据
     */
    private void initBundle() {
        if (getIntent().getExtras() == null) {
            Log.d(TAG, "initBundle: 是新建里的");
            isNew = true;
            isShare = false;
        } else {
            isNew = false;
            isEmpty = false;
            Log.d(TAG, "initBundle: 不是新的");
        }
        // 不是新建日记
        if (!isNew) {
            Bundle bundle = new Bundle();
            bundle = getIntent().getExtras();
            noteTemp = (NoteBean) bundle.getSerializable("note");
            isShare = noteTemp.isShareStatus();
            NOTE_ID = String.valueOf(noteTemp.getNoteId());
            Log.d(TAG, "initBundle: " + noteTemp.getNoteId());
            // TODO: 2017/12/14 不是新建的装载内容
            icarus.setContent(noteTemp.getContent());
        }
    }

    /**
     * 删除云端后删除本地数据库
     */
    private void deleteNote() {
        NotePresenter notePresenter = new NotePresenter();
        notePresenter.refreshToken();
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
                String json = "";
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url("http://119.29.179.150:81/api/users/" + userBean.getUserId() + "/notes/" + noteTemp.getNoteId())
                        .addHeader("Authorization", userBean.getToken())
                        .delete(body)
                        .build();
                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    Log.d(TAG, "delete response" + response.body().string());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 删除本地数据库
                String deleteId = String.valueOf(noteTemp.getId());
                Log.d(TAG, "delete id is " + deleteId);
                DataSupport.deleteAll(NoteBean.class, "id == ?", deleteId);
                Message msg = new Message();
                msg.what = DELETE_NOTE;
                handler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * 更新云端数据库，然后更新本地数据库
     */
    private void updateNote() {
        // TODO: 2017/12/14  更新时提供本地html代码 
        String htmlCode = htmlContent;
        updateHtml(htmlCode);
    }

    /**
     * 根据路径更新七牛云html
     */
    private String updateHtml(final String htmlCode) {
        NotePresenter notePresenter = new NotePresenter();
        notePresenter.refreshToken();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String Code = htmlCode;
                String key = null;
                // 七牛机房设置，构造一个带指定Zone对象的配置类
                Configuration cfg = new Configuration(Zone.zone0());
                UploadManager uploadManager = new UploadManager(cfg);
                //...生成上传凭证，然后准备上传
                String accessKey = "zV8eZYiEVp-Akvx_wGlUtfhjB3VmDd4mvl9u-s6O";
                String secretKey = "ij9yPsXMxznSeifiblhNTVpiGAvnhhhsXS4gdenc";
                String bucket = "thousfeet";
                //普通上传
                /*Auth auth = Auth.create(accessKey, secretKey);
                String upToken = auth.uploadToken(bucket);*/
                //魔法变量设置回调格式
                Auth auth = Auth.create(accessKey, secretKey);
                StringMap putPolicy = new StringMap();
                putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
                long expireSeconds = 3600;
                String upToken = auth.uploadToken(bucket, null, expireSeconds, putPolicy);
                Log.d(TAG, "Token is " + upToken);
                try {
                    byte[] uploadBytes = Code.getBytes("utf-8");
                    ByteArrayInputStream byteInputStream = new ByteArrayInputStream(uploadBytes);
                    Response response = uploadManager.put(byteInputStream, key, upToken, null, null);
                    //解析上传成功的结果
                    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                    Log.d(TAG, "成功上传" + putRet.key);
                    Log.d(TAG, "成功上传" + putRet.hash);
                    String url = "http://ozcxh8wzm.bkt.clouddn.com/" + putRet.key;
                    URL = url;
                    Log.d(TAG, "url is " + url);
                    // 更新服务器
                    int serverId = 0;
                    Log.d(TAG, "upLoadServer Url" + URL);
                    // 获取当前用户id
                    UserBean userBean;
                    UserPresenter userPresenter = new UserPresenter();
                    userBean = userPresenter.toGetUserInfo();
                    Log.d(TAG, "userBean" + userBean.getToken());
                    Log.d(TAG, "userBean" + userBean.getUserId());
                    Document doc = Jsoup.parse(htmlCode);
                    String content = doc.body().text();
                    Log.d(TAG, " Html Content is " + content);

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date currentDate = new Date(System.currentTimeMillis());
                    String dTime = formatter.format(currentDate);
                    // 更新服务器
                    updateNote(URL, dTime, false, content);
                    //更新本地数据
                    noteTemp.setCreateTime(createTime);
                    noteTemp.setContent(htmlCode);
                    String updateNoteId = String.valueOf(noteTemp.getNoteId());
                    noteTemp.updateAll("noteId = ?", updateNoteId);
                    Log.d(TAG, "note 修改成功");
                    // 结束上传
                    Message message = new Message();
                    message.what = SAVE_NOTE;
                    handler.sendMessage(message);
                } catch (QiniuException ex) {
                    Response r = ex.response;
                    System.err.println(r.toString());
                    try {
                        System.err.println(r.bodyString());
                    } catch (QiniuException ex2) {
                        //ignore
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "run: " + "expireSeconds");
                }
            }
        }).start();
        return URL;
    }

    private void updateNote(String url, String createTime, boolean share, String content) {
        //创建一个Client对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //json为String类型的json数据
        // 使用Gson生成
        putNote putNote = new putNote(url, share, content);
        String json = getJsonString(putNote);
        Log.d(TAG, "json is " + json);
        RequestBody requestBody = RequestBody.create(JSON, json);
        // 获取当前用户id
        UserBean userBean;
        UserPresenter userPresenter = new UserPresenter();
        userBean = userPresenter.toGetUserInfo();
        Log.d(TAG, "userBean" + userBean.getToken());
        Log.d(TAG, "userBean" + userBean.getUserId());
        // "http://www.cxpzz.com/learnlaravel5/public/index.php/api/users/" + userBean.getUserId() +"/notes"
        Request request = new Request.Builder()
                .url("http://119.29.179.150:81/api/users/" + userBean.getUserId() + "/notes/" + noteTemp.getNoteId())
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", userBean.getToken())
                .put(requestBody)
                .build();
        try {
            okhttp3.Response response = okHttpClient.newCall(request).execute();
            String responseData = response.body().string();
            Log.d(TAG, "updateNote: response" + responseData);
            JSONObject jsonObject = new JSONObject(responseData);
            Log.d(TAG, "updateNote: errorCode is " + jsonObject.getInt("error_code"));
            if (jsonObject.getInt("error_code") == 200) {
                Log.d(TAG, "sendNote: noteJson" + jsonObject.getString("note"));
/*                JSONObject getNoteId = new JSONObject(jsonObject.getString("note"));
                NoteId = getNoteId.getInt("id");
                Log.d(TAG, "sendNote: Noteid " + NoteId);
                // 之后保存数据库*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据路径更新七牛云html
     */
    private String shareHtml(final String htmlCode) {
        NotePresenter notePresenter = new NotePresenter();
        notePresenter.refreshToken();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String Code = htmlCode;
                String key = null;
                // 七牛机房设置，构造一个带指定Zone对象的配置类
                Configuration cfg = new Configuration(Zone.zone0());
                UploadManager uploadManager = new UploadManager(cfg);
                //...生成上传凭证，然后准备上传
                String accessKey = "zV8eZYiEVp-Akvx_wGlUtfhjB3VmDd4mvl9u-s6O";
                String secretKey = "ij9yPsXMxznSeifiblhNTVpiGAvnhhhsXS4gdenc";
                String bucket = "thousfeet";
                //普通上传
                /*Auth auth = Auth.create(accessKey, secretKey);
                String upToken = auth.uploadToken(bucket);*/
                //魔法变量设置回调格式
                Auth auth = Auth.create(accessKey, secretKey);
                StringMap putPolicy = new StringMap();
                putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
                long expireSeconds = 3600;
                String upToken = auth.uploadToken(bucket, null, expireSeconds, putPolicy);
                Log.d(TAG, "Token is " + upToken);
                try {
                    byte[] uploadBytes = Code.getBytes("utf-8");
                    ByteArrayInputStream byteInputStream = new ByteArrayInputStream(uploadBytes);
                    Response response = uploadManager.put(byteInputStream, key, upToken, null, null);
                    //解析上传成功的结果
                    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                    Log.d(TAG, "成功上传" + putRet.key);
                    Log.d(TAG, "成功上传" + putRet.hash);
                    String url = "http://ozcxh8wzm.bkt.clouddn.com/" + putRet.key;
                    URL = url;
                    Log.d(TAG, "url is " + url);
                    // 更新服务器
                    int serverId = 0;
                    Log.d(TAG, "upLoadServer Url" + URL);
                    // 获取当前用户id
                    UserBean userBean;
                    UserPresenter userPresenter = new UserPresenter();
                    userBean = userPresenter.toGetUserInfo();
                    Log.d(TAG, "userBean" + userBean.getToken());
                    Log.d(TAG, "userBean" + userBean.getUserId());
                    Document doc = Jsoup.parse(htmlCode);
                    String content = doc.body().text();
                    Log.d(TAG, " Html Content is " + content);

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date currentDate = new Date(System.currentTimeMillis());
                    String dTime = formatter.format(currentDate);

                    // 更新服务器,更新本地数据
                    if (!isNew) {
                        shareNote(url, content);
                        noteTemp.setCreateTime(createTime);
                        noteTemp.setContent(htmlCode);
                        noteTemp.setShareStatus(true);
                        String updateNoteId = String.valueOf(noteTemp.getNoteId());
                        noteTemp.updateAll("noteId = ?", updateNoteId);
                    } else {
                        //定义与事件相关的属性信息
                        try {
                            JSONObject eventObject = new JSONObject();
                            eventObject.put("用户事件", "新建记录");
                            eventObject.put("数量", 1);
                            //记录事件,以购买为例
                            ZhugeSDK.getInstance().track(getApplicationContext(), "新建记录", eventObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        sendNote(url, dTime, true, content);
                        shareNote(url, content);
                        // 新建笔记 服务器，本地
                        Connector.getDatabase();
                        NoteBean note = new NoteBean();
                        note.setContent(htmlCode);
                        note.setNoteId(NoteId);
                        note.setCreateTime(createTime);
                        note.setShareStatus(true);
                        note.setUserId(userBean.getUserId());
                        note.save();
                        //保存当前编辑的数据
                        noteTemp = note;
                    }
                    Log.d(TAG, "note 分享成功");
                    // 2017/12/12  新建笔记&旧的笔记成功的分享
                    // 结束上传
                    Message message = new Message();
                    message.what = SAVE_NOTE;
                    handler.sendMessage(message);
                } catch (QiniuException ex) {
                    Response r = ex.response;
                    System.err.println(r.toString());
                    try {
                        System.err.println(r.bodyString());
                    } catch (QiniuException ex2) {
                        //ignore
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "run: " + "expireSeconds");
                }
            }
        }).start();
        return URL;
    }

    private void shareNote(String url, String content) {
        //创建一个Client对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //json为String类型的json数据
        // 使用Gson生成
        putNote putNote = new putNote(url, true, content);
        String json = getJsonString(putNote);
        Log.d(TAG, "json is " + json);
        RequestBody requestBody = RequestBody.create(JSON, json);
        // 获取当前用户id
        UserBean userBean;
        UserPresenter userPresenter = new UserPresenter();
        userBean = userPresenter.toGetUserInfo();
        Log.d(TAG, "userBean" + userBean.getToken());
        Log.d(TAG, "userBean" + userBean.getUserId());
        // "http://www.cxpzz.com/learnlaravel5/public/index.php/api/users/" + userBean.getUserId() +"/notes"
        Request request = new Request.Builder()
                .url("http://119.29.179.150:81/api/users/" + userBean.getUserId() + "/notes/" + NOTE_ID)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", userBean.getToken())
                .put(requestBody)
                .build();
        try {
            okhttp3.Response response = okHttpClient.newCall(request).execute();
            String responseData = response.body().string();
            Log.d(TAG, "shareNote: response" + responseData);
            JSONObject jsonObject = new JSONObject(responseData);
            Log.d(TAG, "shareNote: errorCode is " + jsonObject.getInt("error_code"));
            if (jsonObject.getInt("error_code") == 200) {
                Log.d(TAG, "shareNote: noteJson" + jsonObject.getString("note"));
/*                JSONObject getNoteId = new JSONObject(jsonObject.getString("note"));
                NoteId = getNoteId.getInt("id");
                Log.d(TAG, "sendNote: Noteid " + NoteId);
                // 之后保存数据库*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消分享
     *
     * @param url
     */
    private void cancelShareNote(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建一个Client对象
                OkHttpClient okHttpClient = new OkHttpClient();
                //json为String类型的json数据
                // 使用Gson生成
                String content = noteTemp.getContent();
                try {
                    Document doc = Jsoup.connect(url).get();
                    content = doc.text();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                putNote putNote = new putNote(url, false, content);
                String json = getJsonString(putNote);
                Log.d(TAG, "json is " + json);
                RequestBody requestBody = RequestBody.create(JSON, json);
                // 获取当前用户id
                UserBean userBean;
                UserPresenter userPresenter = new UserPresenter();
                userBean = userPresenter.toGetUserInfo();
                Log.d(TAG, "userBean" + userBean.getToken());
                Log.d(TAG, "userBean" + userBean.getUserId());
                // "http://www.cxpzz.com/learnlaravel5/public/index.php/api/users/" + userBean.getUserId() +"/notes"
                Request request = new Request.Builder()
                        .url("http://119.29.179.150:81/api/users/" + userBean.getUserId() + "/notes/" + NOTE_ID)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", userBean.getToken())
                        .put(requestBody)
                        .build();
                try {
                    okhttp3.Response response = okHttpClient.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "cacel shareNote: response" + responseData);
                    JSONObject jsonObject = new JSONObject(responseData);
                    Log.d(TAG, "shareNote: errorCode is " + jsonObject.getInt("error_code"));
                    if (jsonObject.getInt("error_code") == 200) {
                        Log.d(TAG, "shareNote: noteJson" + jsonObject.getString("note"));
/*                JSONObject getNoteId = new JSONObject(jsonObject.getString("note"));
                NoteId = getNoteId.getInt("id");
                Log.d(TAG, "sendNote: Noteid " + NoteId);
                // 之后保存数据库*/
                        noteTemp.setShareStatus(false);
                        noteTemp.save();
                        Message msg = new Message();
                        msg.what = CANCEL_SHARE_NOTE;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "Save 正在保存");
        if (!isEmpty) {
            if (isNew) {
                //定义与事件相关的属性信息
                try {
                    JSONObject eventObject = new JSONObject();
                    eventObject.put("用户事件", "新建记录");
                    eventObject.put("数量", 1);
                    //记录事件,以购买为例
                    ZhugeSDK.getInstance().track(getApplicationContext(), "新建记录", eventObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                saveNote();
            } else {
                updateNote();
            }
        } else {
            Toast.makeText(this, "未输入文字不保存", Toast.LENGTH_SHORT).show();
        }
        super.onBackPressed();
    }

    private String formatContent(String html){
        StringBuffer sb = new StringBuffer(html);
        String result = null;
        sb.delete(0,12);
        sb.delete(sb.length()-2,sb.length());
        Log.d(TAG, "格式化" + sb.toString());
        result = sb.toString();
        return result;
    }
}
