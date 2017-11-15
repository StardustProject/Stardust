package org.swsd.stardust.presenter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.DatePicker;

import org.swsd.stardust.model.HomeNoteModel;
import org.swsd.stardust.model.IHomeNoteModel;
import org.swsd.stardust.model.bean.NoteBean;
import org.swsd.stardust.view.fragment.IHomeView;

import java.util.Calendar;
import java.util.List;

/**
 *    author     :  张昭锡
 *    time       :  2017/11/14
 *    description:  主页Presenter层
 *    version:   :  1.0
 */

public class HomePresenter implements IHomePresenter{
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
     *    description:  显示时间选择器日期
     *    version:   :  1.0
     */
    @Override
    public void showDate(){
        mIHomeView.showDate();
    }

    @Override
    public void showDate(String date) {
        mIHomeView.showDate(date);
    }

    /**
     *    author     :  张昭锡
     *    time       :  2017/11/14
     *    description:  设置时间选择器位置
     *    version:   :  1.0
     */
    @Override
    public void setDatePickerDialogPotision() {
        mIHomeView.setDatePickerDialogPosition();
    }


    /**
     *    author     :  张昭锡
     *    time       :  2017/11/14
     *    description:  更改时间选择器时间
     *    version:   :  1.0
     */
    @Override
    public void changeDate(Context context) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                String date = year + "/" + month + "/" + dayOfMonth;
                showDate(date);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year,month,day
        );
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }


    /**
     *    author     :  张昭锡
     *    time       :  2017/11/14
     *    description:  更新时间选择器显示示数
     *    version:   :  1.0
     */
    @Override
    public void updateDate() {
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = year + "/" + month + "/" + dayOfMonth;
                showDate(date);
            }
        };
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
}
