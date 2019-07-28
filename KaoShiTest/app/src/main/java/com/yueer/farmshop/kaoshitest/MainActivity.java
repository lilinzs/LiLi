package com.yueer.farmshop.kaoshitest;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.yueer.farmshop.kaoshitest.base.BaseActivity;
import com.yueer.farmshop.kaoshitest.base.BasePresenter;
import com.yueer.farmshop.kaoshitest.fragment.home.HomeFragment;
import com.yueer.farmshop.kaoshitest.fragment.home.classes.ClassesFragment;
import com.yueer.farmshop.kaoshitest.fragment.home.mycenter.MyCenterFragment;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    @BindView(R.id.main_toolbar_title)
    TextView mainToolbarTitle;
    @BindView(R.id.main_vp)
    ViewPager mainVp;
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.main_tab)
    TabLayout mainTab;

    @Override
    protected BasePresenter initBasePresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        mainToolbar.setTitle("");
        setSupportActionBar(mainToolbar);




        ArrayList<String> titles = new ArrayList<>();
        ArrayList<Fragment> fragments = new ArrayList<>();
        titles.add("首页");
        titles.add("分类");
        titles.add("个人中心");

        fragments.add(new HomeFragment());
        fragments.add(new ClassesFragment());
        fragments.add(new MyCenterFragment());

        initVp(titles,fragments);
    }

    private void initVp(ArrayList<String> titles, ArrayList<Fragment> fragments) {
        VpMainAdapter vpMainAdapter = new VpMainAdapter(getSupportFragmentManager(),fragments,titles);
        mainVp.setAdapter(vpMainAdapter);
        mainTab.setupWithViewPager(mainVp);

        mainTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mainToolbarTitle.setText(tab.getText());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


}
