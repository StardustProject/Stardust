package org.swsd.stardust.view.fragment;

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
import org.swsd.stardust.presenter.adapter.MeteorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *     author : 熊立强
 *     time : 2017/11/11
 *     description : 流星碎片
 *     version : 1.0
 */
public class StarFragment extends Fragment {

    private List<MeteorBean> meteorList = new ArrayList<>();
    MeteorAdapter meteorAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meteor, null);
        initMetor();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        meteorAdapter = new MeteorAdapter(getContext(), meteorList);
        Log.d("luojingzhao","success");
        recyclerView.setAdapter(meteorAdapter);
        return view;
    }

    private void initMetor() {
        String str = "我从14岁那年开始写日记，" +
                "一直写到今年27岁，第13本日记。它成为我人" +
                "生的一部分，于我而言比任何社交都要重要，它" +
                "带给我的......首先MVP 是从经典的MVC架构演变而来，那我们是不是要先说下何为MVC模式？\n" +
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
