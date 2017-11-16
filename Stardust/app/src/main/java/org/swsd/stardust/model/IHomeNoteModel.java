package org.swsd.stardust.model;

import org.swsd.stardust.model.bean.NoteBean;

import java.util.List;

/**
 *    author     :  张昭锡
 *    time       :  2017/11/14
 *    description:  主页Model层接口
 *    version:   :  1.0
 */
public interface IHomeNoteModel {
    List<NoteBean> getNoteList();
    void setYear(int year);
    void setMonth(int month);
    void setDay(int day);
    int getYear();
    int getMonth();
    int getDay();
}
