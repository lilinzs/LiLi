package com.yueer.farmshop.kaoshitest.baiduditu.baidu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.yueer.farmshop.kaoshitest.R;
import com.yueer.farmshop.kaoshitest.fragment.home.mycenter.GouWuCheActivity;


import java.util.List;

public class BaiDuActivity extends AppCompatActivity implements OnGetGeoCoderResultListener
        , OnGetSuggestionResultListener,
        BaiduMap.OnMapStatusChangeListener, BDLocationListener, TextWatcher {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    public BDLocationListener myListener = new MyLocationListener();
    private RadioGroup mRadioGroupGender;
    private LatLng latLng;
    private boolean isFirstLoc = true;
    private GeoCoder mSearch;
    private ListView searchPois;
    private List<PoiInfo> poiInfos;
    private EditText searchAddress;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private LocationClient mLocClient;
    private ListView lv_near_address;
    private SuggestionSearch mSuggestionSearch;
    private GeoCoder geoCoder;
    private AutoCompleteTextView keyWorldsView;
    private ArrayAdapter<String> sugAdapter;

    private static final String TAG = "BaiDuActivity";
    private String city;
    private long stopTome;
    private double mLatitude;
    private double mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        多实例显示地图
//        setContentView(R.layout.activity_multi_map_demo);
//        initView2();


//        单独实现地图
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a);
        initView();


    }

    //实现BDLocationListener接口,BDLocationListener为结果监听接口，异步获取定位结果
    public class MyLocationListener implements BDLocationListener {


        @Override
        public void onReceiveLocation(BDLocation location) {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            // 当不需要定位图层时关闭定位图层
            //mBaiduMap.setMyLocationEnabled(false);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    // GPS定位结果
                    Toast.makeText(BaiDuActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    // 网络定位结果
                    Toast.makeText(BaiDuActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();

                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                    // 离线定位结果
                    Toast.makeText(BaiDuActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();

                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    Toast.makeText(BaiDuActivity.this, "服务器错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    Toast.makeText(BaiDuActivity.this, "网络错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    Toast.makeText(BaiDuActivity.this, "手机模式错误，请检查是否飞行", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mRadioGroupGender = (RadioGroup) findViewById(R.id.radioGroup_gender);
        mBaiduMap = mMapView.getMap();
        typeOnClien();


        searchAddress = (EditText) findViewById(R.id.geocodekey);
        searchPois = (ListView) findViewById(R.id.mai_search_pois);
        searchAddress.addTextChangedListener(this);
        searchPois.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                searchPois.setVisibility(View.GONE);
                if (poiInfos != null) {
                    if (poiInfos.get(position) != null) {
                        LatLng location = poiInfos.get(position).location;
                        if (location != null) {
                            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(location, 18);
                            mBaiduMap.animateMapStatus(msu);
                        }
                    }
                }

            }
        });


        MapStatus mapStatus = new MapStatus.Builder().zoom(15).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        // 地图状态改变相关监听
        mBaiduMap.setOnMapStatusChangeListener(this);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位图层显示方式
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, null));
        mLocClient = new LocationClient(this);
        // 注册定位监听
        mLocClient.registerLocationListener(this);

        // 定位选项
        LocationClientOption option = new LocationClientOption();
        /**
         * coorType - 取值有3个： 返回国测局经纬度坐标系：gcj02 返回百度墨卡托坐标系 ：bd09 返回百度经纬度坐标系
         * ：bd09ll//可选，默认gcj02，设置返回的定位结果坐标系
         */
        option.setCoorType("bd09ll");
        // 设置是否需要地址信息，默认为无地址可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        // 设置是否需要返回位置语义化信息，可以在BDLocation.getLocationDescribe()中得到数据，ex:"在天安门附近"，
        // 可以用作地址信息的补充   可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        option.setIsNeedLocationDescribe(true);
        // 设置是否需要返回位置POI信息，可以在BDLocation.getPoiList()中得到数据
        // .getLocationDescribe里得到，结果类似于“在北京天安门附近”
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        /**
         * 设置定位模式 Battery_Saving 低功耗模式 Device_Sensors 仅设备(Gps)模式 Hight_Accuracy
         * 高精度模式
         /   */
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 设置是否打开gps进行定位
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        int span = 1000;
        // 设置扫描间隔，单位是毫秒 当<1000(1s)时，定时定位无效
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(span);

        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(true);
        option.setIgnoreKillProcess(false);

        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        // 设置 LocationClientOption
        mLocClient.setLocOption(option);
        //图片点击事件，回到定位点
        mLocClient.requestLocation();
        // 开始定位
        mLocClient.start();
        lv_near_address = (ListView) findViewById(R.id.lv_near_addresss);
//        // 初始化搜索模块，注册搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        //  接口OnGetSuggestionResultListener    接口里的方法onGetSuggestionResult
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);


        keyWorldsView = (AutoCompleteTextView) findViewById(R.id.et_search);
        sugAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        keyWorldsView.setAdapter(sugAdapter);
        /**
         * 当输入关键字变化时，动态更新建议列表
         */
        keyWorldsView.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length() <= 0) {
                    return;
                }
                String city = "杭州";
                String cityone = "安徽省";
                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(cs.toString()).city(city));
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(cs.toString()).city(cityone));
            }
        });

        keyWorldsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String address = arg0.getItemAtPosition(arg2).toString();
                Log.i(TAG, "address.toString()" + address.toString());
//                Intent intentAddress = new Intent();
//                intentAddress.putExtra("selectAddress", keyWorldsView.getText().toString());
//                setResult(RESULT_OK, intentAddress);
                // finish();
            }
        });



////        //缩放级别
//        MapStatus.Builder builder = new MapStatus.Builder();
//        builder.zoom(18.0f);
//        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    /**
     * 发起搜索
     *
     * @param v
     */
    public void searchButtonProcess(View v) {
        if (v.getId() == R.id.geocode) {
            // Geo搜索
            mSearch.geocode(new GeoCodeOption()
                    .city("北京")
                    .address(searchAddress.getText().toString()));
        }
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(BaiDuActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
            return;
        }

        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions()
                .position(result.getLocation())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka)));

        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result.getLocation()));
        String strInfo = String.format("纬度：%f 经度：%f",
                result.getLocation().latitude,
                result.getLocation().longitude);

        Toast.makeText(BaiDuActivity.this, strInfo, Toast.LENGTH_LONG).show();

        Log.e("GeoCodeDemo", "onGetGeoCodeResult = " + result.toString());
    }


//    @Override
    //    }

    private void typeOnClien() {
        mRadioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.reli:
//        //开启热力图
                        mBaiduMap.setBaiduHeatMapEnabled(true);
                        mBaiduMap.setTrafficEnabled(false);
                        break;
                    case R.id.putong:
////普通地图 ,mBaiduMap是地图控制器对象    普通地图MAP_TYPE_NORMAL
                        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                        mBaiduMap.setMyLocationEnabled(true);
                        mBaiduMap.setTrafficEnabled(false);
                        mBaiduMap.setBaiduHeatMapEnabled(false);
                        break;
                    case R.id.weixing:
                        //卫星地图   MAP_TYPE_SATELLITE
                        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                        mBaiduMap.setTrafficEnabled(false);
                        mBaiduMap.setBaiduHeatMapEnabled(false);
                        break;
                    case R.id.kongbai:
//        //空白地图    MAP_TYPE_NONE
                        mBaiduMap.setTrafficEnabled(false);
                        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
                        mBaiduMap.setBaiduHeatMapEnabled(false);

                        break;
                    case R.id.shikuang:
                        //开启交通图
                        mBaiduMap.setTrafficEnabled(true);
                        mBaiduMap.setCustomTrafficColor("#ffba0101", "#fff33131", "#ffff9e19", "#00000000");
                        mBaiduMap.setBaiduHeatMapEnabled(false);
//  对地图状态做更新，否则可能不会触发渲染，造成样式定义无法立即生效。
                        MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(13);
                        mBaiduMap.animateMapStatus(u);
                        break;
                }
            }
        });
    }

    //        Log.e("GeoCodeDemo", "ReverseGeoCodeResult = " + result.toString());
    //

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    //        Toast.makeText(BaiDuActivity.this, result.getAddress() + " adcode: " + result.getAdcode(), Toast.LENGTH_LONG).show();

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    //

    @Override
    protected void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView = null;
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        if (geoCoder != null) {
            geoCoder.destroy();
        }
        if (mSuggestionSearch != null) {
            mSuggestionSearch.destroy();
            mSuggestionSearch = null;
        }
        super.onDestroy();
    }
    //        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result.getLocation()));

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 1, "我的位置");
        return super.onCreateOptionsMenu(menu);
    }
    //

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case 1:
                //把定位点再次显现出来
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(mapStatusUpdate);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka)));

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    //                .position(result.getLocation())

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
    //        mBaiduMap.addOverlay(new MarkerOptions()

    private LatLng locationLatLng;
    //        mBaiduMap.clear();

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 0 || "".equals(s.toString())) {
            searchPois.setVisibility(View.GONE);
        } else {
            searchPois.setVisibility(View.VISIBLE);

            // 创建PoiSearch实例
            PoiSearch poiSearch = PoiSearch.newInstance();
            // 城市内检索
            PoiCitySearchOption poiCitySearchOption = new PoiCitySearchOption();
            // 关键字
            poiCitySearchOption.keyword(s.toString());
            // 城市
            poiCitySearchOption.city(city);
            // 设置每页容量，默认为每页10条
            poiCitySearchOption.pageCapacity(5);
            // 分页编号
            poiCitySearchOption.pageNum(3);
            poiSearch.searchInCity(poiCitySearchOption);
            // 设置poi检索监听者
            poiSearch
                    .setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
                        // poi 查询结果回调
                        @Override
                        public void onGetPoiResult(PoiResult poiResult) {
                            poiInfos = poiResult.getAllPoi();
                            if (poiInfos != null) {
                                PoiSearchAdapter poiSearchAdapter = new PoiSearchAdapter(
                                        BaiDuActivity.this, poiInfos,
                                        locationLatLng);
                                // searchPois.setVisibility(View.VISIBLE);
                                searchPois.setAdapter(poiSearchAdapter);
                            }
                        }

                        // poi 详情查询结果回调
                        @Override
                        public void onGetPoiDetailResult(
                                PoiDetailResult poiDetailResult) {
                        }

                        @Override
                        public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

                        }

                        @Override
                        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

                        }
                    });
        }
    }
    //

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
//        latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        // 构造定位数据
//        MyLocationData locData = new MyLocationData.Builder()
//                .accuracy(location.getRadius())
//                // 此处设置开发者获取到的方向信息，顺时针0-360
//                .direction(100).latitude(location.getLatitude())
//                .longitude(location.getLongitude()).build();
//        // 设置定位数据
//        mBaiduMap.setMyLocationData(locData);
//        // 当不需要定位图层时关闭定位图层
//        //mBaiduMap.setMyLocationEnabled(false);
//        if (isFirstLoc) {
//            isFirstLoc = false;
//            LatLng ll = new LatLng(location.getLatitude(),
//                    location.getLongitude());
//            MapStatus.Builder builder = new MapStatus.Builder();
//            builder.target(ll).zoom(18.0f);
//            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {
//                // GPS定位结果
//                Toast.makeText(BaiDuActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
//                // 网络定位结果
//                Toast.makeText(BaiDuActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
//
//            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
//                // 离线定位结果
//                Toast.makeText(BaiDuActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
//
//            } else if (location.getLocType() == BDLocation.TypeServerError) {
//                Toast.makeText(BaiDuActivity.this, "服务器错误，请检查", Toast.LENGTH_SHORT).show();
//            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//                Toast.makeText(BaiDuActivity.this, "网络错误，请检查", Toast.LENGTH_SHORT).show();
//            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//                Toast.makeText(BaiDuActivity.this, "手机模式错误，请检查是否飞行", Toast.LENGTH_SHORT).show();
//            }
//        }
        // 如果bdLocation为空或mapView销毁后不再处理新数据接收的位置
        if (bdLocation == null || mBaiduMap == null) {
            return;
        }

        mLatitude = bdLocation.getLatitude();
        mLongitude = bdLocation.getLongitude();
        // 定位数据
        MyLocationData data = new MyLocationData.Builder()
                // 定位精度bdLocation.getRadius()
                .accuracy(bdLocation.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(bdLocation.getDirection())
                // 经度

                .latitude(mLatitude)
                // 纬度
                .longitude(mLongitude)
                // 构建
                .build();

        // 设置定位数据
        mBaiduMap.setMyLocationData(data);

        // 是否是第一次定位
        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(ll, 18);
            mBaiduMap.animateMapStatus(msu);
        }

        locationLatLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        // 获取城市，待会用于POISearch
        city = bdLocation.getCity();

        // 创建GeoCoder实例对象
        geoCoder = GeoCoder.newInstance();
        // 发起反地理编码请求(经纬度->地址信息)
        ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption();
        // 设置反地理编码位置坐标
        reverseGeoCodeOption.location(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude()));
        geoCoder.reverseGeoCode(reverseGeoCodeOption);

        // 设置查询结果监听者
        geoCoder.setOnGetGeoCodeResultListener(this);
    }
    //        }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }
    //            return;

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

    }
    //            Toast.makeText(BaiDuActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }
    //        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        // 地图操作的中心点
        LatLng cenpt = mapStatus.target;
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(cenpt));
    }
    //    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }
        sugAdapter.clear();
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null)
                sugAdapter.add(info.key + " " + info.city + info.district);
        }
        sugAdapter.notifyDataSetChanged();
    }

    // 拿到变化地点后的附近地址

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        /*onGetReverseGeoCodeResult获取到结果之后,
        mLocClient.stop(); 这里获取到附近定位的地址之后关闭定位*/
        stopTome = System.currentTimeMillis();
        mLocClient.stop();
        final List<PoiInfo> poiInfos = reverseGeoCodeResult.getPoiList();
        Log.i(TAG, "这里的值:" + poiInfos);
        if (poiInfos != null && !"".equals(poiInfos)) {
            PoiAdapter poiAdapter = new PoiAdapter(BaiDuActivity.this, poiInfos);
            lv_near_address.setAdapter(poiAdapter);
            lv_near_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String name = poiInfos.get(position).name.toString();
                    String address = poiInfos.get(position).address.toString();
                    Toast.makeText(BaiDuActivity.this, name, Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onItemClick: " + name);
                    Intent intent = new Intent(BaiDuActivity.this, GouWuCheActivity.class);
                    intent.putExtra("selectAddress", name+address);
                    Log.d(TAG, "onItemClick: ======"+mLatitude+"=========="+mLongitude);
                    intent.putExtra("longitude", mLongitude+"");
                    intent.putExtra("latitude", mLatitude+"");
                    setResult(RESULT_OK, intent);
//                    overridePendingTransition(R.anim.anim_open_exit, R.anim.anim_open_entry);
                    finish();
                }
            });
        }
    }
    private void initView2() {
        LatLng GEO_BEIJING = new LatLng(39.945, 116.404);
        LatLng GEO_SHANGHAI = new LatLng(31.227, 121.481);

        //北京为地图中心，logo在左上角
//        MapStatusUpdate status1 = MapStatusUpdateFactory.newLatLng(GEO_BEIJING);
//        SupportMapFragment map1 = (SupportMapFragment) (getSupportFragmentManager()
//                .findFragmentById(R.id.map1));
//        map1.getBaiduMap().setMapStatus(status1);
//        map1.getMapView().setLogoPosition(LogoPosition.logoPostionleftTop);
//
//        //上海为地图中心
//        MapStatusUpdate status2 = MapStatusUpdateFactory.newLatLng(GEO_SHANGHAI);
//        SupportMapFragment map2 = (SupportMapFragment) (getSupportFragmentManager()
//                .findFragmentById(R.id.map2));
//        map2.getBaiduMap().setMapStatus(status2);
    }
}
