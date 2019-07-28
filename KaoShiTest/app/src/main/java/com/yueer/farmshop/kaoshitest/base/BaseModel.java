package com.yueer.farmshop.kaoshitest.base;


import io.reactivex.disposables.CompositeDisposable;

public class BaseModel {
    protected CompositeDisposable mCompositeDisposable=new CompositeDisposable();
    public void onDestroy() {
        //切断所有的Disposable对象
        mCompositeDisposable.clear();
    }
}
