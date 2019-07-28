package com.yueer.farmshop.kaoshitest.fragment.home.mycenter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yueer.farmshop.kaoshitest.R;
import com.yueer.farmshop.kaoshitest.db.DbDataBean;
import com.yueer.farmshop.kaoshitest.db.DbUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class RlvGouWuCheAdapter extends RecyclerView.Adapter<RlvGouWuCheAdapter.MyHolder> {


    private static final int FIERTNAME = 0;
    private static final int ITEM = 2;
    private List<DbDataBean> mData;
    private GouWuCheActivity mGouWuCheActivity;
    private String mTitle2;

    public RlvGouWuCheAdapter(List<DbDataBean> child, GouWuCheActivity gouWuCheActivity) {
        mData = child;

        mGouWuCheActivity = gouWuCheActivity;

    }

    @NonNull
    @Override
    public RlvGouWuCheAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(mGouWuCheActivity).inflate(R.layout.gouwuche_rlv_item, null));
    }



    @Override
    public void onBindViewHolder(@NonNull final RlvGouWuCheAdapter.MyHolder holder, final int position) {
        holder.mName.setText(mData.get(position).getShop_name());
        holder.mMoney.setText(mData.get(position).getShop_pirce());
        Glide.with(mGouWuCheActivity).load(mData.get(position).getShop_image_url()).into(holder.mImg);
        String title = mData.get(position).getShop_name().substring(0, 6);
        int posi = position + 1;
        if (posi == mData.size()) {//size比索引大于1   那就是索引1 的位置
            mTitle2 = mData.get(position).getShop_name().substring(0, 6);
        } else {
            mTitle2 = mData.get(posi).getShop_name().substring(0, 6);
        }

        holder.mFirstName.setText(title);
        if (title.equals(mTitle2)) {
            holder.mFirstName.setVisibility(View.GONE);
        }

        holder.mJia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num2 = holder.mShuliang.getText().toString();
                int parseInt1 = Integer.parseInt(num2);
                parseInt1 += 1;
                holder.mShuliang.setText(parseInt1 + "");
            }
        });

        holder.mJian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //减号监听   mTvNum 是 数量  一个TextView
                String num1 = holder.mShuliang.getText().toString();
                int parseInt = Integer.parseInt(num1);

                DbDataBean dbDataBean = mData.get(position);

                if (parseInt == 0) {
                    //删除
                    DbUtils.getDbUtils().delete(dbDataBean);
                    mData.remove(position);
                    notifyDataSetChanged();
                } else {
                    parseInt -= 1;
                    holder.mShuliang.setText(parseInt + "");
                    notifyDataSetChanged();

                }



            }
        });
        holder.mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num1 = holder.mShuliang.getText().toString();
                int parseInt = Integer.parseInt(num1);
                int a = parseInt * 44;
                Log.d("zongjine :" + num1, "onBindViewHolder: " + a);

                EventBus.getDefault().postSticky(a);
            }
        });


    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return FIERTNAME;
        } else {
            return ITEM;
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private final TextView mMoney;
        private final TextView mName;
        private final TextView mShuliang;
        private final TextView mJia;
        private final TextView mJian;
        private final ImageView mImg;
        private final TextView mFirstName;

        public MyHolder(View itemView) {
            super(itemView);
            mMoney = itemView.findViewById(R.id.gouwuche_i_ed_money);
            mName = itemView.findViewById(R.id.gouwuche_i_ed_name);
            mShuliang = itemView.findViewById(R.id.gouwuche_i_shuliang);
            mJia = itemView.findViewById(R.id.gouwuche_i_jia);
            mJian = itemView.findViewById(R.id.gouwuche_i_jian);
            mImg = itemView.findViewById(R.id.gouwuche_i_img);
            mFirstName = itemView.findViewById(R.id.gouwuche_r_i_firtName);


        }
    }



}

  /*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder mViewHolder = null;
    if (convertView == null) {
    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shopping_trolley, null);
    mViewHolder = new ViewHolder();
    mViewHolder.mTvDianpu = convertView.findViewById(R.id.tv_dianpu);
    mViewHolder.mImgUrl = convertView.findViewById(R.id.img_url);
    mViewHolder.mTvName = convertView.findViewById(R.id.tv_name);
    mViewHolder.mTvPirce = convertView.findViewById(R.id.tv_pirce);
    mViewHolder.mBtnJian = convertView.findViewById(R.id.btn_jian);
    mViewHolder.mEtCount = convertView.findViewById(R.id.et_count);
    mViewHolder.mBtnZeng = convertView.findViewById(R.id.btn_zeng);
    convertView.setTag(mViewHolder);
    } else {
    mViewHolder = (ViewHolder) convertView.getTag();
    }

    String title = mList.get(position).getShop_name().substring(0, 6);
    int posi = position + 1;
    if (posi == mList.size()) {
    mTitle2 = mList.get(position).getShop_name().substring(0, 6);
    } else {
    mTitle2 = mList.get(posi).getShop_name().substring(0, 6);
    }
    mViewHolder.mTvDianpu.setText(title);
    if (title.equals(mTitle2)) {
    mViewHolder.mTvDianpu.setVisibility(View.GONE);
    }
    mViewHolder.mTvName.setText(mList.get(position).getShop_name());
    mViewHolder.mTvPirce.setText(mList.get(position).getShop_pirce());
    Glide.with(mContext).load(mList.get(position).getShop_image_url()).into(mViewHolder.mImgUrl);
    final ViewHolder finalMViewHolder = mViewHolder;
    mViewHolder.mBtnJian.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
    String s = finalMViewHolder.mEtCount.getText().toString();
    int count = Integer.parseInt(s);
    if (count == 0) {
    Toast.makeText(mContext, "亲，你没有点那啥数吗？最起码买一个啊！！", Toast.LENGTH_SHORT).show();
    } else {
    finalMViewHolder.mEtCount.setText(count-- + "");
    }
    }
    });
    mViewHolder.mBtnZeng.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
    String s = finalMViewHolder.mEtCount.getText().toString();
    int count = Integer.parseInt(s);
    finalMViewHolder.mEtCount.setText(count++ + "");
    }
    });
    return convertView;
    }*/