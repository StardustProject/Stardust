package org.swsd.stardust.model;

import org.swsd.stardust.model.bean.MeteorBean;
import org.swsd.stardust.model.bean.NoteBean;

import java.util.List;

/**
 *    author     :  骆景钊
 *    time       :  2017/11/15
 *    description:  流星Model层接口
 *    version:   :  1.0
 */

public interface IMeteorModel {
    List<MeteorBean> getMeteorList();
}
