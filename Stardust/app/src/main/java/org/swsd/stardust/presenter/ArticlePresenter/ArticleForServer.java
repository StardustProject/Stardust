package org.swsd.stardust.presenter.ArticlePresenter;

/**
 * author : 熊立强
 * time   : 2017/11/12
 * desc   :
 * version: 1.0
 */
public class ArticleForServer {
    private String id;
    private String url;
    private String need_dedication;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNeed_dedication() {
        return need_dedication;
    }

    public void setNeed_dedication(String need_dedication) {
        this.need_dedication = need_dedication;
    }
}
