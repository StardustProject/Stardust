package org.swsd.stardust.view.fragment;


import android.app.DatePickerDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.swsd.stardust.R;
import org.swsd.stardust.model.bean.NoteBean;
import org.swsd.stardust.presenter.HomePresenter;
import org.swsd.stardust.presenter.IHomePresenter;
import org.swsd.stardust.presenter.adapter.HomeAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 *    author     :  张昭锡
 *    time       :  2017/11/13
 *    description:  编写主页View层
 *    version:   :  1.0
 */
public class HomeFragment extends Fragment implements IHomeView,View.OnClickListener{

    private View mView;
    private RecyclerView mRvLightspot;
    private TextView mTvDisplayDate;
    private HomeAdapter adapter;
    private List<NoteBean>mNoteList;
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
        mTvDisplayDate = (TextView)mView.findViewById(R.id.tv_home_date);

        //设置时间选择器响应事件
        mTvDisplayDate.setOnClickListener(this);

        //设置时间选择器主页显示位置
        mHomePresenter.setDatePickerDialogPotision();

        //显示日期
        mHomePresenter.showDate();

        mNoteList = mHomePresenter.getNoteList();

        //设置瀑布流为4列
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        mRvLightspot.setLayoutManager(layoutManager);

        //创建主页适配器
        adapter = new HomeAdapter(getContext(), mNoteList);
        mRvLightspot.setAdapter(adapter);

        return mView;
    }

    @Override
    public void showDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        mHomePresenter.setNoteYear(year);
        mHomePresenter.setNoteMonth(month);
        mHomePresenter.setNoteDay(day);
        mTvDisplayDate.setText(year + "/" + (month + 1) + "/" + day);

    }

    @Override
    public void showDate(String date) {
        mTvDisplayDate.setText(date);
    }

    @Override
    public void setDatePickerDialogPosition() {
        mTvDisplayDate.setGravity(Gravity.CENTER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_home_date:
                mHomePresenter.changeDate(getContext(),adapter,mNoteList);
                break;
        }
    }

}
