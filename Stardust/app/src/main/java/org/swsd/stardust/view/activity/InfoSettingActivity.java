package org.swsd.stardust.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.swsd.stardust.R;
import org.swsd.stardust.base.BaseActivity;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.presenter.SetAvatarPresenter;
import org.swsd.stardust.presenter.UserPresenter;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author     :  胡俊钦，林炜鸿
 * time       :  2017/11/07
 * description:  个人信息设置模块
 * version:   :  1.0
 */
public class InfoSettingActivity extends BaseActivity {

    private static final int CHOOSE_PHOTO = 2;
    UserBean userBean;
    UserPresenter userPresenter = new UserPresenter();

    private BroadcastReceiver bcReload = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 从数据库获取用户信息
            userBean = userPresenter.toGetUserInfo();
            // 显示用户名
            TextView tvMyUser = (TextView) findViewById(R.id.tv_setting_username);
            tvMyUser.setText(userBean.getUserName());

            //根据图片路径显示头像
            CircleImageView circleImageView = (CircleImageView) findViewById(R.id.civ_setting_photo);
            if (userBean.getAvatarPath().equals("")) {
                // 如果头像路径为空，则使用默认头像
                Glide.with(InfoSettingActivity.this).load(R.drawable.ic_setting_photo)
                        .into(circleImageView);
            } else {
                Glide.with(InfoSettingActivity.this).load(userBean.getAvatarPath())
                        .into(circleImageView);
            }
        }
    };

    @Override
    public int bindLayout() {
        setContentView(R.layout.activity_information_setting);
        return 0;
    }

    @Override
    public void initView() {
        // 沉浸式顶部栏，继承基类的方法
        steepStatusBar();
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bcReload);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 从数据库获取用户信息
        userBean = userPresenter.toGetUserInfo();
        // 显示用户名
        TextView tvMyUser = (TextView) findViewById(R.id.tv_setting_username);
        tvMyUser.setText(userBean.getUserName());

        //根据图片路径显示头像
        CircleImageView circleImageView = (CircleImageView) findViewById(R.id.civ_setting_photo);
        if (userBean.getAvatarPath().equals("")) {
            // 如果头像路径为空，则使用默认头像
            Glide.with(this).load(R.drawable.ic_setting_photo)
                    .into(circleImageView);
        } else {
            Glide.with(this).load(userBean.getAvatarPath())
                    .into(circleImageView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化界面,实现沉浸式顶部栏
        initView();
        // 绑定并加载登录界面布局
        bindLayout();
        // 注册刷新页面的广播接收器
        registerReceiver(bcReload, new IntentFilter("reload the setting page"));
        // 获取顶部状态栏的高度
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int stateBarHeight = resources.getDimensionPixelSize(resourceId);

        // 用空的TextView预留顶部状态栏高度
        TextView tvStateBar = (TextView) findViewById(R.id.tv_setting_stateBar);
        android.view.ViewGroup.LayoutParams setHeight = tvStateBar.getLayoutParams();
        setHeight.height = stateBarHeight;
        tvStateBar.setLayoutParams(setHeight);

        // 设置“返回”图标监听事件
        ImageView ivGoBack = (ImageView) findViewById(R.id.iv_go_back);
        ivGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击按钮后回到到个人信息页面
                finish();
            }
        });

        // 设置“修改用户名”图标监听事件
        ImageView ivSetUsername = (ImageView) findViewById(R.id.iv_setting_setUsername);
        ivSetUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击图标后转去设置用户名
                Intent goToSetName = new Intent(InfoSettingActivity.this, SetUsernameActivity.class);
                startActivity(goToSetName);
            }
        });

        // 设置“修改用户名”框监听事件
        TextView tvSetUsername = (TextView) findViewById(R.id.tv_setting_username);
        tvSetUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击图标后转去设置用户名
                Intent goToSetName = new Intent(InfoSettingActivity.this, SetUsernameActivity.class);
                startActivity(goToSetName);
            }
        });

      /*  // 设置“修改密码”图标监听事件
        ImageView ivSetPassword = (ImageView) findViewById(R.id.iv_setting_setPassword);
        ivSetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击图标后转去设置密码
                Intent goToSetPassword = new Intent(InfoSettingActivity.this, setPasswordActivity.class);
                startActivity(goToSetPassword);

            }
        });*/

        // 设置“修改密码”框监听事件
        TextView tvSetPassword = (TextView) findViewById(R.id.tv_setPassword);
        tvSetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击图标后转去设置密码
                Intent goToSetPassword = new Intent(InfoSettingActivity.this, setPasswordActivity.class);
                startActivity(goToSetPassword);

            }
        });

        // 选择头像
        ImageView ivChoosePhoto = (ImageView) findViewById(R.id.iv_setting_change_photo);
        ivChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查看权限
                if (ContextCompat.checkSelfPermission(InfoSettingActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(InfoSettingActivity.this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });

        // 选择头像
        TextView tvChoosePhoto = (TextView) findViewById(R.id.tv_choose_photo);
        tvChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查看权限
                if (ContextCompat.checkSelfPermission(InfoSettingActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(InfoSettingActivity.this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        // 打开相册
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    // 申请用户权限
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.
                        PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    // 用户拒绝授权
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
                // 判断手机系统版本号
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 手机系统在4.4及以上的才能使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 手机系统在4.4以下的使用这个方法处理图片
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
        String imagePath = null;
        // 如果是document类型的Uri，，则通过document id处理
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                // 解析出数字格式的id
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
            // 如果是file类型的Uri，直接获取图片路径
            imagePath = uri.getPath();
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，使用普通方式处理
            imagePath = getImagePath(uri, null);
        }

        // 把图片上传七牛云返回链接并把链接上传服务器
        SetAvatarPresenter setAvatar = new SetAvatarPresenter();
        setAvatar.afterChangeAvatar(getApplicationContext(), imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        // 把图片上传七牛云返回链接并把链接上传服务器
        SetAvatarPresenter setAvatar = new SetAvatarPresenter();
        setAvatar.afterChangeAvatar(getApplicationContext(), imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
            }
        }
        return path;
    }
}

