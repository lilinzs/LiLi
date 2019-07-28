package com.yueer.farmshop.kaoshitest.fragment.home.classes.mvp;

import com.yueer.farmshop.kaoshitest.base.BaseView;
import com.yueer.farmshop.kaoshitest.fragment.home.classes.DataBean;

public interface View_Data extends BaseView {
    void setData(DataBean bean);

    void setData(String msg);
}
