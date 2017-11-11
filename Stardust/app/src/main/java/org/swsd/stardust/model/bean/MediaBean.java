package org.swsd.stardust.model.bean;

import org.litepal.crud.DataSupport;

/**
 * author     :  张昭锡
 * time       :  2017/11/02
 * description:  数据库多媒体表的JavaBean文件
 *               存储记录中多媒体实体的文件名及文件路径
 * version:   :  1.0
 */

public class MediaBean extends DataSupport{

    private int id;
    private String mediaName;
    private String mediaPath;

    //多媒体归属Note的ID
    private int noteId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }
}
