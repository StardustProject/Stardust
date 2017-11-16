package com.xiong123.buttonnavagationtest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 *     author : 熊立强
 *     time : 2017/11/11
 *     description : 文章类碎片
 *     version : 1.0
 */
public class ArticleFragment extends Fragment {
    private static final String TAG = "熊立强：ArticleFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article,container,false);
        return view;
    }
}
