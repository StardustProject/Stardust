package org.swsd.stardust.model.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * author     :  骆景钊
 * time       :  2017/11/14
 * description:  数据库流星表的JavaBean文件
 *               存储流星的内容
 * version:   :  1.0
 */

public class MeteorBean extends DataSupport implements Serializable {

    private int meteorId;
    private String URL;
    private String meteorContent;
    private boolean isPureMedia;
    private int upvoteQuantity;
    private Boolean isLike;

    public int getMeteorId() {
        return meteorId;
    }

    public void setMeteorId(int meteorId) {
        this.meteorId = meteorId;
    }

    public String getURL(){
        return URL;
    }

    public  void setURL(String URL){
        this.URL = URL;
    }

    public String getMeteorContent() {
        return meteorContent;
    }

    public void setMeteorContent(String meteorContent) {
        this.meteorContent = meteorContent;
    }

    public boolean getIsPureMedia() {
        return isPureMedia;
    }

    public void setIsPureMedia(boolean isPureMedia) {
        this.isPureMedia = isPureMedia;
    }

    public int getUpvoteQuantity(){
        return upvoteQuantity;
    }

    public void setUpvoteQuantity(int upvoteQuantity){
        this.upvoteQuantity = upvoteQuantity;
    }

    public Boolean getIsLike(){
        return isLike;
    }

    public void setIsLike(Boolean isLike){
        this.isLike = isLike;
    }
}
