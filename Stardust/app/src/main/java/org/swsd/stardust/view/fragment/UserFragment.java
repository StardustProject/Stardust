package org.swsd.stardust.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.swsd.stardust.R;

/**
 *     author : 熊立强
 *     time : ${YEAR}/${MONTH}/${DAY}
 *     description : 用户碎片
 *     version : 1.0
 */
public class UserFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_tab_5, null);

    }
}
