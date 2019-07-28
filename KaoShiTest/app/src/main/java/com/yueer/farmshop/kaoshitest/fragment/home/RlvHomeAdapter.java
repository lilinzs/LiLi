package com.yueer.farmshop.kaoshitest.fragment.home;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.yueer.farmshop.kaoshitest.R;
import com.yueer.farmshop.kaoshitest.fragment.home.bean.DataBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RlvHomeAdapter extends RecyclerView.Adapter {
    private static final int BANNER = 0;
    private static final int TIME = 1;
    private static final int DATA = 3;
    private Context mContext;
    private List<DataBean.ShopListBeansBean> mShopListBeans;
    private DataBean.WonderfulTimeBean mWonderfulTime;
    private ArrayList<DataBean.BannerBeanBean> mBannerBean;
    private OnItemLongClicklistener mLongListener;
    private Long aLong;

    public RlvHomeAdapter(Context context, List<DataBean.ShopListBeansBean> shopListBeans, DataBean.WonderfulTimeBean wonderfulTime, List<DataBean.BannerBeanBean> bannerBean) {
        mContext = context;

        mShopListBeans = shopListBeans;
        mWonderfulTime = wonderfulTime;
        mBannerBean = (ArrayList<DataBean.BannerBeanBean>) bannerBean;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == BANNER) {
            return new MyHolder3(LayoutInflater.from(mContext).inflate(R.layout.fragment_home_rlv_banner, parent, false));
        } else if (viewType == TIME) {
            return new MyHolder2(LayoutInflater.from(mContext).inflate(R.layout.fragment_home_rlv_time, parent, false));

        } else {
            return new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.fragment_home_rlv_item, parent, false));

        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == BANNER) {
            MyHolder3 myHolder3 = (MyHolder3) holder;
            myHolder3.mBanner.setImages(mBannerBean).setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    DataBean.BannerBeanBean bannerBeanBean = (DataBean.BannerBeanBean) path;
                    for (int i = 0; i < mBannerBean.size(); i++) {

                        Glide.with(context).load(bannerBeanBean.getBannerIma_url()).into(imageView);
                    }
                }
            }).start();

            myHolder3.mBanner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    if (mLongListener!=null){
                        mLongListener.onItemClick(mBannerBean,position);
                    }
                }
            });

        } else if (itemViewType == TIME) {
            if (mBannerBean != null) {
                int mPosition = position - 1;

            }

            final MyHolder2 myHolder2 = (MyHolder2) holder;

            new CountDownUtil().start(Long.parseLong(mWonderfulTime.getStart_time() + "000"), new CountDownUtil.OnCountDownCallBack() {
                @Override
                public void onProcess(int day, int hour, int minute, int second) {
                    myHolder2.time_tv.setText(day + "天" + hour + "小时" + minute + "分钟" + second + "秒");
                    if (mWonderfulTime.getStart_time()*1000>System.currentTimeMillis()){
                        Toast.makeText(mContext, "倒计时完毕", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFinish() {
                    Toast.makeText(mContext, "倒计时完毕", Toast.LENGTH_SHORT).show();
                }
            });

//            stampToDate(mWonderfulTime.getStart_time()+"",((MyHolder2) holder).time_tv);
            Glide.with(mContext).load(mWonderfulTime.getWonderful_url()).into(myHolder2.time_img);

        } else {
            int mPosition = 0;
            if (mBannerBean != null && mWonderfulTime != null) {
                mPosition = position - 2;
            }

            MyHolder myHolder = (MyHolder) holder;
            myHolder.mContent.setText(mShopListBeans.get(mPosition).getShop_introd());
            myHolder.mTitle.setText(mShopListBeans.get(mPosition).getShop_name());
            myHolder.mMoney.setText("￥" + mShopListBeans.get(mPosition).getShop_pirce());
//            stampToDate(mShopListBeans.get(mPosition).getShop_ListingTime(), myHolder.mTime);
            aLong = Long.valueOf(mShopListBeans.get(mPosition).getShop_ListingTime());
            shangshi(mShopListBeans.get(mPosition).getShop_ListingTime(), myHolder.mTime);


//            myHolder.mTime.setText("上市时间："+mShopListBeans.get(mPosition).getShop_ListingTime());
            Glide.with(mContext).load(mShopListBeans.get(mPosition).getShop_image_url()).into(myHolder.mImg);

        }
    }

    private void shangshi(String shop_listingTime,TextView time) {

        long difference = System.currentTimeMillis() / 1000 - aLong;
        //相差天数：
        long day = difference / (3600 * 24 * 1000);

        if (day > 0 && day < 5) {
            time.setText("上市时间:" + day);
        } else {
            time.setText("上市时间:" + "很久以前");
        }

        //相差小时：
        long hour = difference / (3600 * 1000);
        if (hour > 0 && hour < 24) {
            time.setText("上市时间:" + hour + "小时前");
        }

        //相差分钟：
        long minute = difference / (60 * 1000);
        if (minute > 0 && minute < 60) {
            time.setText("上市时间:" + minute + "分钟前");
        }

        //相差秒：
        long second = difference / 1000;
        if (second > 0 && second < 60) {
            time.setText("上市时间:" + second + "秒前");
        }

    }

    public static String stampToDate(final String s, final TextView time_tv) {
        final String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //如果它本来就是long类型的,则不用写这一步
        long lt = new Long(s + "000");
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        Log.d("daojishi:", "stampToDate: " + res);
        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time_tv.setText("上市时间：" + res);
            }

            @Override
            public void onFinish() {

            }
        }.start();
        return res;


    }

    @Override
    public int getItemCount() {
        if (mBannerBean.size() > 0 || mWonderfulTime != null) {
            if (mBannerBean.size() > 0 && mWonderfulTime != null) {
                return mShopListBeans.size() + 2;
            } else {
                return mShopListBeans.size() + 1;
            }
        } else {
            return mShopListBeans.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return BANNER;
        } else if (position == 1) {
            return TIME;
        } else {
            return DATA;
        }
    }

    //    @NonNull
//    @Override
//    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.fragment_home_rlv_time,null));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
//        holder.mContent.setText(mShopListBeans.get(position).getShop_introd());
//        holder.mTitle.setText(mShopListBeans.get(position).getShop_name());
//        holder.mMoney.setText(mShopListBeans.get(position).getShop_pirce());
//        holder.mTime.setText(mShopListBeans.get(position).getShop_ListingTime());
//        Glide.with(mContext).load(mShopListBeans.get(position).getShop_image_url()).into(holder.mImg);
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return mShopListBeans.size();
//    }
//
    public class MyHolder extends RecyclerView.ViewHolder {

        private final TextView mContent;
        private final TextView mTitle;
        private final TextView mTime;
        private final TextView mMoney;
        private final ImageView mImg;

        public MyHolder(View itemView) {
            super(itemView);

            mContent = itemView.findViewById(R.id.home_i_content);
            mTime = itemView.findViewById(R.id.home_i_time);
            mTitle = itemView.findViewById(R.id.home_i_title);
            mMoney = itemView.findViewById(R.id.home_i_money);
            mImg = itemView.findViewById(R.id.home_i_img);

        }
    }

    public class MyHolder2 extends RecyclerView.ViewHolder {


        private final ImageView time_img;
        private final TextView time_tv;

        public MyHolder2(View itemView) {
            super(itemView);

            time_img = itemView.findViewById(R.id.f_home_img);
            time_tv = itemView.findViewById(R.id.f_home_time);


        }
    }

    public class MyHolder3 extends RecyclerView.ViewHolder {

        private final Banner mBanner;

        public MyHolder3(View itemView) {
            super(itemView);

            mBanner = itemView.findViewById(R.id.f_home_banner);

        }
    }

    public interface OnItemLongClicklistener {
        void onItemClick(ArrayList<DataBean.BannerBeanBean> mBannerBean , int position);
    }

    public void setOnItemLongClicklistener(OnItemLongClicklistener longListener) {
        mLongListener = longListener;
    }
}
