package com.yueer.farmshop.kaoshitest.gaode;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.yueer.farmshop.kaoshitest.R;


import java.util.List;


public class SeachListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, Inputtips.InputtipsListener {


    private SearchView ssLocation;
    private ListView locationLv;
    private List<Tip> mCurrentTipList;
    private SeachListAdapter mIntipAdapter;
    public static final int RESULT_CODE_INPUTTIPS = 101;
    public static final int REQUEST_SUC = 1000;
    MapView mMapView = null;
    AMap aMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }
        setContentView(R.layout.activity_gaode);
        initView();
        mMapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

    private void initView() {

        mMapView = (MapView) findViewById(R.id.map);
        ssLocation = (SearchView) findViewById(R.id.ss_location);
        locationLv = (ListView) findViewById(R.id.location_lv);
        ssLocation.setOnQueryTextListener(this);
        //设置SearchView默认为展开显示
        ssLocation.setIconified(false);
        ssLocation.onActionViewExpanded();
        ssLocation.setIconifiedByDefault(true);
        ssLocation.setSubmitButtonEnabled(false);

    }

    @Override
    public void onGetInputtips(final List<Tip> list, int i) {
        // 正确返回
        if (i == REQUEST_SUC) {
            mCurrentTipList = list;
            mIntipAdapter = new SeachListAdapter(this, mCurrentTipList);

            locationLv.setAdapter(mIntipAdapter);
            mIntipAdapter.notifyDataSetChanged();
            locationLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(SeachListActivity.this, list.get(position).getName() + "", Toast.LENGTH_SHORT).show();

                 /*   LatLonPoint point = list.get(position).getPoint();
                    Log.e("tag", "onItemClick: "+point );
                    double latitude = point.getLatitude();//维度
                    double longitude = point.getLongitude();//经度
                    String name = list.get(position).getName();
                    Log.e("tag", "onItemClick+++++%%%%%%%+++++: "+latitude );
                    Log.e("tag", "onItemClick+++++%%%%%%%+++++:: "+longitude );
                    Log.e("tag", "onItemClick+++++%%%%%%%+++++:: "+name );
                    Intent intent = new Intent(SeachListActivity.this, GouWuCheActivity.class);
                    intent.putExtra("latitude",latitude);
                    intent.putExtra("longitude",longitude);
                    intent.putExtra("name",name);
                    startActivity(intent);*/
                    Intent intent = getIntent();
                    intent.putExtra("adCode", list.get(position).getAdcode());
                    intent.putExtra("address", list.get(position).getName());
                    LatLonPoint point = list.get(position).getPoint();
                    if (point != null) {
                        double latitude = point.getLatitude();//维度
                        double longitude = point.getLongitude();//经度
                        intent.putExtra("latitude", latitude + "");
                        intent.putExtra("longitude", longitude + "");
                    }
                    setResult(321, intent);
                    finish();

                }
                  /*  intent = getIntent();
                    intent.putExtra("adCode", list.get(position).getAdcode());
                    intent.putExtra("address", list.get(position).getName());
                    LatLonPoint point = list.get(position).getPoint();
                    if (point != null) {
                        double latitude = point.getLatitude();//维度
                        double longitude = point.getLongitude();//经度
                        intent.putExtra("latitude", latitude + "");
                        intent.putExtra("longitude", longitude + "");
                    }
                    setResult(2, intent);
                    finish();
                }*/
            });
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!TextUtils.isEmpty(newText)) {
            InputtipsQuery inputquery = new InputtipsQuery(newText, "");
            Inputtips inputTips = new Inputtips(SeachListActivity.this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
            locationLv.setVisibility(View.VISIBLE);
            mMapView.setVisibility(View.GONE);
        } else {
            // 如果输入为空  则清除 listView 数据
            if (mIntipAdapter != null && mCurrentTipList != null) {
                mCurrentTipList.clear();
                mIntipAdapter.notifyDataSetChanged();
                mMapView.setVisibility(View.VISIBLE);
                locationLv.setVisibility(View.GONE);
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

}
