package org.swsd.stardust.presenter.ButtonNavigationBarPresenter.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 *     author : 熊立强
 *     time : 2017/11/11
 *     description : 底部碎片填充适配器
 *     version : 1.0
 */
public class TabViewPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] fragmentArray;

    public TabViewPagerAdapter(FragmentManager mFragmentManager, Fragment[] fragmentArray) {
        super(mFragmentManager);
        this.fragmentArray = fragmentArray;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentArray[position];
    }

    @Override
    public int getCount() {
        return fragmentArray.length;
    }
}

