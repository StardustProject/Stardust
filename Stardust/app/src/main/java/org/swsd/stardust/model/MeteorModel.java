package org.swsd.stardust.model;

import org.litepal.crud.DataSupport;
import org.swsd.stardust.model.bean.MeteorBean;
import org.swsd.stardust.model.bean.NoteBean;

import java.util.ArrayList;
import java.util.List;

/**
 *    author     :  骆景钊
 *    time       :  2017/11/15
 *    description:  流星Model层
 *    version:   :  1.0
 */

public class MeteorModel implements IMeteorModel {

    @Override
    public List<MeteorBean> getMeteorList() {
        return DataSupport.findAll(MeteorBean.class);
    }

}
