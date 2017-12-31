package org.swsd.stardust.model.bean;

import org.litepal.crud.DataSupport;

/**
 * author  ： 林炜鸿
 * time    ： 2017/12/13
 * desc    ： 数据库中Mail表的JavaBean文件
 * version ： 1.0
 */
public class MailBean extends DataSupport {

    private int id;
    private String sender;
    private String content;
    private String createTime;

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getSender() {return sender;}

    public void setSender(String sender) {this.sender = sender;}

    public String getContent() {return content;}

    public void setContent(String content) {this.content = content;}

    public String getCreateTime() {return createTime;}

    public void setCreateTime(String createTime) {this.createTime = createTime;}
}
