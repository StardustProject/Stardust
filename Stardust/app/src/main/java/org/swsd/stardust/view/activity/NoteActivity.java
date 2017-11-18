package org.swsd.stardust.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.litepal.tablemanager.Connector;
import org.swsd.stardust.R;
import org.swsd.stardust.model.bean.NoteBean;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.presenter.NotePresenter.Note;
import org.swsd.stardust.presenter.UserPresenter;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.wasabeef.richeditor.RichEditor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 *     author : 熊立强
 *     time : 2017/11/16
 *     description : 记录模块
 *     version : 1.0
 */
public class NoteActivity extends AppCompatActivity {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final int SAVE_NOTE = 1;
    private RichEditor mEditor;
    private boolean isEdited = false;
    private static final String TAG = "熊立强";
    private static final int CHOOSE_PHOTO = 2;
    private String imagePath;
    private static String URL = null;
    private static int NoteId;

    private Handler handler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SAVE_NOTE :
                    finish();
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.note_toolbar);
        toolbar.setTitle("Note");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.go_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
        mEditor = (RichEditor)findViewById(R.id.md_editor);
        //mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.BLACK);
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("Insert text here...");
        //mEditor.setInputEnabled(false);
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override public void onTextChange(String text) {
                isEdited = true;
                Log.d(TAG, "onTextChange: " + isEdited);
            }
        });

        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.undo();
            }
        });

        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.redo();
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setItalic();
            }
        });

        findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setSubscript();
            }
        });

        findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setSuperscript();
            }
        });

        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(4);
            }
        });

        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(5);
            }
        });

        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(6);
            }
        });

        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBlockquote();
            }
        });

        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBullets();
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setNumbers();
            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getSystemImage();
                Log.d(TAG, "onClick: " + imagePath);
            }
        });

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
            }
        });
        findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.insertTodo();
            }
        });
    }

    /**
     *  填充toolbar菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.note_share:{
                //// TODO: 2017/11/16
                Log.d(TAG, "Share 正在分享");
                shareNote();
                return true;
            }

            case R.id.note_save:{
                Log.d(TAG, "Save 正在保存");
                saveNote();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     *  保存笔记函数
     */
    private void saveNote(){
        String noteHtml = mEditor.getHtml();
        // TODO: 2017/11/16 保存内容到本地，上传七牛云，上传服务器url
        String htmlCode = mEditor.getHtml();
        Log.d(TAG, "saveNote: " + htmlCode);
        /*String filePath = saveFile(htmlCode);
        Log.d(TAG, "saveNote: " + filePath);*/
        uploadHtml(htmlCode);
        Connector.getDatabase();
        NoteBean note = new NoteBean();
        note.setContent(htmlCode);
        note.setNoteId(NoteId);
        note.setCreateTime(0);
        note.setShareStatus(false);
        note.setUserId(0);
        note.save();
        Log.d(TAG, "note 保存成功" );
    }

    /**
     *  分享笔记函数
     */
    private void shareNote(){
        String noteHtml = mEditor.getHtml();
        // TODO: 2017/11/16  上传七牛云，回调之后上传服务器

        Log.d(TAG, "分享成功");
    }

    /**
     *  获取文件的路径
     * @return
     */
    private String getSystemImage() {
        // TODO: 2017/11/16  调用系统相册返回路径
        //查看权限
        if(ContextCompat.checkSelfPermission(NoteActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(NoteActivity.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else{
            openAlbum();
        }
        Log.d(TAG, "getSystemImage: " + imagePath);
        return imagePath;

    }

    /**
     * 打开相册
     */
    private void openAlbum(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        //打开相册
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    /**
     * 权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults){
        switch( requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.
                        PERMISSION_GRANTED){
                    // 获取权限，跳转选择照片
                    openAlbum();
                }else{
                    Toast.makeText(this,"You denied the permission",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch(requestCode){
            case CHOOSE_PHOTO:
                //判断手机系统版本号
                if(resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=19){
                        //手机系统在4.4及以上的才能使用这个方法处理图片
                        handleImageOnKitKat(data);
                    }else{
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
    private void handleImageOnKitKat(Intent data){
        Uri uri=data.getData();
        //如果是document类型的Uri，，则通过document id处理
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                //解析出数字格式的id
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:" +
                        "//downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri，直接获取图片路径
            imagePath = uri.getPath();
        }
        else if("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的Uri，使用普通方式处理
            imagePath=getImagePath(uri,null);
        }
        Log.d("熊立强", "6.0 handler" + imagePath);
        uploadQiniu(imagePath);
        //根据图片路径显示图片
        //displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        Log.d("熊立强", "before 6.0 handler" + imagePath);
        uploadQiniu(imagePath);
        //根据图片路径显示图片
        //displayImage(imagePath);
    }

    /**
     *   根据Uri获取，文件路径
     * @param uri
     * @param selection
     * @return
     */
    private String getImagePath(Uri uri,String selection){
        String path=null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor =getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
            }
        }
        return path;
    }

    /**
     *  根据路径上传七牛云
     * @param path
     */
    private String  uploadQiniu(final String path){


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
                    String url = "http://oziec3aec.bkt.clouddn.com/" + putRet.key;
                    Log.d(TAG, "url is " + url);
                    insertEditor(url);
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
     *  Editor插入图片
     * @param response
     */
    private void insertEditor(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NoteActivity.this, "图片正在载入中", Toast.LENGTH_SHORT).show();
                mEditor.insertImage(response,"dachshund");
            }
        });
    }

    /**
     *  显示 上传html 到七牛云
     * @param response
     */
    private void showHtml(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NoteActivity.this, "记录上传完成", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    /**
     *  根据路径上传七牛云
     */
    private String  uploadHtml(final String htmlCode){

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
                    ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);
                    Response response = uploadManager.put(byteInputStream, key, upToken,null,null);
                    //解析上传成功的结果
                    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                    Log.d(TAG, "成功上传" + putRet.key);
                    Log.d(TAG, "成功上传" + putRet.hash);
                    String url = "http://oziec3aec.bkt.clouddn.com/" + putRet.key;
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
                    String content = parseHtml(htmlCode);
                    // TODO: 2017/11/18
                    // 获取安卓系统时间
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
                    Date currentDate = new Date(System.currentTimeMillis());
                    String dTime = formatter.format(currentDate);
                    sendNote(URL,dTime,false,content);
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
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(TAG, "run: " + "expireSeconds");
                }
            }
        }).start();
        return URL;
    }

    private String parseHtml(String html){
        Document doc = Jsoup.parse(html);
        return doc.body().text();
    }
    private void sendNote(String url,String createTime,boolean share, String content){
        //创建一个Client对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //json为String类型的json数据
        // 使用Gson生成
        Note note = new Note(url,createTime,share,content);
        String json = getJsonString(note);
        Log.d(TAG, "json is " + json);
        RequestBody requestBody = RequestBody.create(JSON,json);
        // 获取当前用户id
        UserBean userBean;
        UserPresenter userPresenter = new UserPresenter();
        userBean = userPresenter.toGetUserInfo();
        Log.d(TAG, "userBean" + userBean.getToken());
        Log.d(TAG, "userBean" + userBean.getUserId());
        // "http://www.cxpzz.com/learnlaravel5/public/index.php/api/users/" + userBean.getUserId() +"/notes"
        Request request = new Request.Builder()
                .addHeader("Content-Type","tapplication/json")
                .addHeader("Authorization",userBean.getToken())
                .post(requestBody)
                .build();
        try{
            okhttp3.Response response = okHttpClient.newCall(request).execute();
            String responseData = response.body().string();
            Log.d(TAG, "sendNote: response" + responseData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 给定一个类，生成Json格式。
     *
     * @param object 需要被解析成Json的类
     * @return
     */
    private String getJsonString(Object object){
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return json;
    }
}
