package com.yueer.farmshop.kaoshitest.fragment.home.classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yueer.farmshop.kaoshitest.R;
import com.yueer.farmshop.kaoshitest.db.DbDataBean;
import com.yueer.farmshop.kaoshitest.db.DbUtils;

import java.util.ArrayList;
import java.util.List;

public class RlvClassAdapter extends RecyclerView.Adapter<RlvClassAdapter.MyHolder> {
    private ArrayList<DataBean.ClassFristShopBean> mClassFristShop;
    private int mPosition;
    private Context mContext;
    private List<DataBean.ClassFristShopBean.ShopListBeansBean> mShopListBeans;
    private OnItemClicklistener mListener;
    private ArrayList<CheckBox> mCheckBoxes=new ArrayList<>();

    public RlvClassAdapter(ArrayList<DataBean.ClassFristShopBean> classFristShop, int position, Context context) {

        mClassFristShop = classFristShop;
        mPosition = position;
        mContext = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.fragment_classes_rlv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        mShopListBeans = mClassFristShop.get(mPosition).getShopListBeans();

        DbDataBean query = DbUtils.getDbUtils().query(mShopListBeans.get(position).getShop_name());
        if (query == null) {
            holder.mCb.setChecked(false);
        } else {
            holder.mCb.setChecked(true);
        }

        holder.mJiege.setText(mShopListBeans.get(position).getShop_pirce());
        holder.mJieshao.setText(mShopListBeans.get(position).getShop_introd());
        holder.mName.setText(mShopListBeans.get(position).getShop_name());
        Glide.with(mContext).load(mShopListBeans.get(position).getShop_image_url()).into(holder.mImg);

        holder.mCb.setChecked(mShopListBeans.get(position).isBoolean());//jie


        /*holder.mCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(holder.mCb.isChecked(), mShopListBeans, position);
                }
            }
        });*/

        holder.mCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mListener != null) {
                    mListener.onItemClick(holder.mCb.isChecked(), mShopListBeans, position);
                }
            }
        });
        for (int i = 0; i < mShopListBeans.size(); i++) {
            mCheckBoxes.add(holder.mCb);
        }
    }

    @Override
    public int getItemCount() {
        return mClassFristShop.get(mPosition).getShopListBeans().size();
    }

    public void addCb(boolean hidden, int newPosition) {
        for (int i = 0; i < mCheckBoxes.size(); i++) {

            mCheckBoxes.get(newPosition).setChecked(hidden);
        }
    }


    public class MyHolder extends RecyclerView.ViewHolder {

        private final TextView mName;
        private final TextView mJieshao;
        private final TextView mJiege;
        private final ImageView mImg;
        private final CheckBox mCb;

        public MyHolder(View itemView) {
            super(itemView);
            mCb = itemView.findViewById(R.id.f_class_i_cb);
            mImg = itemView.findViewById(R.id.f_class_i_img);
            mJiege = itemView.findViewById(R.id.f_class_i_jiage);
            mJieshao = itemView.findViewById(R.id.f_class_i_jieshao);
            mName = itemView.findViewById(R.id.f_class_i_name);


        }
    }

    public interface OnItemClicklistener {
        void onItemClick(boolean a, List<DataBean.ClassFristShopBean.ShopListBeansBean> data, int position);
    }

    public void setOnItemClicklistener(OnItemClicklistener listener) {
        mListener = listener;
    }
}
