package org.swsd.stardust.model.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * author     :  张昭锡
 * time       :  2017/11/02
 * description:  数据库记录表的JavaBean文件
 * version:   :  1.0
 */
public class NoteBean extends DataSupport implements Serializable{

    private int id;

    //服务器返回Note的ID
    private int noteId;
    private long createTime;
    private boolean shareStatus;
    private String content;


    //归属的用户ID
    private int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isShareStatus() {
        return shareStatus;
    }

    public void setShareStatus(boolean shareStatus) {
        this.shareStatus = shareStatus;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
