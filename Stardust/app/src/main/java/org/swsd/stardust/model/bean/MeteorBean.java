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

public class MeteorBean extends DataSupport  {

    private int noteId;
    private String URL;
    private String meteorContent;
    private boolean isPureMedia;

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
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

}
