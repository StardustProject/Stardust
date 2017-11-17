package org.swsd.stardust.presenter;

import org.swsd.stardust.model.bean.MeteorBean;
import org.swsd.stardust.model.bean.NoteBean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/16.
 */

public interface IMeteorPresenter {
    List<MeteorBean> getMeteorList();
    void updataMeteor();
}
