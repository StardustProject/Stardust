package org.swsd.stardust.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * author     :  张昭锡
 * time       :  2017/11/02
 * description:  封装Activity中的通用方法
 * version:   :  1.0
 */
public abstract class BaseActivity extends AppCompatActivity {

	private NetWorkChangeReceiver netWorkChangeReceiver;

    //当前Class的名字
    protected final String TAG = this.getClass().getName();
    private String APP_NAME;

    //APP是否为调试状态
    private boolean isDebug;

	//返回是否权限申请成功
	public static Boolean checkPermission;

    /**
     * author     :  张昭锡
     * time       :  2017/11/02
     * description:  沉浸式状态栏
     * version:   :  1.0
     */
	public void steepStatusBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}

	/**
     * author     :  张昭锡
     * time       :  2017/11/02
     * description:  绑定布局
     * version:   :  1.0
     */
	public abstract int bindLayout();


    /**
     * author     :  张昭锡
     * time       :  2017/11/02
     * description:  初始化控件
     * version:   :  1.0
     */
	public abstract void initView();

    /**
     * author     :  张昭锡
     * time       :  2017/11/02
     * description:  初始化数据
     * version:   :  1.0
     */
    public abstract void initData();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		ActivityCollector.addActivity(this);

		/**
		 * author     :  骆景钊
		 * time       :  2017/11/11
		 * description:  捕捉当前网络状态，进行网络状态判断
		 * version:   :  1.0
		 */
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		netWorkChangeReceiver = new NetWorkChangeReceiver();
		registerReceiver(netWorkChangeReceiver, intentFilter);
        initView();
        initData();
    }
    
    @Override
	protected void onResume() {
		super.onResume();
        Log.d(TAG, "onResume: ");

    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
        Log.d(TAG, "onDestroy: ");
		ActivityCollector.removeActivity(this);
		unregisterReceiver(netWorkChangeReceiver);
	}

	/**
     * author     :  张昭锡
     * time       :  2017/11/02
     * description:  isDebug作为总开关，然后控制是否显示调试信息
     * version:   :  1.0
     */
	protected void $log(String msg) {
		if (isDebug) {
			Log.d(APP_NAME, msg);
		}
	}

	/**
	 * author     :  骆景钊
	 * time       :  2017/11/11
	 * description:  重写父类监听，增加了网路监听事件
	 * version:   :  1.0
	 */
	class NetWorkChangeReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			if(networkInfo == null || !networkInfo.isAvailable()){

				//若当前无网络则提醒用户
				Toast.makeText(context, "network is unavailable", Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * author     :  骆景钊
	 * time       :  2017/11/12
	 * description:  权限申请
	 * 				 permissions为待申请的权限集合
	 * version:   :  1.0
	 */
	public static void requestRunTimePermission(String[] permissions){

		Activity topActivity = ActivityCollector.getTopActivity();
		if (topActivity == null){
			return;
		}

		//用于存放为授权的权限
		List<String> permissionList = new ArrayList<>();

		//遍历传递过来的权限集合
		for (String permission : permissions) {

			//判断是否已经授权
			if (ContextCompat.checkSelfPermission(topActivity,permission) != PackageManager.PERMISSION_GRANTED){

				//未授权，则加入待授权的权限集合中
				permissionList.add(permission);
			}
		}

		//判断集合
		if (!permissionList.isEmpty()){

			//如果集合不为空，则需要去授权
			ActivityCompat.requestPermissions(topActivity,permissionList.toArray(new String[permissionList.size()]),1);
		}else{

			//为空，则已经全部授权
		}
	}

	/**
	 * author     :  骆景钊
	 * time       :  2017/11/12
	 * description:  权限申请结果
	 * 				 requestCode  请求码
	 *  			 permissions  所有的权限集合
	 *               grantResults 授权结果集合
	 * version:   :  1.0
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case 1:
				if (grantResults.length > 0){

					//被用户拒绝的权限集合
					List<String> deniedPermissions = new ArrayList<>();

					//用户通过的权限集合
					List<String> grantedPermissions = new ArrayList<>();
					for (int i = 0; i < grantResults.length; i++) {

						//获取授权结果，这是一个int类型的值
						int grantResult = grantResults[i];
						if (grantResult != PackageManager.PERMISSION_GRANTED){

							//用户拒绝授权的权限
							String permission = permissions[i];
							deniedPermissions.add(permission);
						}else{

							//用户同意的权限
							Log.d("luojingzhao","success");
							String permission = permissions[i];
							grantedPermissions.add(permission);
						}
					}

					if (deniedPermissions.isEmpty()){

						//用户拒绝权限为空
						checkPermission = true;
					}else {

						//不为空
						checkPermission = false;
					}
				}
				break;
			default:
				break;
		}
	}

}
