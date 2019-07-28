package com.yueer.farmshop.kaoshitest.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity<V extends BaseView,P extends BasePresenter>
        extends AppCompatActivity implements BaseView{
    protected P mBasePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());//找布局
        ButterKnife.bind(this);//找控件
        mBasePresenter=initBasePresenter();//P层
        if (mBasePresenter!=null){
            mBasePresenter.bind((V)this);
        }

        initView();
        initListener();
        initData();

    }

    protected void initData() {

    }

    protected void initListener() {

    }

    protected void initView() {

    }

    protected abstract P initBasePresenter();

    protected abstract int getLayoutId();

    //悬浮按钮为什么直接退出
//????????
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mBasePresenter.onDestroy();
//        mBasePresenter=null;
//    }
}
