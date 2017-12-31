package org.swsd.stardust.presenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.swsd.stardust.R;
import org.swsd.stardust.model.bean.MailBean;

import java.util.List;

/**
 * author  ： 林炜鸿
 * time    ： 2017/12/14
 * desc    ： 站内信的RecyclerView的适配器
 * version ： 1.0
 */
public class MailAdapter extends  RecyclerView.Adapter<MailAdapter.ViewHolder>{
    private List<MailBean> mMailBeanList;

    public MailAdapter(List<MailBean> mailBeanList) {
        mMailBeanList = mailBeanList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSender;
        TextView tvContent;
        TextView tvCreateTime;

        public ViewHolder(View view) {
            super(view);
            tvSender = view.findViewById(R.id.tv_mail_sender);
            tvContent = view.findViewById(R.id.tv_mail_content);
            tvCreateTime = view.findViewById(R.id.tv_mail_createTime);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_mail_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MailBean mailBean = mMailBeanList.get(position);
        holder.tvSender.setText(mailBean.getSender());
        holder.tvContent.setText(mailBean.getContent());
        holder.tvCreateTime.setText(mailBean.getCreateTime());
    }

    @Override
    public int getItemCount() {
        return mMailBeanList.size();
    }
}
