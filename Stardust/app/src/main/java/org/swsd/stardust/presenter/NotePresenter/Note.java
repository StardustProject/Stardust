package org.swsd.stardust.presenter.NotePresenter;

/**
 * author : 熊立强
 * time   : 2017/11/18
 * desc   :
 * version: 1.0
 */
public class Note {
    private String url;
    private String create_time;
    private boolean share;
    private String content;

    public Note(String url, String create_time, boolean share, String content){
        this.url = url;
        this.create_time =create_time;
        this.share = share;
        this.content = content;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public boolean isShare() {
        return share;
    }

    public void setShare(boolean share) {
        this.share = share;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
