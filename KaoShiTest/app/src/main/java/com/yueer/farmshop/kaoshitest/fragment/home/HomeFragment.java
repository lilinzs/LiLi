package com.yueer.farmshop.kaoshitest.fragment.home;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.yueer.farmshop.kaoshitest.R;
import com.yueer.farmshop.kaoshitest.base.BaseFragment;
import com.yueer.farmshop.kaoshitest.fragment.home.bean.DataBean;
import com.yueer.farmshop.kaoshitest.fragment.home.mvp.Presenter_Data;
import com.yueer.farmshop.kaoshitest.fragment.home.mvp.View_Data;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment<View_Data, Presenter_Data> implements View_Data {



    @BindView(R.id.f_home_rlv)
    RecyclerView fHomeRlv;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        mBasePresenter.getData();

    }

    @Override
    protected Presenter_Data initBasePresenter() {
        return new Presenter_Data();
    }

//    private void initTime(final DataBean.WonderfulTimeBean wonderfulTime) {
//        Glide.with(getContext()).load(wonderfulTime.getWonderful_url()).into(fHomeImg);
//
//        /*Animation rotate = new RotateAnimation( 0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f );
//        LinearInterpolator lin = new LinearInterpolator();
//        rotate.setInterpolator( lin ); //设置插值器
//        rotate.setDuration( 1000 );//设置动画持续周期
//        rotate.setRepeatCount( -1 );//设置重复次数
//        rotate.setFillAfter( true );
//        mImage.setAnimation( rotate );
//        mImage.startAnimation( rotate );
//        */
//
//        new CountDownTimer(5000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                fHomeTime.setText(wonderfulTime.getStart_time()+ "秒");
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        }.start();
//    }

//    private void initVpBanner(final List<DataBean.BannerBeanBean> bannerBean) {
//        ArrayList<Integer> integers = new ArrayList<>();
//        integers.add(R.drawable.loading_01);
//        integers.add(R.drawable.loading_01);
//        integers.add(R.drawable.loading_01);
//        fHomeBanner.setImages(bannerBean).setImageLoader(new ImageLoader() {
//            @Override
//            public void displayImage(Context context, Object path, ImageView imageView) {
//                DataBean.BannerBeanBean bannerBeanBean= (DataBean.BannerBeanBean) path;
//                for (int i = 0; i < bannerBean.size(); i++) {
//
//                    Glide.with(context).load(bannerBean.get(i).getBannerIma_url()).into(imageView);
//                }
//            }
//        }).start();
//    }

    @Override
    public void setData(DataBean bean) {
        Log.d("数据", "setData: "+bean.toString());
        if (bean != null) {
            List<DataBean.BannerBeanBean> bannerBean = bean.getBannerBean();

            DataBean.WonderfulTimeBean wonderfulTime = bean.getWonderfulTime();
            List<DataBean.ShopListBeansBean> shopListBeans = bean.getShopListBeans();

//            //轮播图
//            initVpBanner(bannerBean);
//            //到计时
//            initTime(wonderfulTime);
            //Rlv
            initRlv(shopListBeans,wonderfulTime,bannerBean);
        }

    }

    private void initRlv(List<DataBean.ShopListBeansBean> shopListBeans, DataBean.WonderfulTimeBean wonderfulTime, List<DataBean.BannerBeanBean> bannerBean) {
        RlvHomeAdapter rlvHomeAdapter = new RlvHomeAdapter(getContext(),shopListBeans,wonderfulTime,bannerBean);
        fHomeRlv.setAdapter(rlvHomeAdapter);
        fHomeRlv.setLayoutManager(new LinearLayoutManager(getContext()));

        rlvHomeAdapter.setOnItemLongClicklistener(new RlvHomeAdapter.OnItemLongClicklistener() {
            @Override
            public void onItemClick(ArrayList<DataBean.BannerBeanBean> mBannerBean, int position) {
                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("data",mBannerBean);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
    }

    @Override
    public void setData(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
