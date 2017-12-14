package org.swsd.stardust.view.guideActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.swsd.stardust.R;

/**
 * author     :  骆景钊
 * time       :  2017/12/15
 * description:  引导页第四页
 * version:   :  1.0
 */

public class FourthFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.guide_fragment_fourth, container, false);
        return view;
    }
}
