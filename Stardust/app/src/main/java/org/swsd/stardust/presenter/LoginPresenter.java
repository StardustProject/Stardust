package org.swsd.stardust.presenter;

import android.content.Context;
import android.text.Editable;
import android.util.Log;

import org.swsd.stardust.presenter.ButtonNavigationBarPresenter.tools.CommonFunctions;

/**
 * author  ： 胡俊钦
 * time    ： 2017/11/16
 * desc    ： 登录逻辑
 * version ： 1.0
 */
public class LoginPresenter {

    private Context mContext;

    public void checkBeforeLogin(final Context context, Editable username, Editable password) {
        // 创建工具类对象
        final CommonFunctions fCheck = new CommonFunctions();
        mContext = context;
        // 先进行格式检查
        if (fCheck.check(context, username, password)) {
            String strUsername = username.toString();
            String strPassword = password.toString();

            // 格式检查通过，向服务器发送账号密码进行验证
            Log.d("LoginPresenter", "Begin to login");
            UserPresenter userPresenter = new UserPresenter();
            userPresenter.toLogin(mContext, strUsername, strPassword);
            // 获取所有的站内信
            MailPresenter mailPresenter = new MailPresenter();
            mailPresenter.toGetMail(mContext, MailPresenter.ALL_MAIL);
        }
    }

}







