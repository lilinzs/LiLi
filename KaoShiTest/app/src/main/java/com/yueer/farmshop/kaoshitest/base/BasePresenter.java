package com.yueer.farmshop.kaoshitest.base;

import java.util.ArrayList;

public abstract class BasePresenter <V extends BaseView>  {
    protected V mBaseView;
    public void bind(V v) {
        this.mBaseView=v;
    }

    protected ArrayList<BaseModel> mBaseModels=new ArrayList<>();

    public BasePresenter() {
        initModel();
    }

    protected abstract void initModel();


    public void onDestroy() {
        //打断P  V的关系
        mBaseView=null;
        //掐断网络请求
        if (mBaseModels.size()>0){
            for (BaseModel mBaseModels:mBaseModels){
                mBaseModels.onDestroy();
            }
            mBaseModels.clear();
        }
    }
}
