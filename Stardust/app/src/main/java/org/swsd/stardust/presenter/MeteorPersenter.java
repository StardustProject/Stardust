package org.swsd.stardust.presenter;

import org.swsd.stardust.model.IMeteorModel;
import org.swsd.stardust.model.bean.MeteorBean;
import org.swsd.stardust.model.bean.NoteBean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/16.
 */

public class MeteorPersenter implements IMeteorPresenter {

    IMeteorModel meteorModel;
    @Override
    public List<MeteorBean> getMeteorList() {
        return meteorModel.getMeteorList();
    }
}
