package org.swsd.stardust.model.bean;

import org.litepal.crud.DataSupport;

/**
 * author : 熊立强
 * time   : 2017/12/15
 * desc   : 用户收藏文章Bean
 * version: 1.0
 */
public class ArticleCollectedBean extends DataSupport {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    private String articleUrl;
    private String articleId;
    private boolean isLiked;
}
