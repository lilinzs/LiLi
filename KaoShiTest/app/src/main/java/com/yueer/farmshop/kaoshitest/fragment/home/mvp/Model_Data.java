package com.yueer.farmshop.kaoshitest.fragment.home.mvp;

import com.yueer.farmshop.kaoshitest.api.MyApi;
import com.yueer.farmshop.kaoshitest.base.BaseCallBack;
import com.yueer.farmshop.kaoshitest.base.BaseModel;
import com.yueer.farmshop.kaoshitest.fragment.home.bean.DataBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;

import io.reactivex.Observable;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Model_Data extends BaseModel{

    public void getData(final BaseCallBack<DataBean> baseCallBack) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyApi.DataUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        Observable<DataBean> data = retrofit.create(MyApi.class).getData();
        data.subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DataBean value) {
                        if (value!=null){
                            baseCallBack.onSuccess(value);
                        }else {
                            baseCallBack.onfial("空");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        baseCallBack.onfial(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });



    }
}
