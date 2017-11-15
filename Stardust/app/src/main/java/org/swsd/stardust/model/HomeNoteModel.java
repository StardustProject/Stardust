package org.swsd.stardust.model;

import org.litepal.crud.DataSupport;
import org.swsd.stardust.model.bean.NoteBean;
import org.swsd.stardust.presenter.HomePresenter;
import org.swsd.stardust.presenter.IHomePresenter;

import java.util.List;

/**
 *    author     :  张昭锡
 *    time       :  2017/11/14
 *    description:  主页Model层
 *    version:   :  1.0
 */

public class HomeNoteModel implements IHomeNoteModel{

    @Override
    public List<NoteBean> getNoteList() {
        return DataSupport.findAll(NoteBean.class);
    }

}
