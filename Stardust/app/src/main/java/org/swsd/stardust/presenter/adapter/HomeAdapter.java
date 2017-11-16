package org.swsd.stardust.presenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.swsd.stardust.R;
import org.swsd.stardust.model.bean.NoteBean;

import java.util.List;
import java.util.Random;

/**
 *    author     :  张昭锡
 *    time       :  2017/11/12
 *    description:  主页适配器
 *    version:   :  1.0
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{

    private static final String TAG = "HomeAdapter";

    private List<NoteBean>mNoteList;
    private Context mContext;
    private int[] mLightspotImages = {R.drawable.iv_home_lightspot_small,R.drawable.iv_home_lightspot_medium,
                                        R.drawable.iv_home_lightspot_big,R.drawable.iv_home_lightspot_large};

    static class ViewHolder extends RecyclerView.ViewHolder{
        View lightSpotView;
        ImageView lightSpotImage;
        TextView lightSportText;

        public ViewHolder(View view){
            super(view);
            lightSpotView = view;
            lightSpotImage = (ImageView)view.findViewById(R.id.iv_home_lightspot);
            lightSportText = (TextView)view.findViewById(R.id.tv_home_lightspot);
        }
    }

    public HomeAdapter(Context context, List<NoteBean>noteList){
        mContext = context;
        mNoteList = noteList;

        //随机没有记录实体的记录
        int size = mNoteList.size() / 2;
        Random random = new Random();
        for (int i = 0;i < size;i++){
            int cnt = random.nextInt(2);
            for (int j = 0;j < cnt;j++){
                NoteBean note = new NoteBean();
                note.setId(-1);
                int index = random.nextInt(mNoteList.size());
                mNoteList.add(index,note);
            }
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lightspot,parent,false);
        final ViewHolder holder = new ViewHolder(view);


        //TODO  记录详情页负责人员未创建记录详情相关代码文件
        holder.lightSpotImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int t;
        int sum = mNoteList.size();

        //根据记录总数将记录分成四部分，选择记录在主页光点的显示大小
        if (position <= sum / 4)   t = 3;
        else if (position > sum / 4 && position <= 2 * (sum / 4)) t = 2;
        else if (position > 2 * (sum / 4) && position < 3*(sum / 4))  t = 1;
        else t = 0;

        NoteBean note = mNoteList.get(position);
        holder.lightSpotImage.setImageResource(mLightspotImages[t]);

        //设置记录光点不规则排列
        holder.lightSportText.setText(getRandomLengthName("\n"));

        //隐藏随机生成的没有记录实体的记录
        holder.lightSpotView.setVisibility(note.getId() == -1?View.INVISIBLE:View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return mNoteList.size();
    }


    /**
     *    author     :  张昭锡
     *    time       :  2017/11/12
     *    description:  随机生成textview行数，使瀑布流不规则排列
     *    version:   :  1.0
     */
    private String getRandomLengthName(String name){
        Random random = new Random();
        int length = random.nextInt(5) + 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0;i < length;i++){
            builder.append(name);
        }
        return builder.toString();
    }
}