package com.yueer.farmshop.kaoshitest.fragment.home.classes.mvp;

import com.yueer.farmshop.kaoshitest.base.BaseCallBack;
import com.yueer.farmshop.kaoshitest.base.BasePresenter;
import com.yueer.farmshop.kaoshitest.fragment.home.classes.DataBean;

public class Presenter_Data extends BasePresenter<View_Data> {

    private Model_Data mModel_data;

    @Override
    protected void initModel() {
        mModel_data = new Model_Data();
        mBaseModels.add(mModel_data);
    }

    public void getData(){
        mModel_data.getData(new BaseCallBack<DataBean>() {
            @Override
            public void onSuccess(DataBean bean) {
                if (bean!=null){
                    if (mBaseView!=null){
                        mBaseView.setData(bean);
                    }
                }
            }

            @Override
            public void onfial(String msg) {
                if (mBaseView!=null){
                    mBaseView.setData(msg);
                }
            }
        });
    }
}
