package org.swsd.stardust.util;

import org.swsd.stardust.base.BaseActivity;

/**
 * Created by Administrator on 2017/11/12.
 */

public class PermissionUtil {

    //申请权限（传入权限String表）,返回Boolean类型（true为申请权限成功，false为失败）
    public static Boolean requestPermission(String[] permissions){
        BaseActivity.requestRunTimePermission(permissions);
        return BaseActivity.checkPermission;
    }


}
