package com.yueer.farmshop.kaoshitest.baiduditu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yueer.farmshop.kaoshitest.R;

import java.util.ArrayList;

public class LvBaiDuAdapter extends BaseAdapter{


    private BaiDuDiTuActivity mBaiDuDiTuActivity;
    private ArrayList<String> mDiZhi;
    private MyViewHolder mMyViewHolder;

    public LvBaiDuAdapter(BaiDuDiTuActivity baiDuDiTuActivity, ArrayList<String> diZhi) {

        mBaiDuDiTuActivity = baiDuDiTuActivity;
        mDiZhi = diZhi;
    }

    @Override
    public int getCount() {
        return mDiZhi.size();
    }

    @Override
    public Object getItem(int position) {
        return mDiZhi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            mMyViewHolder = new MyViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.baidutidi_lv_item, null);
            mMyViewHolder.mTextView=convertView.findViewById(R.id.baidu_lv_i_tv);


            convertView.setTag(mMyViewHolder);

        }else {
            mMyViewHolder = (MyViewHolder) convertView.getTag();

        }
        mMyViewHolder.mTextView.setText(mDiZhi.get(position));

        //if (url!=null){
        //Glide.with(mContext).load(url).into(mMyViewHolder.img);

        //}
        return convertView;

    }
    class MyViewHolder {
        TextView mTextView;
    }

}




