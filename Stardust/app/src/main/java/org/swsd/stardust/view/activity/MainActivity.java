package org.swsd.stardust.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;

import org.swsd.stardust.R;
import org.swsd.stardust.base.BaseActivity;
import org.swsd.stardust.view.fragment.ArticleFragment;
import org.swsd.stardust.view.fragment.HomeFragment;
import org.swsd.stardust.view.fragment.MeteorFragment;
import org.swsd.stardust.view.fragment.UserFragment;

/**
 *     author : 熊立强
 *     time :  2017/11/16
 *     description : 底部导航栏
 *     version : 2.0
 */
public class MainActivity extends BaseActivity {

    BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(new HomeFragment());
                    navigation.getBackground().setAlpha(0);
                    return true;
                case R.id.navigation_article:
                    navigation.getBackground().setAlpha(255);
                    replaceFragment(new ArticleFragment());
                    return true;
                case R.id.navigation_addtion:
                    Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_metor:
                    replaceFragment(new MeteorFragment());
                    return true;
                case R.id.navigation_user:
                    replaceFragment(new UserFragment());
                    return true;

            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindLayout();
        initView();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getBackground().setAlpha(0);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public int bindLayout() {
        this.setContentView(R.layout.activity_main);
        return 0;
    }

    @Override
    public void initView() {
        Log.d("熊立强", "initView: ");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        replaceFragment(new HomeFragment());
    }

    @Override
    public void initData() {

    }

    /**
     *  替换碎片
     * @param fragment 传入需要替换的碎片
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.top_content, fragment);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Main ");
        replaceFragment(new HomeFragment());
    }
}
