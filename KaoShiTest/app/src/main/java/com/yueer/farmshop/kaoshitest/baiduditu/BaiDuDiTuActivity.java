package com.yueer.farmshop.kaoshitest.baiduditu;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.yueer.farmshop.kaoshitest.R;
import com.yueer.farmshop.kaoshitest.baiduditu.baidu.PoiSearchAdapter;
import com.yueer.farmshop.kaoshitest.base.BaseActivity;
import com.yueer.farmshop.kaoshitest.base.BasePresenter;
import com.yueer.farmshop.kaoshitest.fragment.home.mycenter.GouWuCheActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class BaiDuDiTuActivity extends BaseActivity {

    //    @BindView(R.id.geocodekey)
//    EditText geocodekey;
    @BindView(R.id.searchView)
    SearchView searchView;
    private AutoCompleteTextView autoCompleteTextView;

    private static final String TAG = "BaiDuDiTuActivity";
    @BindView(R.id.baiduditu_bmapView)
    MapView baidudituBmapView;
    @BindView(R.id.location)
    Button location;
    @BindView(R.id.marker)
    Button marker;
    @BindView(R.id.poi)
    Button poi;
    @BindView(R.id.baidu_lv)
    ListView lv;

    @BindView(R.id.planSearch)
    Button planSearch;
    private BaiduMap mMap;
    private LocationClient mLocationClient;
    private RoutePlanSearch mRoutePlanSearch;
    private PoiSearch mPoiSearch;
    private LatLng mUser_latlng;
    private ArrayList<String> mDIZhi = new ArrayList<>();
    private GeoCoder mSearch;
    private SuggestionSearch mSuggestionSearch;

    @Override
    protected BasePresenter initBasePresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bai_du_di_tu;
    }

    @Override
    protected void initData() {

        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
//可选，设置返回经纬度坐标类型，默认GCJ02
//GCJ02：国测局坐标；
//BD09ll：百度经纬度坐标；
//BD09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(1000);
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
//可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
//可选，V7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明


//        this.autoCompleteTextView = (AutoCompleteTextView) this.findViewById(R.id.autoCompleteTextView);
//        //1.得到资源对象
//        Resources resources = this.getResources();
//        //2.读取指定资源的数组
//        String[] country = resources.getStringArray(R.array.country_array);
//        System.out.println("country=" + Arrays.toString(country));
//        //3.实例化数组适配器对象
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1,//系统提供好的布局文件,即TextView控件
//                country//数据源
//        );
//        //4.设置当前控件的适配器对象adapter
//        this.autoCompleteTextView.setAdapter(adapter);


        initSouSuo();


    }

    private void initSouSuo() {


//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                searchPois.setVisibility(View.GONE);
//                if (poiInfos != null) {
//                    if (poiInfos.get(position) != null) {
//                        LatLng location = poiInfos.get(position).location;
//                        if (location != null) {
//                            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(location, 18);
//                            mMap.animateMapStatus(msu);
//                        }
//                    }
//                }
//
//            }
//        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mDIZhi != null && mDIZhi.size() > 0) {
                    Log.d(TAG, "Poi地址: " + mDIZhi.toString());
                    LvBaiDuAdapter lvBaiDuAdapter = new LvBaiDuAdapter(BaiDuDiTuActivity.this, mDIZhi);
                    lv.setAdapter(lvBaiDuAdapter);


                    lv.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(BaiDuDiTuActivity.this, "晒哦皇帝", Toast.LENGTH_SHORT).show();
                    lv.setVisibility(View.GONE);
                }
            }
        });

    }

    //定位一上来已经完成了,点击按钮仅仅是把地图视图拉到用户的位置
    private void locate2User() {
        MapStatusUpdate status2 = MapStatusUpdateFactory.newLatLng(mUser_latlng);
        mMap.setMapStatus(status2);
    }

    /**
     * 自定义定位图标
     */
    private void locationConfig() {
        //自定义定位图标
        MyLocationConfiguration mLocationConfiguration = new MyLocationConfiguration
                (MyLocationConfiguration.LocationMode.NORMAL, true,
                        BitmapDescriptorFactory.fromResource(R.drawable.icon_geo),
                        0xAAFFFF88, 0xAA00FF00);
        mMap.setMyLocationConfiguration(mLocationConfiguration);
    }

    @Override
    protected void initView() {
        initPer();

        mMap = baidudituBmapView.getMap();//百度核心地图
        //设置地图类型,普通类型卫星图类型  空白类型
        mMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        //开启交通图
        mMap.setTrafficEnabled(true);

        //开启地图的定位图层
        mMap.setMyLocationEnabled(true);


        location();//定位
        initListenerMarker();//marker的点击事件
        //创建POI检索实例
        mPoiSearch = PoiSearch.newInstance();
        //设置检索监听器
        mPoiSearch.setOnGetPoiSearchResultListener(listener);

        //路径规划
        mRoutePlanSearch = RoutePlanSearch.newInstance();


//        // 初始化搜索模块，注册搜索事件监听
//        mSuggestionSearch = SuggestionSearch.newInstance();
//        //  接口OnGetSuggestionResultListener    接口里的方法onGetSuggestionResult
//        mSuggestionSearch.setOnGetSuggestionResultListener((OnGetSuggestionResultListener) this);
//
//        // 初始化搜索模块，注册事件监听
//        mSearch = GeoCoder.newInstance();
//        mSearch.setOnGetGeoCodeResultListener((OnGetGeoCoderResultListener) this);
    }

    //步行导航
    private void initListenerMarker() {
        //marker点击事件
        mMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            //marker被点击时回调的方法
            //若响应点击事件，返回true，否则返回false
            //默认返回false
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle extraInfo = marker.getExtraInfo();
                String id = extraInfo.getString("id");
                if ("id_01".equals(id)) {
                    Toast.makeText(BaiDuDiTuActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
                }
                //步行导航
                walkNavi(marker.getPosition());
                return false;
            }
        });


        mMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //marker
                addMarker(latLng);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    private void addMarker(LatLng latLng) {
        //定义Maker坐标点
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marka);
        Bundle bundle = new Bundle();
        bundle.putString("id", "id_01");
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(latLng)
                .animateType(MarkerOptions.MarkerAnimateType.jump)
                .draggable(true)
                .title("你好,百度")
                .extraInfo(bundle)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mMap.addOverlay(option);
    }

    private void walkNavi(final LatLng end) {
        // 获取导航控制类
        // 引擎初始化
        WalkNavigateHelper.getInstance().initNaviEngine(this, new IWEngineInitListener() {

            @Override
            public void engineInitSuccess() {
                //引擎初始化成功的回调
                routeWalkPlanWithParam(end);
            }

            @Override
            public void engineInitFail() {
                //引擎初始化失败的回调
            }
        });
    }

    private void routeWalkPlanWithParam(LatLng end) {
        //发起算路
        //构造WalkNaviLaunchParam
        //起终点位置

//构造WalkNaviLaunchParam
        WalkNaviLaunchParam mParam = new WalkNaviLaunchParam().stPt(mUser_latlng).endPt(end);

        //发起算路
        WalkNavigateHelper.getInstance().routePlanWithParams(mParam, new IWRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                //开始算路的回调
            }

            @Override
            public void onRoutePlanSuccess() {
                //算路成功
                //跳转至诱导页面
                Intent intent = new Intent(BaiDuDiTuActivity.this,
                        WNaviGuideActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(WalkRoutePlanError walkRoutePlanError) {
                //算路失败的回调
            }
        });

    }

    private List<PoiInfo> poiInfos;
    //创建POI检索监听器
    OnGetPoiSearchResultListener listener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {

            poiInfos = poiResult.getAllPoi();
            if (poiInfos != null) {
                PoiSearchAdapter poiSearchAdapter = new PoiSearchAdapter(BaiDuDiTuActivity.this, poiInfos, mUser_latlng);
                // searchPois.setVisibility(View.VISIBLE);
                lv.setAdapter(poiSearchAdapter);
            }


            if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
                mMap.clear();

                //创建PoiOverlay对象
                PoiOverlay poiOverlay = new PoiOverlay(mMap);

                //设置Poi检索数据
                poiOverlay.setData(poiResult);

                //将poiOverlay添加至地图并缩放至合适级别
                poiOverlay.addToMap();
                poiOverlay.zoomToSpan();


                List<PoiInfo> allPoi = poiResult.getAllPoi();
                for (int i = 0; i < allPoi.size(); i++) {
                    String name = allPoi.get(i).getName();
                    mDIZhi.add(name);
                }

                for (int i = 0; i < mDIZhi.size() - 1; i++) {
                    for (int j = mDIZhi.size() - 1; j > i; j--) {
                        if (mDIZhi.get(j).equals(mDIZhi.get(i))) {
                            mDIZhi.remove(j);
                        }
                    }
                }

                Log.d(TAG, "fujindizhi : " + poiResult.getAllPoi() + "dizhi" + mDIZhi);

            }

        }

        @Override
        public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }

        //废弃
        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        }
    };

    private void location() {


        //开启地图的定位图层
        mMap.setMyLocationEnabled(true);
        //通过LocationClient发起定位
        //定位初始化
        mLocationClient = new LocationClient(this);

//通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);

//设置locationClientOption
        mLocationClient.setLocOption(option);

//注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
//开启地图定位图层
        mLocationClient.start();

//获取地址

        option.setIsNeedAddress(true);
//可选，是否需要地址信息，默认为不需要，即参数为false
//如果开发者需要获得当前点的地址信息，此处必须为true

        mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
    }


    @OnClick({R.id.location, R.id.marker, R.id.poi, R.id.planSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.location:
                locationConfig();
                locate2User();
                break;
            case R.id.marker:
                LatLng latLng = new LatLng(39.963175, 116.400244);
                addMarker(latLng);
                break;
            case R.id.poi:
                poi();
                break;
            case R.id.planSearch:
                planSearch();
                break;
        }
    }

    private void poi() {
        //发起检索请求
        //城市检索
        //mPoiSearch.searchInCity()
        /**
         * 以天安门为中心，搜索半径100米以内的餐厅
         */
        mPoiSearch.searchNearby(new PoiNearbySearchOption()
                .location(new LatLng(mUser_latlng.latitude, mUser_latlng.longitude))
                .radius(5000)
                .keyword("餐厅")
                .pageNum(10));


    }

    /**
     * 步行路径规划
     */
    private void planSearch() {
        OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
                if (walkingRouteResult != null) {
                    //创建WalkingRouteOverlay实例
                    WalkingRouteOverlay overlay = new WalkingRouteOverlay(mMap);
                    if (walkingRouteResult.getRouteLines().size() > 0) {
                        //获取路径规划数据,(以返回的第一条数据为例)
                        //为WalkingRouteOverlay实例设置路径数据
                        overlay.setData(walkingRouteResult.getRouteLines().get(0));
                        //在地图上绘制WalkingRouteOverlay
                        overlay.addToMap();
                    }
                }
            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        };
        mRoutePlanSearch.setOnGetRoutePlanResultListener(listener);

        PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京", "北京吉利大学广播台");
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京", "现代软件学院");

        mRoutePlanSearch.walkingSearch((new WalkingRoutePlanOption())
                .from(stNode)
                .to(enNode));
    }


    //定义回调监听
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f

            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

            int errorCode = location.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明


            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息


            Log.d("维度", "onReceiveLocation: " + latitude + "========地址" + addr + country + province);

            SharedPreferences sp = getSharedPreferences("纬度信息", MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("纬度信息", latitude + "");
            edit.putString("经度", longitude + "");
            edit.putString("地址", addr);

            edit.commit();


//mapView 销毁后不在处理新接收的位置
            if (location == null || baidudituBmapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            Log.d(TAG, "latitude: " + location.getLatitude() + ",longitude:" + location.getLongitude());
            mUser_latlng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.setMyLocationData(locData);
        }
    }

    //百度地图权限
    private void initPer() {
        String[] per = {Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA};

        ActivityCompat.requestPermissions(this, per, 100);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences sp = getSharedPreferences("纬度信息", MODE_PRIVATE);
        String weidu = sp.getString("纬度信息", null);
        String jingdu = sp.getString("经度", null);
        String dizhi = sp.getString("地址", null);
        Log.d(TAG, "维度: " + weidu + "===========经度:" + jingdu);
        if (weidu != null && jingdu != null) {
            Intent intent = new Intent(this, GouWuCheActivity.class);
            intent.putExtra("维度", weidu);
            intent.putExtra("经度", jingdu);
            intent.putExtra("地址", dizhi);
//            setResult(321,intent);
//            finish();
            startActivity(intent);
        } else {
            Toast.makeText(this, "空的哦", Toast.LENGTH_SHORT).show();
        }
    }


//    /**
//     * 发起搜索
//     *
//     * @param v
//     */
//    public void searchButtonProcess(View v) {
//        if (v.getId() == R.id.geocode) {
//            // Geo搜索
//            mSearch.geocode(new GeoCodeOption()
//                    .city("北京")
//                    .address(geocodekey.getText().toString()));
//
//        }
//    }
}