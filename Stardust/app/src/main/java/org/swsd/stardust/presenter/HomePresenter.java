package org.swsd.stardust.presenter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import org.swsd.stardust.model.HomeNoteModel;
import org.swsd.stardust.model.IHomeNoteModel;
import org.swsd.stardust.model.bean.NoteBean;
import org.swsd.stardust.presenter.adapter.HomeAdapter;
import org.swsd.stardust.view.fragment.IHomeView;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 *    author     :  张昭锡
 *    time       :  2017/11/14
 *    description:  主页Presenter层
 *    version:   :  1.0
 */
public class HomePresenter implements IHomePresenter{
    private static final String TAG = "HomePresenter";
    
    IHomeView mIHomeView;
    IHomeNoteModel mHomeNoteModel;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    public HomePresenter(IHomeView iHomeView){
        mIHomeView = iHomeView;
        mHomeNoteModel = new HomeNoteModel();
    }


    /**
     *    author     :  张昭锡
     *    time       :  2017/11/14
     *    description:  更改时间选择器时间
     *    version:   :  1.0
     */
    @Override
    public void changeDate(Context context, HomeAdapter adapter, List<NoteBean> noteList, int year, int month, int day) {
        mHomeNoteModel.setYear(year);
        mHomeNoteModel.setMonth(month - 1);
        mHomeNoteModel.setDay(day);

        noteList.clear();
        noteList.addAll(getNoteList());
        Log.d(TAG, "changeDate: zyzhang" + getNoteList().size());
        refreshAdapter(adapter);
    }


    /**
     *    author     :  张昭锡
     *    time       :  2017/11/14
     *    description:  获取用户记录数据
     *    version:   :  1.0
     */
    @Override
    public List<NoteBean> getNoteList() {
        return mHomeNoteModel.getNoteList();
    }

    @Override
    public void setNoteYear(int year) {
        mHomeNoteModel.setYear(year);
    }

    @Override
    public void setNoteMonth(int month) {
        mHomeNoteModel.setMonth(month);
    }

    @Override
    public void setNoteDay(int day) {
        mHomeNoteModel.setDay(day);
    }


    @Override
    public void refreshAdapter(final HomeAdapter adapter) {
        adapter.notifyDataSetChanged();
    }

    /**
     *    author     :  张昭锡
     *    time       :  2017/11/18
     *    description:  判断是否是闰年
     *    version:   :  1.0
     */
    @Override
    public boolean isLeapYear(int year) {
        boolean res = false;
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0){
            res = true;
        }else{
            res = false;
        }
        return res;
    }

    /**
     *    author     :  张昭锡
     *    time       :  2017/11/18
     *    description:  判断本月份天数是否是31天
     *    version:   :  1.0
     */
    @Override
    public boolean isBigMonth(int month) {
         boolean res = false;
        if (month == 1 || month == 3 || month == 5 || month == 7
                || month == 8 || month == 10 || month == 12){
            res = true;
        }else if (month == 4 || month == 6 || month == 9 || month == 11){
            res = false;
        }
        return res;
    }
}
