package org.swsd.stardust.presenter;

import android.app.Activity;
import android.content.Context;

import org.swsd.stardust.model.bean.MeteorBean;
import org.swsd.stardust.model.bean.NoteBean;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.presenter.adapter.MeteorAdapter;

import java.util.List;

/**
 * author  ： 骆景钊
 * time    ： 2017/11/16
 * desc    ： 流星的presenter层接口
 * version ： 1.0
 */

public interface IMeteorPresenter {
    List<MeteorBean> getMeteorList();
    void updataMeteor(UserBean userBean, final Activity mActivity);
}
