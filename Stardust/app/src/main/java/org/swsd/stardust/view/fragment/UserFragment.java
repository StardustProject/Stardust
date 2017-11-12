package org.swsd.stardust.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.swsd.stardust.R;

/**
 * author : 胡俊钦
 * time : 2017/11/12
 * description : 用户碎片
 * version : 1.0
 */
public class UserFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //加载动态布局
        View view = inflater.inflate(R.layout.fragment_user, null);

        //设置“齿轮”图标监听事件
        ImageView ivSetting = view.findViewById(R.id.iv_my_setting);
        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击按钮后跳转到个人信息设置页面
                Intent goToSetting = new Intent("org.swsd.stardust.ACTION_START");
                startActivity(goToSetting);
            }
        });
        return view;
    }
}
