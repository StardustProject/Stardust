package org.swsd.stardust.presenter.ButtonNavigationBarPresenter.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import org.swsd.stardust.R;

/**
 *     author : 熊立强
 *     time : 2017/11/11
 *     description : 底部导航栏的适配器类
 *     version : 1.0
 */
public class MainViewAdapter extends BaseAdapter {

    private Fragment[] fragmentArray;
    private FragmentManager fragmentManager;
    private int hasMsgIndex=0;

    public void setHasMsgIndex(int hasMsgIndex) {
        this.hasMsgIndex = hasMsgIndex;
    }

    public MainViewAdapter(FragmentManager fragmentManager, Fragment[] fragmentArray) {
        this.fragmentManager = fragmentManager;
        this.fragmentArray = fragmentArray;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public int hasMsgIndex() {
        return hasMsgIndex;
    }


    @Override
    public String[] getTextArray() {
        return new String[] {"首页", "文章", "新建", "流星","用户"};
    }

    @Override
    public Fragment[] getFragmentArray() {
        return fragmentArray;
    }

    @Override
    public int[] getIconImageArray() {
        return new int[] {R.mipmap.homepage, R.mipmap.article, R.mipmap.addition, R.mipmap.star, R.mipmap.mine};
    }

    @Override
    public int[] getSelectedIconImageArray() {
        return new int[] {R.mipmap.homepage_fill, R.mipmap.article_fill, R.mipmap.addition_fill, R.mipmap.star_fill, R.mipmap.mine_fill};
    }

    @Override
    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }
}
