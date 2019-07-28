package com.yueer.farmshop.kaoshitest.fragment.home.mvp;

import com.yueer.farmshop.kaoshitest.base.BaseView;
import com.yueer.farmshop.kaoshitest.fragment.home.bean.DataBean;

public interface View_Data extends BaseView {
    void setData(DataBean bean);

    void setData(String msg);
}
