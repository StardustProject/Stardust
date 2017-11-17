package org.swsd.stardust.presenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.swsd.stardust.R;
import org.swsd.stardust.model.bean.MeteorBean;
import org.swsd.stardust.view.activity.MeteorDetail;

import java.util.List;

/**
 *     author : 骆景钊
 *     time : 2017/11/15
 *     description : 流星RecyclerView的适配器
 *     version : 1.0
 */

public class MeteorAdapter extends RecyclerView.Adapter<MeteorAdapter.ViewHolder> {

    private List<MeteorBean> mMeteorList;
    private Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_meteor_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MeteorAdapter.ViewHolder holder, int position) {
        final MeteorBean meteor = mMeteorList.get(position);

        if(!meteor.getIsPureMedia()){
            holder.meteorContent.setText(meteor.getMeteorContent());
        }else {
            holder.meteorContent.setVisibility(View.GONE);
            holder.meteorPicture.setImageResource(R.mipmap.ic_launcher_round);
            holder.meteorPicture.setVisibility(View.VISIBLE);
        }

        holder.meteorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext,MeteorDetail.class);
                Bundle bundle = new Bundle();
                //通过bundle传输数据
//                bundle.putSerializable("Meteor", meteor);
//                intent.putExtras(bundle);
//                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMeteorList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView meteorView;
        TextView meteorContent;
        ImageView meteorPicture;
        public ViewHolder(View view){
            super(view);
            meteorView = (CardView) view;
            meteorContent = (TextView) view.findViewById(R.id.tv_Meteor_content);
            meteorPicture = (ImageView) view.findViewById(R.id.iv_Meteor_picture);
        }
    }

    public MeteorAdapter(Context context, List<MeteorBean> meteorList){
        mContext = context;
        mMeteorList = meteorList;
    }
}
