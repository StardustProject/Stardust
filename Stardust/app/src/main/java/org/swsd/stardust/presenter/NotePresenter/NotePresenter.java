package org.swsd.stardust.presenter.NotePresenter;

import android.util.Log;

import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.presenter.UserPresenter;
import org.swsd.stardust.util.UpdateTokenUtil;

/**
 * author : 熊立强
 * time   : 2017/11/29
 * desc   :
 * version: 1.0
 */
public class NotePresenter implements INotePresenter {

    /**
     * 刷新用互Token
     */
    @Override
    public void refreshToken() {
        // 更新token
        UserBean userBean;
        UserPresenter userPresenter = new UserPresenter();
        userBean = userPresenter.toGetUserInfo();
        UpdateTokenUtil.updateUserToken(userBean);
    }

    /**
     * 刷新七牛云Token
     */
    @Override
    public void refreshQiniuToken() {
        // 更新token
        UserBean userBean;
        UserPresenter userPresenter = new UserPresenter();
        userBean = userPresenter.toGetUserInfo();
        UpdateTokenUtil.getQiniuToken(userBean);
        Log.d("熊立强", "refreshQiniuToken: " + userBean.getQiniuToken());
    }
}
