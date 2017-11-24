package org.swsd.stardust.presenter.ArticlePresenter;

import android.app.Activity;

import org.swsd.stardust.model.bean.ArticleBean;
import org.swsd.stardust.model.bean.UserBean;

import java.util.List;

/**
 * author : 熊立强
 * time   : 2017/11/20
 * desc   : 获取文章Presenter
 * version: 2.0
 */
public interface IArticlePresenter {
    List<ArticleBean> getArticleList();
    void getArticle(UserBean userBean, final Activity mActivity);
}
