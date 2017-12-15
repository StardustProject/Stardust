package org.swsd.stardust.model.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * author     :  张昭锡
 * time       :  2017/11/02
 * description:  数据库中Article表的JavaBean文件
 * version:   :  1.0
 */
public class ArticleBean extends DataSupport implements Serializable{

    private int id;
    private String createTime;
    private String title;
    private String author;
    private String content;
    private String articleUrl;
    private String articleCover;

    public String getArticleCover() {
        return articleCover;
    }

    public void setArticleCover(String articleCover) {
        this.articleCover = articleCover;
    }

    private String articleId;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
