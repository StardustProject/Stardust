package org.swsd.stardust.presenter.ArticlePresenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.swsd.stardust.R;
import org.swsd.stardust.view.activity.WebViewActivity;

import java.util.List;

/**
 * author : 熊立强
 * time   : 2017/11/12
 * desc   : 文章类的适配器
 * version: 1.0
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>{
    private static final String TAG ="熊立强";
    private Context mContext;
    private List<Article> mArticleList;

    // 适配器的构造函数
    public ArticleAdapter(List<Article> articleList){
        mArticleList = articleList;
    }

    // 适配器的ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView tvArticleTitle;
        TextView tvArticleAuthor;
        TextView tvArticlePublishTime;
        TextView tvArticleAbstract;
        public ViewHolder (View view){
            super(view);
            cardView = (CardView) view;
            tvArticleTitle = (TextView) view.findViewById(R.id.article_title);
            tvArticleAuthor = (TextView) view.findViewById(R.id.article_author);
            tvArticlePublishTime = (TextView) view.findViewById(R.id.article_publish_time);
            tvArticleAbstract = (TextView) view.findViewById(R.id.article_abstract);
        }

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view  = LayoutInflater.from(mContext).inflate(R.layout.article_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        // CardView 设置监听事件
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Article article = mArticleList.get(position);
                Log.d(TAG, "url is " + article.getArticleUrl());
                Intent intent = new Intent(mContext, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url",article.getArticleUrl());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = mArticleList.get(position);
        holder.tvArticleTitle.setText(article.getArticleTitle());
        holder.tvArticleAuthor.setText(article.getArticleAuthor());
        holder.tvArticlePublishTime.setText(article.getArtilePublishTime());
        holder.tvArticleAbstract.setText(article.getArticleAbstract());
    }

    @Override
    public int getItemCount() {
        return mArticleList.size();
    }
}
