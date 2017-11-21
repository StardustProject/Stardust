package org.swsd.stardust.util;

import org.swsd.stardust.base.BaseActivity;

/**
 * author     :  骆景钊
 * time       :  2017/11/13
 * description:  用户权限申请
 * version:   :  1.0
 */

public class PermissionUtil {

    //申请权限（传入权限String表）,返回Boolean类型（true为申请权限成功，false为失败）
    public static Boolean requestPermission(String[] permissions){
        BaseActivity.requestRunTimePermission(permissions);
        return BaseActivity.checkPermission;
    }


}
