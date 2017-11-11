package org.swsd.stardust.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

/**
 * author     :  张昭锡
 * time       :  2017/11/02
 * description:  封装Activity中的通用方法
 * version:   :  1.0
 */

public abstract class BaseActivity extends AppCompatActivity {

    //当前Class的名字
    protected final String TAG = this.getClass().getName();
    private String APP_NAME;

    //APP是否为调试状态
    private boolean isDebug;

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

}
