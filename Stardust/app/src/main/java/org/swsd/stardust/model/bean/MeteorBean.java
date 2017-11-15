package org.swsd.stardust.model.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/11/15.
 */

public class MeteorBean extends DataSupport {

    private int id;
    private int noteId;
    private String meteorContent;
    private boolean isPureMedia;

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

    public String getMeteorContent() {
        return meteorContent;
    }

    public void setMeteorContent(String meteorContent) {
        this.meteorContent = meteorContent;
    }

    public boolean isPureMedia() {
        return isPureMedia;
    }

    public void setShareStatus(boolean isPureMedia) {
        this.isPureMedia = isPureMedia;
    }

}
