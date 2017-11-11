package org.swsd.stardust.presenter;

import android.content.Context;

/**
 * author     :  张昭锡
 * time       :  2017/11/02
 * description:  Presenter层的基类接口
 * version:   :  1.0
 */

public interface IPresenter<T> {

    /**
     * author     :  张昭锡
     * time       :  2017/11/02
     * description:  presenter对象与view对象建立连接
     * version:   :  1.0
     */
    void attachView(T view, Context context);


    /**
     * author     :  张昭锡
     * time       :  2017/11/02
     * description:  保持presenter对象与view对象生命周期一致，即v销毁，p也失效
     * version:   :  1.0
     */
    void detachView();
}
