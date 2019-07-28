package com.yueer.farmshop.kaoshitest.fragment.home.mycenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yueer.farmshop.kaoshitest.R;
import com.yueer.farmshop.kaoshitest.baiduditu.BaiDuDiTuActivity;
import com.yueer.farmshop.kaoshitest.baiduditu.baidu.BaiDuActivity;
import com.yueer.farmshop.kaoshitest.base.BaseActivity;
import com.yueer.farmshop.kaoshitest.base.BasePresenter;
import com.yueer.farmshop.kaoshitest.db.DbDataBean;
import com.yueer.farmshop.kaoshitest.db.DbUtils;
import com.yueer.farmshop.kaoshitest.gaode.SeachListActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GouWuCheActivity extends BaseActivity {
    Double zongjia = 0.00;
    @BindView(R.id.gouwuche_rlv)
    ExpandableListView gouwucheRlv;
    @BindView(R.id.gouwuche_tv_jine)
    TextView gouwucheTvJine;
    @BindView(R.id.gouwuche_tv_dizhi)
    TextView gouwucheTvDizhi;
    @BindView(R.id.gouwuche_tv_jindu)
    TextView gouwucheTvJindu;
    @BindView(R.id.gouwuche_tv_weidu)
    TextView gouwucheTvWeidu;
    @BindView(R.id.gouwuche_i_bt_jiesuan)
    Button gouwucheibtjiesuan;

    //group数据
    private ArrayList<String> mGroupList = new ArrayList<>();
    //item数据
    private ArrayList<ArrayList<DbDataBean>> mItemSet = new ArrayList<ArrayList<DbDataBean>>();
    private List<DbDataBean> mDbDataBeans;
    private ArrayList<DbDataBean> mItemList1;
    private int mId;
    private ArrayList<Integer> mDbId = new ArrayList<>();

    @Override
    protected BasePresenter initBasePresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gou_wu_che;

    }

    @Override
    protected void initView() {
        //获取经纬度
        Intent intent = getIntent();
        String weidu = intent.getStringExtra("维度");
        String jingdu = intent.getStringExtra("经度");
        final String dizhi = intent.getStringExtra("地址");
        gouwucheTvJindu.setText("经度:" + jingdu);
        gouwucheTvWeidu.setText("维度:" + weidu);
        gouwucheTvDizhi.setText("地址:" + dizhi);

        EventBus.getDefault().register(this);


        gouwucheibtjiesuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gouwucheTvJine.getText().toString() != null && gouwucheTvJine.getText().toString().length() > 0) {
                    if (gouwucheTvDizhi.getText().toString() != null && gouwucheTvDizhi.getText().toString().length() > 0) {
                        Toast.makeText(GouWuCheActivity.this, "您消费" + gouwucheTvJine.getText().toString().trim() + "元", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GouWuCheActivity.this, "您还没有要填写地址", Toast.LENGTH_SHORT).show();
                        getDialog();
                    }

                } else {
                    Toast.makeText(GouWuCheActivity.this, "请选择商品哦qin", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //跳转地图
        gouwucheTvDizhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog();


            }
        });


    }

    private void getDialog() {
        new AlertDialog.Builder(this)
                .setTitle("温习提示")
                .setMessage("请选择地图类型:")
                .setPositiveButton("百度地图", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(GouWuCheActivity.this, BaiDuDiTuActivity.class);
                        startActivityForResult(intent, 123);
                        finish();
                    }
                })
                .setNegativeButton("高德地图", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(GouWuCheActivity.this, SeachListActivity.class);
                        startActivityForResult(intent, 123);
                        finish();
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == 321) {
            if (data != null) {
                String weidu = data.getStringExtra("latitude");
                String jingdu = data.getStringExtra("longitude");
                String dizhi = data.getStringExtra("address");
                Log.d("维度", "onActivityResult: " + weidu);
                gouwucheTvWeidu.setText("维度:" + weidu);
                gouwucheTvJindu.setText("进度:" + jingdu);
                gouwucheTvDizhi.setText("地址:" + dizhi);
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getData(int s) {
        if (s != 0) {
            Log.d("总价", "getData: "+s);
            zongjia = Double.valueOf(s);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
        EventBus.getDefault().unregister(this);

    }

    @Override
    protected void initData() {
        mDbDataBeans = DbUtils.getDbUtils().queryAll();


        ArrayList<Integer> integers = new ArrayList<>();
        for (int k = 0; k < mDbDataBeans.size(); k++) {
            String substring = mDbDataBeans.get(k).getShop_name().substring(0, 6);
            int frist_id = mDbDataBeans.get(k).getId();
            if (!integers.contains(frist_id)) {
                integers.add(frist_id);
                mGroupList.add(substring);
            }
        }
        Log.d("二级列表数据：", "addAll:            integers " + integers.size());
        for (int i = 0; i < integers.size(); i++) {
            ArrayList<DbDataBean> child = new ArrayList<>();
            for (int k = 0; k < mDbDataBeans.size(); k++) {
                if (integers.get(i) == mDbDataBeans.get(k).getId()) {
                    child.add(mDbDataBeans.get(k));
                }
            }
            mItemSet.add(child);
        }


        if (mDbDataBeans != null) {
//            RlvGouWuCheAdapter rlvGouWuCheAdapter = new RlvGouWuCheAdapter(mDbDataBeans, GouWuCheActivity.this);
//            gouwucheRlv.setAdapter(rlvGouWuCheAdapter);
//            gouwucheRlv.setLayoutManager(new LinearLayoutManager(GouWuCheActivity.this));


//            for (int i = 0; i < mDbDataBeans.size(); i++) {
//                mId = mDbDataBeans.get(i).getId();
//                String frist_name = mDbDataBeans.get(i).getFrist_name();
//                String shop_name = mDbDataBeans.get(i).getShop_name().substring(0, 6);
//                String shop_image_url = mDbDataBeans.get(i).getShop_image_url();
//                String shop_pirce = mDbDataBeans.get(i).getShop_pirce();
//                String shop_introd = mDbDataBeans.get(i).getShop_introd();
//                Log.d("数据库的数据:", "initData: " + mDbDataBeans.toString());
//
//                mDbId.add(mId);//id
//
//
//                mGroupList.add(shop_name);//父
//
//
//                mItemList1 = new ArrayList<>();
//                mItemList1.add(mDbDataBeans.get(i));
////                itemList1.add(shop_image_url);
////                itemList1.add(shop_pirce);
////                itemList1.add(shop_introd);
//                mItemSet.add(mItemList1);
//                Log.d("用Id查找的数据：", "initData: " + "id:" + mId + "fu" + mGroupList + "zi" + mItemSet);
//
//
//            }
//
//            for (int i = 0; i < mGroupList.size() - 1; i++) {
//                for (int j = mGroupList.size() - 1; j > i; j--) {
//                    if (mGroupList.get(j).equals(mGroupList.get(i))) {
//                        mGroupList.remove(j);
//                    }
//                }
//            }
//
//            for (int i = 0; i < mItemSet.size() - 1; i++) {
//                for (int j = mItemSet.size() - 1; j > i; j--) {
//                    if (mItemSet.get(j).equals(mItemSet.get(i))) {
//                        mItemSet.remove(j);
//                    }
//                }
//            }
//
//            for (int i = 0; i < mDbId.size() - 1; i++) {
//                for (int j = mDbId.size() - 1; j > i; j--) {
//                    if (mDbId.get(j) == (mDbId.get(i))) {
//                        mDbId.remove(j);
//                    }
//                }
//            }


            Log.d("父列表", "父列表: " + mGroupList + "----------------------------子列表:" + mItemSet + "========" + "mItemList1:" + mItemList1 + "------------------用Id查找的数据" + mId);

            initErji(mGroupList, mItemSet);
        }
    }

    private void initErji(ArrayList<String> groupList, ArrayList<ArrayList<DbDataBean>> itemSet) {
        Log.d("子列表item", "initErji: " + itemSet);
        MyAdapter adapter = new MyAdapter(this, groupList, itemSet);
        gouwucheRlv.setAdapter(adapter);

        adapter.setOnAddclick(new MyAdapter.OnAddclick() {
            @Override
            public void addprice(int[] text, Double price) {
                // price 当前对象的价格
                //  pricecount 要购买的数量
                int pricecount = text[0];


                zongjia += price;
                gouwucheTvJine.setText("金额：" + String.valueOf(zongjia) + " $");
            }
        });

        adapter.setOnDelectclick(new MyAdapter.OnDelectclick() {
            @Override
            public void delectprice(int[] text, Double price) {
                // price 当前对象的价格
                //  pricecount 要购买的数量
                int pricecount = text[0];

                zongjia -= price;
                if (zongjia <= 0) {
                    gouwucheTvJine.setText("金额：" + "0.00 $");
                    return;
                }

                gouwucheTvJine.setText("金额：" + String.valueOf(zongjia) + " $");
            }
        });


    }
}
    /*private void initRlv() {
//        RlvGouWuCheAdapter rlvGouWuCheAdapter = new RlvGouWuCheAdapter(this);
//        gouwucheRlv.setAdapter(rlvGouWuCheAdapter);
//        gouwucheRlv.setLayoutManager(new LinearLayoutManager(this));


    }*/


//二级列表适配器
class MyAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<String> mGroup;
    private ArrayList<ArrayList<DbDataBean>> mItemList;
    private final LayoutInflater mInflater;
    private OnAddclick onAddclick;
    private OnDelectclick onDelectclick;

    public void setOnAddclick(OnAddclick onAddclick) {
        this.onAddclick = onAddclick;
    }

    public void setOnDelectclick(OnDelectclick onDelectclick) {
        this.onDelectclick = onDelectclick;
    }


    public interface OnAddclick {
        void addprice(int[] text, Double price);
    }

    public interface OnDelectclick {
        void delectprice(int[] text, Double price);
    }

    public MyAdapter(Context context, ArrayList<String> group, ArrayList<ArrayList<DbDataBean>> itemList) {
        this.mContext = context;
        this.mGroup = group;
        this.mItemList = itemList;
        mInflater = LayoutInflater.from(context);
    }

    //父项的个数
    @Override
    public int getGroupCount() {
        return mGroup.size();
    }

    //某个父项的子项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        Log.d("子条目:", "getChildrenCount: " + mItemList.get(groupPosition).size() + "=======mItemList" + mItemList.get(groupPosition));
        return mItemList.get(groupPosition).size();
    }

    //获得某个父项
    @Override
    public Object getGroup(int groupPosition) {
        return mGroup.get(groupPosition);
    }

    //获得某个子项
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mItemList.get(groupPosition).get(childPosition);
    }

    //父项的Id
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //子项的id

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //获取父项的view
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_group, parent, false);
        }
        String group = mGroup.get(groupPosition);
        TextView tvGroup = (TextView) convertView.findViewById(R.id.tv_group);
        tvGroup.setText(group);
        return convertView;
    }

    //获取子项的view
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final DbDataBean dbDataBean = mItemList.get(groupPosition).get(childPosition);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.gouwuche_rlv_item, parent, false);
        }

        final TextView mMoney = convertView.findViewById(R.id.gouwuche_i_ed_money);
        TextView mName = convertView.findViewById(R.id.gouwuche_i_ed_name);
        final TextView mShuliang = convertView.findViewById(R.id.gouwuche_i_shuliang);
        TextView mJia = convertView.findViewById(R.id.gouwuche_i_jia);
        TextView mJian = convertView.findViewById(R.id.gouwuche_i_jian);
        ImageView mImg = convertView.findViewById(R.id.gouwuche_i_img);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, dbDataBean.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        Log.d("子条目数据:", "getChildView: " + dbDataBean);
        mName.setText(dbDataBean.getShop_name());
        mMoney.setText(dbDataBean.getShop_pirce());
        Glide.with(mContext).load(dbDataBean.getShop_image_url()).into(mImg);
        EventBus.getDefault().postSticky(dbDataBean.getShop_pirce());

        final int[] count = {1};
        mJia.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                count[0]++;
                mShuliang.setText(count[0] + "");
                onAddclick.addprice(count, Double.parseDouble(dbDataBean.getShop_pirce()));
            }
        });
        mJian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count[0] > 0) {
                    if (count[0] == 1) {
                        //删除
                        DbUtils.getDbUtils().delete(dbDataBean);
                        mItemList.get(groupPosition).remove(childPosition);
                        notifyDataSetChanged();
                    } else {
                        count[0]--;
                        mShuliang.setText(count[0] + "");
                        onDelectclick.delectprice(count, Double.parseDouble(dbDataBean.getShop_pirce()));
                    }
                }
            }
        });



/*        mName.setText(mItemList.get(groupPosition).get(0));
        Glide.with(mContext).load(mItemList.get(groupPosition).get(1)).into(mImg);
        mMoney.setText(mItemList.get(groupPosition).get(2));*/
//            RlvGouWuCheAdapter rlvGouWuCheAdapter = new RlvGouWuCheAdapter(mDbDataBeans, GouWuCheActivity.this);
//            rlv.setAdapter(rlvGouWuCheAdapter);
//            rlv.setLayoutManager(new LinearLayoutManager(GouWuCheActivity.this));
        return convertView;

    }

    //子项是否可选中,如果要设置子项的点击事件,需要返回true
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
