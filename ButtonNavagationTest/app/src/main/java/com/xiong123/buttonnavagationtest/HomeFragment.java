package com.xiong123.buttonnavagationtest;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *     author : 熊立强
 *     time : 2017/11/11
 *     description : 主页类碎片
 *     version : 1.0
 */
public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       return inflater.inflate(R.layout.fragment_tab_1, null);

    }
}
