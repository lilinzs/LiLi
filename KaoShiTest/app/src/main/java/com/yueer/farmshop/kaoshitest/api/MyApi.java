package com.yueer.farmshop.kaoshitest.api;


import com.yueer.farmshop.kaoshitest.fragment.home.classes.DataBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface MyApi {
    public String DataUrl="http://106.13.63.54:8080/sys/";
    @GET("home.json")
    Observable<com.yueer.farmshop.kaoshitest.fragment.home.bean.DataBean> getData();

    @GET("classShop.json")
    Observable<DataBean> getClassesData();

}
