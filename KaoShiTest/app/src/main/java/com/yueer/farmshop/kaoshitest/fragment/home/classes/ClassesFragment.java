package com.yueer.farmshop.kaoshitest.fragment.home.classes;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yueer.farmshop.kaoshitest.R;
import com.yueer.farmshop.kaoshitest.base.BaseFragment;
import com.yueer.farmshop.kaoshitest.base.BasePresenter;
import com.yueer.farmshop.kaoshitest.db.DbDataBean;
import com.yueer.farmshop.kaoshitest.db.DbUtils;
import com.yueer.farmshop.kaoshitest.fragment.home.HomeFragment;
import com.yueer.farmshop.kaoshitest.fragment.home.RlvHomeAdapter;
import com.yueer.farmshop.kaoshitest.fragment.home.classes.mvp.Presenter_Data;
import com.yueer.farmshop.kaoshitest.fragment.home.classes.mvp.View_Data;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;
import q.rorbin.verticaltablayout.widget.TabView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassesFragment extends BaseFragment<View_Data, Presenter_Data> implements View_Data {


    @BindView(R.id.tab)
    q.rorbin.verticaltablayout.VerticalTabLayout tab;
    @BindView(R.id.fl_container)
    RecyclerView mRlv;
    private FragmentManager mManager;
    private FragmentTransaction mTransaction;
    private ArrayList<Fragment> mFragments;
    private RlvClassAdapter mRlvClassAdapter;
    private int mNewPosition;
    private List<DataBean.ClassFristShopBean.ShopListBeansBean> mData;
    private String mTab_name;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_classes;
    }

    @Override
    protected void initData() {
        mBasePresenter.getData();

    }

    @Override
    protected void initView() {

    }

    @Override
    protected Presenter_Data initBasePresenter() {
        return new Presenter_Data();
    }

    private void initListenner(final ArrayList<DataBean.ClassFristShopBean> classFristShop) {
        mRlvClassAdapter = new RlvClassAdapter(classFristShop, 0, getContext());
        mRlv.setAdapter(mRlvClassAdapter);
        mRlv.setLayoutManager(new LinearLayoutManager(getContext()));
        tab.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {


            @Override
            public void onTabSelected(TabView tab, int position) {

//                mManager = getChildFragmentManager();
//                mTransaction = mManager.beginTransaction();
//                mFragments = new ArrayList<>();
//                mFragments.add(new Classer_Shop_Fragment(position,classFristShop));
//                //默认显示
//                mTransaction.add(R.id.fl_container, mFragments.get(0));
//                mTransaction.commit();
                mRlvClassAdapter = new RlvClassAdapter(classFristShop, position, getContext());
                mRlv.setAdapter(mRlvClassAdapter);
                mRlv.setLayoutManager(new LinearLayoutManager(getContext()));

                mRlvClassAdapter.setOnItemClicklistener(new RlvClassAdapter.OnItemClicklistener() {


                    @Override
                    public void onItemClick(boolean a, List<DataBean.ClassFristShopBean.ShopListBeansBean> data, int position) {
                        mNewPosition = position;
                        mData = data;
                        DbDataBean dbDataBean = new DbDataBean();
                        dbDataBean.setNewId(Long.valueOf(position));
                        dbDataBean.setShop_image_url(data.get(position).getShop_image_url());
                        dbDataBean.setShop_introd(data.get(position).getShop_introd());
                        dbDataBean.setShop_name(data.get(position).getShop_name());
                        dbDataBean.setShop_pirce(data.get(position).getShop_pirce());
                        dbDataBean.setId(data.get(position).getFrist_ID());
                        if (mTab_name!=null){

                            dbDataBean.setFrist_name(mTab_name);
                        }
                        if (a) {

                            long insert = DbUtils.getDbUtils().insert(dbDataBean);
                            if (insert >= 0) {
                                Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "添加失败", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            boolean delete = DbUtils.getDbUtils().delete(dbDataBean);
                            if (delete) {
                                Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }


            @Override
            public void onTabReselected(TabView tab, int position) {

            }
        });
    }


    @Override
    public void setData(DataBean bean) {
        if (bean != null) {
            ArrayList<DataBean.ClassFristShopBean> classFristShop = (ArrayList<DataBean.ClassFristShopBean>) bean.getClassFristShop();

            initTabData(classFristShop);
            initListenner(classFristShop);
//            initFrag(classFristShop);
        }

    }

    private void initFrag( ArrayList<DataBean.ClassFristShopBean> classFristShop) {
        for (int i = 0; i < classFristShop.size(); i++) {

        }
    }

    private void initTabData(final List<DataBean.ClassFristShopBean> list) {

        tab.setTabAdapter(new TabAdapter() {
            //设置个数
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public ITabView.TabBadge getBadge(int position) {
                return null;
            }

            @Override
            public ITabView.TabIcon getIcon(int position) {
                return null;
            }

            //给tab栏设置标题
            @Override
            public ITabView.TabTitle getTitle(int position) {
                mTab_name = list.get(position).getFrist_name();

                ITabView.TabTitle title = new ITabView.TabTitle.Builder()
                        .setContent(list.get(position).getFrist_name())
                        .setTextColor(Color.RED, Color.BLACK)
                        .build();
                return title;
            }


            //设置背景
            @Override
            public int getBackground(int position) {
                return 0;
            }
        });

    }

    @Override
    public void setData(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {
            DbDataBean query = DbUtils.getDbUtils().query(mData.get(mNewPosition).getShop_name());
            if (query == null) {
                boolean a=false;
                mRlvClassAdapter.addCb(a,mNewPosition);

            }
        }
    }
}
//app:indicator_color 指示器颜色
//        app:indicator_width 指示器宽度
//        app:indicator_gravity 指示器位置
//        app:indicator_corners 指示器圆角
//        app:tab_mode Tab高度模式
//        app:tab_height Tab高度
//        app:tab_margin Tab间距


//
//            //适配器
//                        @Override
//                        public void onBindViewHolder(@NonNull final RlvHomeAdapter.MyHolder holder, final int position) {
//                            mNewPosition = position;
//                            DbDataBean query = DbUtils.getDbUtils().query(mResults.get(position).getUrl());
//                            if (query==null){
//                                holder.mCb.setChecked(false);
//                            }else {
//                                holder.mCb.setChecked(true);
//                            }
//                            holder.mDesc.setText(mResults.get(position).getSource());
//                            holder.mTitle.setText(mResults.get(position).getType());
//                            Glide.with(mContext).load(mResults.get(position).getUrl()).into(holder.mImg);
//                            final MyDataBean.ResultsBean resultsBean = mResults.get(position);
//                            holder.mCb.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    if (mLongListener!=null){
//                                        mLongListener.onItemClick(holder.mCb.isChecked(),resultsBean,position);
//                                    }
//                                }
//                            });
//                            for (int i = 0; i < mResults.size(); i++) {
//                                mCheckBoxes.add(holder.mCb);
//                            }
//
//                        }
//
//
//
//                        mRlvHomeAdapter.setOnItemLongClicklistener(new RlvHomeAdapter.OnItemLongClicklistener() {
//                            @Override
//                            public void onItemClick(Boolean cb, MyDataBean.ResultsBean data, int position) {
//                                mNewPosition = position;
//                                DbDataBean dbDataBean = new DbDataBean();
//                                dbDataBean.setNewId(Long.valueOf(position));
//                                dbDataBean.setSource(data.getSource());
//                                dbDataBean.setType(data.getType());
//                                dbDataBean.setUrl(data.getUrl());
//                                if (cb) {
//
//                                    long insert = DbUtils.getDbUtils().insert(dbDataBean);
//                                    if (insert >= 0) {
//                                        Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(getContext(), "添加失败", Toast.LENGTH_SHORT).show();
//                                    }
//                                } else {
//                                    boolean delete = DbUtils.getDbUtils().delete(dbDataBean);
//                                    if (delete) {
//                                        Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
//
//                                    } else {
//                                        Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            }
//                        });
//
//
//                        @Override
//                        public void onHiddenChanged(boolean hidden) {
//                            super.onHiddenChanged(hidden);
//                            if (hidden) {
//
//                            } else {
//                                DbDataBean query = DbUtils.getDbUtils().query(mResults.get(mNewPosition).getUrl());
//                                if (query == null) {
//                                    mRlvHomeAdapter.addCb(a);
//
//                                }
//                            }
//                        }