package org.swsd.stardust.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.swsd.stardust.R;
import org.swsd.stardust.base.BaseActivity;
import org.swsd.stardust.presenter.ButtonNavigationBarPresenter.adapter.MainViewAdapter;
import org.swsd.stardust.presenter.ButtonNavigationBarPresenter.listener.OnTabSelectedListener;
import org.swsd.stardust.presenter.ButtonNavigationBarPresenter.widget.Tab;
import org.swsd.stardust.presenter.ButtonNavigationBarPresenter.widget.TabContainerView;
import org.swsd.stardust.view.fragment.ArticleFragment;
import org.swsd.stardust.view.fragment.HomeFragment;
import org.swsd.stardust.view.fragment.NoteFragment;
import org.swsd.stardust.view.fragment.StarFragment;
import org.swsd.stardust.view.fragment.UserFragment;

/**
 *     author : 熊立强
 *     time : 2017/11/11
 *     description : 主页面
 *     version : 1.0
 */
public class MainActivity extends BaseActivity {

    private TabContainerView tabContainerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        bindLayout();
        initView();
        steepStatusBar();
        MainViewAdapter mainViewAdapter=new MainViewAdapter(getSupportFragmentManager(),
                new Fragment[] {new HomeFragment(), new ArticleFragment(),new NoteFragment(), new StarFragment(),new UserFragment()});
        tabContainerView.setAdapter(mainViewAdapter);
        tabContainerView.setOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {

            }
        });
    }

    @Override
    public int bindLayout() {
        this.setContentView(R.layout.activity_main_container);
        return 0;
    }

    @Override
    public void initView() {
        Log.d("熊立强", "initView: ");
        tabContainerView = (TabContainerView) findViewById(R.id.tab_container);
    }

    @Override
    public void initData() {

    }
}
