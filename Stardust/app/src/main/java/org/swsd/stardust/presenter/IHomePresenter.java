package org.swsd.stardust.presenter;

import android.app.DatePickerDialog;
import android.content.Context;

import org.swsd.stardust.model.bean.NoteBean;
import org.swsd.stardust.presenter.adapter.HomeAdapter;

import java.util.List;

/**
 *    author     :  张昭锡
 *    time       :  2017/11/14
 *    description:  主页Presenter层接口
 *    version:   :  1.0
 */
public interface IHomePresenter {

    void changeDate(Context context,final HomeAdapter adapter,List<NoteBean>noteList,int year,int month,int day);
    void setNoteYear(int year);
    void setNoteMonth(int month);
    void setNoteDay(int day);
    void refreshAdapter(HomeAdapter adapter);
    List<NoteBean> getNoteList();
    boolean isLeapYear(int year);
    boolean isBigMonth(int month);

}
