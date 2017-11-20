package org.swsd.stardust.presenter.ArticlePresenter;

/**
 * author : 熊立强
 * time   : 2017/11/12
 * desc   :
 * version: 1.0
 */
public class Article {
    private String articleTitle;
    private String articleAuthor;
    private String artilePublishTime;
    private String articleAbstract;
    private String articleUrl;
    private String articleId;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleAuthor() {
        return articleAuthor;
    }

    public void setArticleAuthor(String articleAuthor) {
        this.articleAuthor = articleAuthor;
    }

    public String getArtilePublishTime() {
        return artilePublishTime;
    }

    public void setArtilePublishTime(String artilePublishTime) {
        this.artilePublishTime = artilePublishTime;
    }

    public String getArticleAbstract() {
        return articleAbstract;
    }

    public void setArticleAbstract(String articleAbstract) {
        this.articleAbstract = articleAbstract;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }
}
