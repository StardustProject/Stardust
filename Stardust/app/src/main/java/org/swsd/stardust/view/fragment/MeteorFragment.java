package org.swsd.stardust.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.swsd.stardust.R;
import org.swsd.stardust.model.bean.MeteorBean;
import org.swsd.stardust.model.bean.UserBean;
import org.swsd.stardust.presenter.IMeteorPresenter;
import org.swsd.stardust.presenter.MeteorPresenter;
import org.swsd.stardust.presenter.adapter.MeteorAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 *     author : 骆景钊
 *     time : 2017/11/15
 *     description : 流星碎片
 *     version : 1.0
 */
public class MeteorFragment extends Fragment {

    IMeteorPresenter meteorPresenter;
    private List<MeteorBean> meteorList = new ArrayList<>();
    MeteorAdapter meteorAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meteor, null);
//        initMetor();
        //编造的当前用户，之后应传入当前登录的用户
        UserBean userBean = new UserBean();
        userBean.setUserId(3);
        userBean.setToken("NWEwZDMzYmIzOTJkZjUuNTA2NzQ4ODQsMTIzNDU2Nzg5LDIwMTctMTEtMjMgMTQ6NDQ6MTE=");

        meteorPresenter = new MeteorPresenter();

        //流星更新
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("lanchtime", Context.MODE_PRIVATE);
        long lastLanchTime = sharedPreferences.getLong("isfisttoday", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (0 != lastLanchTime) {
            long now = System.currentTimeMillis();

            //超过时间更新
            int day = (int) ((now - lastLanchTime)/(24*60*60*1000));
            if (day > 0) {
                meteorPresenter.updataMeteor(userBean);
                editor.putLong("isfisttoday", now);
                editor.commit();
            }
        }else {

            //第一次点击更新
            meteorPresenter.updataMeteor(userBean);
            editor.putLong("isfisttoday", System.currentTimeMillis());
            editor.commit();
        }
        //获取数据库流星信息
        Log.d("luojingzhao","back");
        meteorList = meteorPresenter.getMeteorList();
        for(int i = 0; i<meteorList.size(); i++){
            Log.d("luojingzhao",meteorList.get(i).getURL());
        }
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        meteorAdapter = new MeteorAdapter(getContext(), meteorList);
        Log.d("luojingzhao","success");
        recyclerView.setAdapter(meteorAdapter);
//        sendRequestWithOkHttp();
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
