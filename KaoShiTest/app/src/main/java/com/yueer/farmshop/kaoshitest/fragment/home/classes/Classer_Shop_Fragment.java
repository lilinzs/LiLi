package com.yueer.farmshop.kaoshitest.fragment.home.classes;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yueer.farmshop.kaoshitest.R;
import com.yueer.farmshop.kaoshitest.base.BaseFragment;
import com.yueer.farmshop.kaoshitest.base.BasePresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class Classer_Shop_Fragment extends BaseFragment {


    @BindView(R.id.f_class_rlv)
    RecyclerView fClassRlv;
    Unbinder unbinder;
    private int mPosition;
    private List<DataBean.ClassFristShopBean> mClassFristShop;

    public Classer_Shop_Fragment(int position, List<DataBean.ClassFristShopBean> classFristShop) {
        // Required empty public constructor
        mPosition = position;
        mClassFristShop = classFristShop;
    }


    @Override
    protected BasePresenter initBasePresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_classer__shop_;
    }

    @Override
    protected void initView() {
//        RlvClassAdapter rlvClassAdapter = new RlvClassAdapter(mClassFristShop,mPosition,getContext());
//        fClassRlv.setAdapter(rlvClassAdapter);
//        fClassRlv.setLayoutManager(new LinearLayoutManager(getContext()));

    }
}
