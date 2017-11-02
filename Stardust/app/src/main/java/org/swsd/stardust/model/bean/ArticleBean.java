package org.swsd.stardust.model.bean;

import org.litepal.crud.DataSupport;

/**
 * author     :  张昭锡
 * time       :  2017/11/02
 * description:  数据库中Article表的JavaBean文件
 * version:   :  1.0
 */

public class ArticleBean extends DataSupport{

    private int id;
    private long createTime;
    private String title;
    private String author;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
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
