package org.swsd.stardust.util;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.swsd.stardust.base.ActivityCollector;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.view.activity.LoginActivity;

/**
 * author     :  骆景钊
 * time       :  2017/11/16
 * description:  处理文件丢失的若干情况
 * version:   :  1.0
 */

public class HandleDisappearFile {
    public static void  handleDisappearFile(Context context,String errorCode){
        if(errorCode == "1"){
            Toast.makeText(context,"找不到相应文件，您已被强制退出", Toast.LENGTH_SHORT).show();
            ActivityCollector.finishAll();
            DataSupport.deleteAll(UserBean.class);
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
    }
}
