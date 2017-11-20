package org.swsd.stardust.presenter.NotePresenter;

/**
 * author : 熊立强
 * time   : 2017/11/19
 * desc   :
 * version: 1.0
 */
public class putNote {
    private String url;
    private boolean share;
    private String content;

    public putNote(String url, boolean share, String content) {
        this.url = url;
        this.share = share;
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getShare() {
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
