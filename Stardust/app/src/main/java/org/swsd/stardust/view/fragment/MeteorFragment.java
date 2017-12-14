package org.swsd.stardust.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.litepal.crud.DataSupport;
import org.swsd.stardust.R;
import org.swsd.stardust.model.bean.MeteorBean;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.presenter.IMeteorPresenter;
import org.swsd.stardust.presenter.MeteorPresenter;
import org.swsd.stardust.presenter.adapter.MeteorAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *     author : 骆景钊
 *     time : 2017/11/15
 *     description : 流星碎片
 *     version : 1.0
 */
public class MeteorFragment extends Fragment {

    IMeteorPresenter meteorPresenter;
    public static List<MeteorBean> meteorList = new ArrayList<>();
    public static MeteorAdapter meteorAdapter;
    RecyclerView recyclerView;
    Dialog mDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meteor, null);

        // 获取顶部状态栏的高度
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen","android");
        int stateBarHeight = resources.getDimensionPixelSize(resourceId);

        // 用空的TextView预留顶部状态栏高度
        TextView tvStateBar = view.findViewById(R.id.tv_meteor_stateBar);
        android.view.ViewGroup.LayoutParams setHeight =tvStateBar.getLayoutParams();
        setHeight.height =stateBarHeight;
        tvStateBar.setLayoutParams(setHeight);

//        initMetor();
        //假设为第一个用户，之后应传入当前登录的用户
        UserBean userBean = DataSupport.findLast(UserBean.class);

        meteorPresenter = new MeteorPresenter();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        //流星更新
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("lanchTime", Context.MODE_PRIVATE);
        int lastLanchTime = sharedPreferences.getInt("isFirstToday", -1);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (-1 != lastLanchTime) {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            //第二天更新
            if (day != lastLanchTime) {
                meteorList.clear();
                meteorPresenter.updataMeteor(userBean,getActivity());
                editor.putInt("isFirstToday", day);
                editor.commit();
            }
        }else {
            //第一次点击更新
            meteorList.clear();
            meteorPresenter.updataMeteor(userBean, getActivity());
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            editor.putInt("isFirstToday", day);
            editor.commit();
            Log.d("luojingzhao","back");
        }
        //获取数据库流星信息
        Log.d("luojingzhao","back");
        meteorList = meteorPresenter.getMeteorList();
        for(int i = 0; i<meteorList.size(); i++){
            Log.d("luojingzhao",meteorList.get(i).getURL());
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        meteorAdapter = new MeteorAdapter(getContext(), meteorList);
        Log.d("luojingzhao","success");
        recyclerView.setAdapter(meteorAdapter);

        return view;
    }

    private void initMetor() {
        String str = "首先MVP 是从经典的MVC架构演变而来，那我们是不是要先说下何为MVC模式？\n" +
                "系统C/S(Client/Server)三层架构模型：\n" +
                "1）视图层（View）：一般采用XML文件对应用的界面进行描述，使用的时候可以直接引入，极为方便，可以的大大缩短开发时间，也可以使用JavaScript+HTML等的方式作为View层，当然这里需要进行Java和JavaScript之间的通信，幸运的是，Android提供了非常方便的通信实现。业务逻辑层（BLL）：它的关注点主要集中在业务规则的制定、业务流程的实现等与业务需求有关的系统设计，也即是说它是与系统所应对的领域（Domain）逻辑有关，很多时候，也将业务逻辑层称为领域层。\n" +
                "2）控制层（Controller）：Android的控制层的重任通常落在了众多的Acitvity的肩上，这句话也就暗含了不要在Acitivity中写代码，要通过Activity交割Model业务逻辑层处理。\n" +
                "3）模型层（Model）：对数据库的操作、以及其他和数据有关的的操作都应该在Model里面处理，当然对业务计算等操作也是必须放在的该层的。就是应用程序中二进制的数据。";
        for (int i = 0; i <= 10; i++) {
            if(i % 2 == 0){
                MeteorBean meteor = new MeteorBean();
                meteor.setMeteorContent(str);
                meteorList.add(meteor);
            }else {
                MeteorBean meteor = new MeteorBean();
                meteor.setIsPureMedia(true);
                meteorList.add(meteor);
            }

        }
    }
}
