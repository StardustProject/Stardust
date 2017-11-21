package org.swsd.stardust.model;

import android.util.Log;

import org.litepal.crud.DataSupport;
import org.swsd.stardust.model.bean.NoteBean;
import org.swsd.stardust.presenter.HomePresenter;
import org.swsd.stardust.presenter.IHomePresenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import static android.R.attr.data;

/**
 *    author     :  张昭锡
 *    time       :  2017/11/14
 *    description:  主页Model层
 *    version:   :  1.0
 */
public class HomeNoteModel implements IHomeNoteModel{
    private static final String TAG = "HomeNoteModel";
    
    private List<NoteBean>mNoteList;
    int year;
    int month;
    int day;


    @Override
    public List<NoteBean> getNoteList() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(year,month,day,0,0,0);

        //根据日期计算得到的long值查询数据库
        long startTime = calendar.getTimeInMillis();
        long endTime = startTime + 24 * 3600 * 1000;

        //查询选定天数的记录
        mNoteList = DataSupport
                .where("createTime > ? and createTime < ?",String.valueOf(startTime), String.valueOf(endTime))
                .find(NoteBean.class);

        return mNoteList;
    }

    @Override
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public int getMonth() {
        return month;
    }

    @Override
    public int getDay() {
        return day;
    }
}
