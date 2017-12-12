package org.swsd.stardust.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.swsd.stardust.R;
import org.swsd.stardust.model.bean.NoteBean;
import org.swsd.stardust.presenter.HomePresenter;
import org.swsd.stardust.presenter.IHomePresenter;
import org.swsd.stardust.presenter.adapter.HomeAdapter;

import java.util.Calendar;
import java.util.List;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;

/**
 *    author     :  张昭锡
 *    time       :  2017/11/13
 *    description:  编写主页View层
 *    version:   :  1.0
 */
public class HomeFragment extends Fragment implements IHomeView,View.OnClickListener,NumberPickerView.OnValueChangeListener{

    private View mView;
    private RecyclerView mRvLightspot;
    private NumberPickerView mNpYear;
    private NumberPickerView mNpMonth;
    private NumberPickerView mNpDay;
    private Button mBtnCheckdate;
    private HomeAdapter adapter;
    private List<NoteBean>mNoteList;
    private String[] mStrYear;
    private String[] mStrMonth;
    private String[] mStrDateOfSmallMonth;
    private String[] mStrDateOfBigMonth;
    private String[] mStrLeapFeb;
    private String[] mStrComFeb;


    IHomePresenter mHomePresenter;

    private static final String TAG = "HomeFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home,container,false);

        //初始化
        mHomePresenter = new HomePresenter(this);

        //控件初始化
        mRvLightspot = (RecyclerView)mView.findViewById(R.id.rv_home_lightspot);
        mNpYear = (NumberPickerView)mView.findViewById(R.id.np_home_year);
        mNpMonth = (NumberPickerView)mView.findViewById(R.id.np_home_month);
        mNpDay = (NumberPickerView)mView.findViewById(R.id.np_home_day);
        mBtnCheckdate = (Button)mView.findViewById(R.id.btn_home_checkdate);

        initNumberPickerString();
        initDate();


        //设置响应事件
        mBtnCheckdate.setOnClickListener(this);
        mNpYear.setOnValueChangedListener(this);
        mNpMonth.setOnValueChangedListener(this);
        mNpDay.setOnValueChangedListener(this);

        mNpYear.setOnValueChangeListenerInScrolling(new NumberPickerView.OnValueChangeListenerInScrolling() {
            @Override
            public void onValueChangeInScrolling(NumberPickerView picker, int oldVal, int newVal) {
                mBtnCheckdate.setAlpha(1.0f);
                if (mHomePresenter.isLeapYear(Integer.parseInt(mNpYear.getContentByCurrValue()))){
                    mNpDay.refreshByNewDisplayedValues(mStrLeapFeb);
                }else{
                    mNpDay.refreshByNewDisplayedValues(mStrComFeb);
                }

            }

        });

        mNpMonth.setOnValueChangeListenerInScrolling(new NumberPickerView.OnValueChangeListenerInScrolling() {
            @Override
            public void onValueChangeInScrolling(NumberPickerView picker, int oldVal, int newVal) {
                mBtnCheckdate.setAlpha(1.0f);
                switch (Integer.parseInt(mNpMonth.getContentByCurrValue())){
                    case 2:
                        if (mHomePresenter.isLeapYear(Integer.parseInt(mNpYear.getContentByCurrValue()))){
                            mNpDay.refreshByNewDisplayedValues(mStrLeapFeb);
                        }else{
                            mNpDay.refreshByNewDisplayedValues(mStrComFeb);
                        }
                        break;
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                    case 8:
                    case 10:
                    case 12:
                        mNpDay.refreshByNewDisplayedValues(mStrDateOfBigMonth);
                        break;
                    case 4:
                    case 6:
                    case 9:
                    case 11:
                        mNpDay.refreshByNewDisplayedValues(mStrDateOfSmallMonth);
                        break;
                    default:
                }

            }
        });


        mNoteList = mHomePresenter.getNoteList();

        //设置瀑布流为4列
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);

        mRvLightspot.setLayoutManager(layoutManager);

        //创建主页适配器
        adapter = new HomeAdapter(getContext(), mNoteList);
        mRvLightspot.setAdapter(adapter);

        return mView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_home_checkdate:
                mBtnCheckdate.setAlpha(0.0f);
                mHomePresenter.changeDate(getContext(),adapter,mNoteList,
                        Integer.parseInt(mNpYear.getContentByCurrValue()),
                        Integer.parseInt(mNpMonth.getContentByCurrValue()), Integer.parseInt(mNpDay.getContentByCurrValue()));
                break;

        }
    }

    @Override
    public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
        mBtnCheckdate.setAlpha(1.0f);
    }


    public void initNumberPickerString(){
        mStrYear = new String[81];
        for (int i = 1970;i <= 2050;i++){
            mStrYear[i - 1970] = String.valueOf(i);
        }

        mStrMonth = new String[12];
        for (int i = 1;i <= 12;i++){
            mStrMonth[i - 1] = String.valueOf(i);
        }

        mStrDateOfSmallMonth = new String[30];
        for (int i = 1;i <= 30;i++){
            mStrDateOfSmallMonth[i - 1] = String.valueOf(i);
        }

        mStrDateOfBigMonth = new String[31];
        for (int i = 1;i <= 31;i++){
            mStrDateOfBigMonth[i - 1] = String.valueOf(i);
        }

        mStrLeapFeb = new String[29];
        for (int i = 1;i <= 29;i++){
            mStrLeapFeb[i - 1] = String.valueOf(i);
        }

        mStrComFeb = new String[28];
        for (int i = 1;i <= 28;i++){
            mStrComFeb[i - 1] = String.valueOf(i);
        }
    }

    void initDate(){
        //获取日期
        Calendar calendar = Calendar.getInstance();
        int mPreYear = calendar.get(Calendar.YEAR);
        int mPreMonth = calendar.get(Calendar.MONTH);
        int mPreDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHomePresenter.setNoteYear(mPreYear);
        mHomePresenter.setNoteMonth(mPreMonth);
        mHomePresenter.setNoteDay(mPreDay);


        mNpYear.refreshByNewDisplayedValues(mStrYear);
        mNpYear.setValue(mPreYear - 1970);

        mNpMonth.refreshByNewDisplayedValues(mStrMonth);
        mNpMonth.setValue(mPreMonth);

        mNpDay.refreshByNewDisplayedValues(mStrDateOfBigMonth);
        mNpDay.setValue(mPreDay);

    }

}
