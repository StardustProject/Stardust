package org.swsd.stardust.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import org.swsd.stardust.R;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.presenter.UserPresenter;
import java.util.Date;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author : 胡俊钦
 * time : 2017/11/12
 * description : 用户碎片
 * version : 1.0
 */
public class UserFragment extends Fragment {
    // 将毫秒化为天数时需要除以的数
    static Long DIVISOR = 8640000000L;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 加载动态布局
        View view = inflater.inflate(R.layout.fragment_user, null);

        // 设置“齿轮”图标监听事件
        ImageView ivSetting = view.findViewById(R.id.iv_my_setting);
        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击按钮后跳转到个人信息设置页面
                Intent goToSetting = new Intent("org.swsd.stardust.ACTION_START");
                startActivity(goToSetting);
            }
        });

        // 从数据库获取用户信息
        UserBean userBean;
        UserPresenter userPresenter = new UserPresenter();
        userBean = userPresenter.toGetUserInfo();

        // 显示用户头像
        CircleImageView civMyPhoto = view.findViewById(R.id.ic_my_user);
        if (userBean.getAvatarPath() != null) {
            Glide.with(this).load(userBean.getAvatarPath())
                    .placeholder(R.drawable.loading)
                    .into(civMyPhoto);
        } else {
            // 如果头像路径为空，则使用默认头像
            Glide.with(this).load(R.drawable.ic_user)
                    .into(civMyPhoto);
        }

        // 显示用户名
        TextView tvMyUser = view.findViewById(R.id.tv_my_user);
        tvMyUser.setText(userBean.getUserName());

        // 显示用户天数
        Date date = new Date();
        Long now = date.getTime();
        // 将毫秒数转化为天数
        Long days = (now - userBean.getRegisterTime()) / DIVISOR + 1;
        String str = days.toString();
        TextView tvMyDays = view.findViewById(R.id.tv_my_days);
        tvMyDays.setText("来到Stardust的第" + str + "天");

        // 显示用户星尘数
        int num = userPresenter.toGetStarNum();
        TextView tvMyStars = view.findViewById(R.id.tv_my_stars);
        tvMyStars.setText("已拥有" + num + "个星尘");

        return view;
    }
}
