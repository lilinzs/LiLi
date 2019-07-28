package com.yueer.farmshop.kaoshitest.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<V extends BaseView, P extends BasePresenter>
       extends Fragment implements BaseView {
    protected P mBasePresenter;
    private Unbinder mUnbinder;

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(getLayoutId(), null);//找布局
        mUnbinder = ButterKnife.bind(this, inflate);//找控件
        mBasePresenter=initBasePresenter();//P层
        if (mBasePresenter!=null){
            mBasePresenter.bind((V)this);

        }
        initView();
        initListener();
        initData();

        return inflate;
    }

    protected void initData() {

    }

    protected void initListener() {

    }

    protected void initView() {

    }

    protected abstract P initBasePresenter();

    protected abstract int getLayoutId();

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mBasePresenter.onDestroy();
        mBasePresenter=null;
    }
}
