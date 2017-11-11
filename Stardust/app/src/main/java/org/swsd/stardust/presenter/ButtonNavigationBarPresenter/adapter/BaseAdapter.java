package org.swsd.stardust.presenter.ButtonNavigationBarPresenter.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 *     author : 熊立强
 *     time : 2017/11/11
 *     description : 底部导航栏的适配器基类
 *     version : 1.0
 */
public abstract class BaseAdapter {

    /**
     *  tab数量
     */
    public abstract int getCount();
    public abstract int hasMsgIndex();
    /**
     * tab text 数组
     */
    public abstract String[] getTextArray();

    /**
     * tab icon 数组
     */
    public abstract int[] getIconImageArray();

    /**
     * tab icon 选中 数组
     */
    public abstract int[] getSelectedIconImageArray();

    /**
     * fragment 数组
     */
    public abstract Fragment[] getFragmentArray();
    public abstract FragmentManager getFragmentManager();
}
