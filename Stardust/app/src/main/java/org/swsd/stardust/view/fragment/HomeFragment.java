package org.swsd.stardust.view.fragment;


import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
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
public class HomeFragment extends Fragment implements IHomeView,View.OnClickListener,NumberPickerView.OnValueChangeListener,NumberPickerView.OnScrollListener{

    private View mView;
    private RecyclerView mRvLightspot;
    private NumberPickerView mNpYear;
    private NumberPickerView mNpMonth;
    private NumberPickerView mNpDay;
    private Button mBtnCheckdate;
    public static HomeAdapter adapter;
    private List<NoteBean>mNoteList;
    private String[] mStrYear;
    private String[] mStrMonth;
    private String[] mStrDateOfSmallMonth;
    private String[] mStrDateOfBigMonth;
    private String[] mStrLeapFeb;
    private String[] mStrComFeb;

    private int mPreYear;
    private int mPreMonth;
    private int mPreDay;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

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


        //日期初始化
        initNumberPickerString();
        initDate();

        //设置响应事件
        mBtnCheckdate.setOnClickListener(this);
        mNpYear.setOnValueChangedListener(this);
        mNpYear.setOnScrollListener(this);
        mNpMonth.setOnValueChangedListener(this);
        mNpMonth.setOnScrollListener(this);
        mNpDay.setOnValueChangedListener(this);
        mNpDay.setOnScrollListener(this);

        mNoteList = mHomePresenter.getNoteList();


        //设置瀑布流为4列
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL){
                    @Override
                    public boolean canScrollHorizontally() {
                        return false;
                    }
                };

        mRvLightspot.setLayoutManager(layoutManager);

        //mRvLightspot.setNestedScrollingEnabled(false);

        //创建主页适配器
        adapter = new HomeAdapter(getContext(), mNoteList);
        mRvLightspot.setAdapter(adapter);


        //监听年份状态
        mNpYear.setOnValueChangeListenerInScrolling(new NumberPickerView.OnValueChangeListenerInScrolling() {
            @Override
            public void onValueChangeInScrolling(NumberPickerView picker, int oldVal, int newVal) {
                mBtnCheckdate.setAlpha(1.0f);
                if (mHomePresenter.isLeapYear(Integer.parseInt(mNpYear.getContentByCurrValue()))){
                    if(Integer.parseInt(mNpMonth.getContentByCurrValue()) == 2){
                        mNpDay.refreshByNewDisplayedValues(mStrLeapFeb);
                    }else if (mHomePresenter.isBigMonth(Integer.parseInt(mNpMonth.getContentByCurrValue()))){
                        mNpDay.refreshByNewDisplayedValues(mStrDateOfBigMonth);
                    }else{
                        mNpDay.refreshByNewDisplayedValues(mStrDateOfSmallMonth);
                    }

                }else{
                    if(Integer.parseInt(mNpMonth.getContentByCurrValue()) == 2){
                        mNpDay.refreshByNewDisplayedValues(mStrComFeb);
                    }else if (mHomePresenter.isBigMonth(Integer.parseInt(mNpMonth.getContentByCurrValue()))){
                        mNpDay.refreshByNewDisplayedValues(mStrDateOfBigMonth);
                    }else{
                        mNpDay.refreshByNewDisplayedValues(mStrDateOfSmallMonth);
                    }
                }
                if (mPreDay - 1 > mNpDay.getMaxValue()){
                        mNpDay.setValue(mNpDay.getMaxValue());
                }else{
                    mNpDay.setValue(mPreDay - 1);
                }
            }

        });

        //监听月份状态
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
                if (mPreDay - 1 > mNpDay.getMaxValue()){
                        mNpDay.setValue(mNpDay.getMaxValue());
                }else{
                    mNpDay.setValue(mPreDay - 1);
                }

            }
        });

        if (pref.getBoolean("isFirst",true)){
            mHomePresenter.changeDate(getContext(),adapter,mNoteList,getActivity(),mPreYear,mPreMonth + 1,mPreDay);
            editor.putBoolean("isFirst",false);
            editor.apply();
        }

        Log.d(TAG, "onCreateView: zxzhang hahahha");

        Log.d(TAG, "onCreateView: zxzhang hahahha");

        return mView;
    }


    //监听确定按钮
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_home_checkdate:
                mBtnCheckdate.setAlpha(0.0f);
                mHomePresenter.changeDate(getContext(),adapter,mNoteList,getActivity(),
                        Integer.parseInt(mNpYear.getContentByCurrValue()),
                        Integer.parseInt(mNpMonth.getContentByCurrValue()), Integer.parseInt(mNpDay.getContentByCurrValue()));
                break;
        }
    }

    //日期变化则显示确定按钮
    @Override
    public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
        mBtnCheckdate.setAlpha(1.0f);
    }

    //数字选择器滚动监听
    @Override
    public void onScrollStateChange(NumberPickerView view, int scrollState) {
        switch (scrollState){
            case SCROLL_STATE_IDLE:
                mPreYear = Integer.parseInt(mNpYear.getContentByCurrValue());
                mPreMonth = Integer.parseInt(mNpMonth.getContentByCurrValue()) - 1;
                mPreDay = Integer.parseInt(mNpDay.getContentByCurrValue());
                break;
            default:
        }
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
        mPreYear = calendar.get(Calendar.YEAR);
        mPreMonth = calendar.get(Calendar.MONTH);
        mPreDay = calendar.get(Calendar.DAY_OF_MONTH);


        //持久化数据读取
        mPreYear = pref.getInt("mPreYear",mPreYear);
        mPreMonth = pref.getInt("mPreMonth",mPreMonth);
        mPreDay = pref.getInt("mPreDay",mPreDay);

        mHomePresenter.setNoteYear(mPreYear);
        mHomePresenter.setNoteMonth(mPreMonth);
        mHomePresenter.setNoteDay(mPreDay);


        mNpYear.refreshByNewDisplayedValues(mStrYear);
        mNpYear.setValue(mPreYear - 1970);

        mNpMonth.refreshByNewDisplayedValues(mStrMonth);
        mNpMonth.setValue(mPreMonth);

        mNpDay.refreshByNewDisplayedValues(mStrDateOfBigMonth);
        mNpDay.setValue(mPreDay - 1);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        pref = this.getActivity().getSharedPreferences("date",Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    //    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        Log.d(TAG, "onSaveInstanceState: zzz  in");
//        super.onSaveInstanceState(outState);
//        outState.putInt("mPreYear",mPreYear);
//        outState.putInt("mPreMonth",mPreMonth);
//        outState.putInt("mPreDay",mPreDay);
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        editor.putInt("mPreYear",mPreYear);
        editor.putInt("mPreMonth",mPreMonth);
        editor.putInt("mPreDay",mPreDay);
        editor.apply();
        if (isBackground(getContext())){
            editor.clear();
            editor.apply();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: ");
    }

    /**
     *    author     :  张昭锡
     *    time       :  2017/11/13
     *    description:  判断当前是否处于后台
     *    version:   :  1.0
     */
    public boolean isBackground(Context context) {
        
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.d(TAG, "isBackground: No");
                    return false;
                }else{
                    Log.d(TAG, "isBackground: Yes");
                    return true;
                }
            }
        }

        Log.d(TAG, "isBackground: No");
        return false;
    }



}
