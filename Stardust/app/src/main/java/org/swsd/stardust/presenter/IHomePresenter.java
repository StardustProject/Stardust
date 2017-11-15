package org.swsd.stardust.presenter;

import android.app.DatePickerDialog;
import android.content.Context;

import org.swsd.stardust.model.bean.NoteBean;

import java.util.List;

/**
 *    author     :  张昭锡
 *    time       :  2017/11/14
 *    description:  主页Presenter层接口
 *    version:   :  1.0
 */

public interface IHomePresenter {

    void showDate();
    void showDate(String date);
    void setDatePickerDialogPotision();
    void changeDate(Context context);
    void updateDate();
    List<NoteBean> getNoteList();
}
