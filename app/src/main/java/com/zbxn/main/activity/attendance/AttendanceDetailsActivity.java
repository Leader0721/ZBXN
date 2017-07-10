package com.zbxn.main.activity.attendance;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.ConfigUtils;
import com.pub.utils.DateUtils;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.slidedatetimepicker.NewSlideDateTimeDialogFragment;
import com.pub.widget.slidedatetimepicker.SlideDateTimeListener;
import com.pub.widget.slidedatetimepicker.SlideDateTimePicker;
import com.zbxn.R;
import com.zbxn.main.activity.contacts.ContactsChoseActivity;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.adapter.AttendanceDetailsAdapter;
import com.zbxn.main.entity.AttendanceCountEntity;
import com.zbxn.main.entity.AttendanceRecordEntity;
import com.zbxn.main.entity.Contacts;
import com.zbxn.main.listener.ICustomListener;
import com.zbxn.main.widget.LiOverlayManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

/**
 * @author: ysj
 * @date: 2017-02-13 09:42
 */
public class AttendanceDetailsActivity extends BaseActivity {
    private static final int Flag_Callback_Charge = 1000;
    private static final int Flag_Callback_Details = 1001;

    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_department)
    TextView tvDepartment;
    @BindView(R.id.tv_select_time)
    TextView tvSelectTime;
    @BindView(R.id.tv_late)
    TextView tvLate;
    @BindView(R.id.tv_early)
    TextView tvEarly;
    @BindView(R.id.tv_unSignIn)
    TextView tvUnSignIn;
    @BindView(R.id.tv_unSignOut)
    TextView tvUnSignOut;
    @BindView(R.id.tv_leave)
    TextView tvLeave;
    @BindView(R.id.tv_mTime)
    TextView tvMTime;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.mListView)
    ListView mListView;
    @BindView(R.id.ll_time)
    LinearLayout llTime;
    @BindView(R.id.mapview)
    MapView mapview;
    @BindView(R.id.mScrollView)
    ScrollView mScrollView;
    @BindView(R.id.ll_layout)
    LinearLayout llLayout;
    @BindView(R.id.img_back_top)
    ImageView imgBackTop;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdfYearMonth = new SimpleDateFormat("yyyy-MM");
    private SimpleDateFormat sdfText = new SimpleDateFormat("yyyy年MM月");
    private SimpleDateFormat sdfMonth = new SimpleDateFormat("M");
    private BaiduMap m_BaiduMap = null;
    private MyLocationConfiguration.LocationMode m_CurrentMode;
    private MyLocationListenner myLocationListenner;
    private boolean isList;
    private AttendanceDetailsAdapter mAdapter;
    private List<AttendanceRecordEntity> mList;

    private int screenHeight;
    private int backHeight;

    private ArrayList<Contacts> mListContacts = new ArrayList<>();
    private String chargeId;
    private String chargeName;
    private String mData;//当前选择的时间 yyyy-MM
    private String mRecordData;//当前选择的时间 yyyy-MM-dd
    private String userId;
    private String userHeadUrl;
    private String deparmentName;
    private String mMonth;

    private View viewCache = null;
    private View popupInfo = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_details);
        ButterKnife.bind(this);
        setTitle("考勤");

        initView();
        initData();
    }

    @Override
    public void initRight() {
        super.initRight();
        setRight1Icon(0);
        setRight1("同事");
        setRight1Show(true);
    }

    @Override
    public void actionRight1(MenuItem menuItem) {
        super.actionRight1(menuItem);
        Intent intent = new Intent(this, ContactsChoseActivity.class);
        intent.putExtra("list", mListContacts);
        intent.putExtra("type", 2);//0-查看 1-多选 2-单选
        startActivityForResult(intent, Flag_Callback_Charge);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Flag_Callback_Charge) {
            if (data != null) {
                chargeId = data.getIntExtra("id", 0) + "";
                chargeName = data.getStringExtra("name");
                userHeadUrl = data.getStringExtra("headUrl");
                deparmentName = data.getStringExtra("deparmentName");
                tvDepartment.setText(deparmentName);
                Contacts entity = new Contacts();
                entity.setId(data.getIntExtra("id", 0));
                entity.setUserName(chargeName);
                mListContacts.clear();
                mListContacts.add(entity);
                userId = chargeId;
                tvName.setText(chargeName);
                dataList(this, mData);
                getDetailCount();

                final DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showStubImage(R.mipmap.userhead_img)          // 设置图片下载期间显示的图片
                        .showImageForEmptyUri(R.mipmap.userhead_img)  // 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.mipmap.userhead_img)       // 设置图片加载或解码过程中发生错误显示的图片
                        .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                        .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                        //.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                        .build();                                   // 创建配置过得DisplayImageOption对象
                String server = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
                ImageLoader.getInstance().displayImage(server + userHeadUrl, imgHead, options);
            }
        }
        if (requestCode == Flag_Callback_Details) {
            dataList(AttendanceDetailsActivity.this, mData);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        tvName.setText(PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_USERNAME));
        myLocationListenner = new MyLocationListenner();
        tvSelectTime.setText(sdfText.format(new Date()));

        m_BaiduMap = mapview.getMap();
        m_BaiduMap.setMyLocationEnabled(true);
        m_BaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        mListView.setFocusable(false);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        /*mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                Log.d("scrollY", scrollY + "-------" + oldScrollY);
                backHeight = scrollY;
                if (backHeight < screenHeight) {
                    imgBackTop.setVisibility(View.GONE);
                } else {
                    imgBackTop.setVisibility(View.VISIBLE);
                }
            }
        });*/
        try {
            tvMTime.setText("我的" + sdfMonth.format(sdfText.parse(StringUtils.getEditText(tvSelectTime))) + "月考勤");
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        initBaiduMapData();
    }

    private void initBaiduMapData() {
        m_CurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        m_BaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(m_CurrentMode, false, BitmapDescriptorFactory.fromResource(R.mipmap.mylocation)));
        // 开启定位图层
        m_BaiduMap.setMyLocationEnabled(true);
        m_BaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {

            @Override
            public void onMapLoaded() {
                // 设置缩放级别
                MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(17.0f);
                m_BaiduMap.setMapStatus(msu);
            }
        });
        // 启动百度地图获取当前经纬度
        if (((BaseApp) getApplication()).isStartedLocationClient()) {
            ((BaseApp) getApplication()).requestLocationClient(myLocationListenner);
        } else {
            ((BaseApp) getApplication()).startLocationClient(myLocationListenner);
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapview == null)
                return;

            MyLocationData locData = new MyLocationData.Builder().accuracy(0)
                    // 如果不显示定位精度圈，将accuracy赋值为0即可
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    // .direction(100)
                    .latitude(location.getLatitude()).longitude(location.getLongitude()).build();
            m_BaiduMap.setMyLocationData(locData);

            // 定位
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            m_BaiduMap.animateMapStatus(u);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((BaseApp) getApplication()).stopLocationClient(myLocationListenner);   //添加这句就行了
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapview.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapview.onPause();
    }

    private void initData() {
        userId = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_ID);
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        mData = sdfYearMonth.format(new Date());
        mRecordData = sdf.format(new Date());
        dataList(this, mData);
        getDetailCount();

        userHeadUrl = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_HEADURL);
        deparmentName = PreferencesUtils.getString(this, LoginActivity.FLAG_DEPARMENTNAME);
        tvDepartment.setText(deparmentName);

        final DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.userhead_img)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.userhead_img)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.userhead_img)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                //.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();                                   // 创建配置过得DisplayImageOption对象
        String server = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
        ImageLoader.getInstance().displayImage(server + userHeadUrl, imgHead, options);
    }

    private ICustomListener listener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            Intent intent;
            switch (obj0) {
                case 0://上方的申诉
                    AttendanceRecordEntity entity = (AttendanceRecordEntity) obj1;
                    intent = new Intent(AttendanceDetailsActivity.this, GrievanceActivity.class);
                    intent.putExtra("item", entity);
                    intent.putExtra("type", "1");//1迟到  2早退
                    startActivityForResult(intent, Flag_Callback_Details);
                    break;
                case 1://下方的申诉
                    AttendanceRecordEntity entity1 = (AttendanceRecordEntity) obj1;
                    intent = new Intent(AttendanceDetailsActivity.this, GrievanceActivity.class);
                    intent.putExtra("item", entity1);
                    intent.putExtra("type", "2");//1迟到  2早退
                    startActivityForResult(intent, Flag_Callback_Details);
                    break;
                default:
                    break;
            }
        }
    };

    @OnClick({R.id.ll_time, R.id.tv_type, R.id.img_back_top})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_time://选择时间
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(new SlideDateTimeListener() {
                            @Override
                            public void onDateTimeSet(Date date) {
                                try {
                                    Date dt = sdf.parse(DateUtils.getDate());
                                    if (dt.before(date)) {
                                        MyToast.showToast("暂无考勤记录");
                                        return;
                                    }

                                    mData = sdfYearMonth.format(date);
                                    mRecordData = sdf.format(date);
                                    dataList(AttendanceDetailsActivity.this, mData);
                                    tvSelectTime.setText(sdfText.format(date));

                                    try {
                                        tvMTime.setText("我的" + sdfMonth.format(sdfText.parse(StringUtils.getEditText(tvSelectTime))) + "月考勤");
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setInitialDate(StringUtils.convertToDate(sdf, mRecordData))
                        .setIs24HourTime(true)
                        .setIsHaveTime(NewSlideDateTimeDialogFragment.Have_Date)
                        .build()
                        .show();
                break;
            case R.id.tv_type://切换模式
                if (isList) {
                    mScrollView.setFillViewport(false);
                    mListView.setVisibility(View.VISIBLE);
                    mapview.setVisibility(View.GONE);
                    tvType.setText("地图模式");
                    if (backHeight >= screenHeight) {
                        imgBackTop.setVisibility(View.VISIBLE);
                    }
                } else {
                    mScrollView.setFillViewport(true);
                    mListView.setVisibility(View.GONE);
                    mapview.setVisibility(View.VISIBLE);
                    tvType.setText("列表模式");
                    imgBackTop.setVisibility(View.GONE);
                }
                isList = !isList;
                break;
            case R.id.img_back_top://返回顶部
                mScrollView.smoothScrollTo(0, 0);
                imgBackTop.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 重新设定listView的高度
     *
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        //获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);  //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        listView.setLayoutParams(params);
    }

    /**
     * 获取数据统计
     */
    public void getDetailCount() {
        String tokenid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().getDetailCount(tokenid, currentCompanyId, userId, mData);
        callRequest(call, new HttpCallBack(AttendanceCountEntity.class, this, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    if (mResult.getRows().size() != 0) {
                        AttendanceCountEntity entity = (AttendanceCountEntity) mResult.getRows().get(0);
                        tvLate.setText(entity.getLateCount() + "");
                        tvEarly.setText(entity.getLeaveEarlyCount() + "");
                        tvUnSignIn.setText(entity.getNoCheckInCount() + "");
                        tvUnSignOut.setText(entity.getNoCheckOutCount() + "");
                        tvLeave.setText(entity.getLeaveCount() + "");
                    }
                } else {

                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
            }
        });
    }

    /**
     * 查看考勤记录
     *
     * @param context
     * @param date
     * @param listener
     */
    public void dataList(Context context, String date) {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String currentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().SearchDataList(ssid, currentCompanyId, userId, date);
        callRequest(call, new HttpCallBack(AttendanceRecordEntity.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    List<AttendanceRecordEntity> list = mResult.getRows();
                    mList = new ArrayList<>();
                    mList.addAll(list);
                    mAdapter = new AttendanceDetailsAdapter(AttendanceDetailsActivity.this, mList, listener);
                    mListView.setAdapter(mAdapter);
                    setListViewHeightBasedOnChildren(mListView);
                    if (mList.size() == 0) {
                        MyToast.showToast("暂无考勤记录");
                    }
                    setData();
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast("获取网络数据失败");
            }
        });
    }

    private void setData() {
        // 构建Marker图标
        Bitmap bmA = BitmapFactory.decodeResource(getResources(), R.mipmap.mylocation_red);
        BitmapDescriptor bitmapA = BitmapDescriptorFactory.fromBitmap(bmA);
        Bitmap bmB = BitmapFactory.decodeResource(getResources(), R.mipmap.mylocation);
        BitmapDescriptor bitmapB = BitmapDescriptorFactory.fromBitmap(bmB);
        // 构建MarkerOption，用于在地图上添加Marker
        Bundle bundle = null;
        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        List<OverlayOptions> list = new ArrayList<OverlayOptions>();
        for (int i = 0; i < mList.size(); i++) {
            double lat = 0.00;
            double lng = 0.00;
            if (!StringUtils.isEmpty(mList.get(i).getCheckInLatitude()) && !StringUtils.isEmpty(mList.get(i).getCheckInLongitude())) {
                lat = Double.parseDouble(mList.get(i).getCheckInLatitude()) / 10000D;
                lng = Double.parseDouble(mList.get(i).getCheckInLongitude()) / 10000D;
                if (0 == lat || lng == 0) {
                    continue;
                }

                latLng = new LatLng(lat, lng);

                // 构建MarkerOption，用于在地图上添加Marker
                bundle = new Bundle();
                bundle.putParcelable("data", mList.get(i));
                bundle.putInt("position", i);
                bundle.putString("address", mList.get(i).getCheckInAddress());
                overlayOptions = new MarkerOptions().position(latLng).icon(bitmapA).extraInfo(bundle);
                list.add(overlayOptions);
            }

            if (!StringUtils.isEmpty(mList.get(i).getCheckOutLatitude()) && !StringUtils.isEmpty(mList.get(i).getCheckOutLongitude())) {
                lat = Double.parseDouble(mList.get(i).getCheckOutLatitude()) / 10000D;
                lng = Double.parseDouble(mList.get(i).getCheckOutLongitude()) / 10000D;
                if (0 == lat || lng == 0) {
                    continue;
                }

                latLng = new LatLng(lat, lng);

                // 构建MarkerOption，用于在地图上添加Marker
                bundle = new Bundle();
                bundle.putParcelable("data", mList.get(i));
                bundle.putInt("position", i);
                bundle.putString("address", mList.get(i).getCheckOutAddress());
                overlayOptions = new MarkerOptions().position(latLng).icon(bitmapB).extraInfo(bundle);
                list.add(overlayOptions);
            }
        }

        m_BaiduMap.clear();
        LiOverlayManager overlayManager = new LiOverlayManager(m_BaiduMap);
        //overlayManager.setCustomListener(listener);
        overlayManager.setData(list);
        //        m_BaiduMap.setOnMarkerClickListener(overlayManager);
        m_BaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                AttendanceRecordEntity data = (AttendanceRecordEntity) marker.getExtraInfo().getParcelable("data");
                String name = marker.getExtraInfo().getString("address");
                InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                    public void onInfoWindowClick() {
                        //                        LatLng ll = marker.getPosition();
                        //                        LatLng llNew = new LatLng(ll.latitude + 0.005,
                        //                                ll.longitude + 0.005);
                        //                        marker.setPosition(llNew);
                        //                        m_BaiduMap.hideInfoWindow();
                    }
                };
                //创建InfoWindow展示的view
                viewCache = getLayoutInflater().inflate(R.layout.custom_text_view, null);
                popupInfo = (View) viewCache.findViewById(R.id.popinfo);
                TextView m_name = (TextView) viewCache.findViewById(R.id.m_name);

                m_name.setText(name);

//                InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(viewCache), marker.getPosition(), -47, listener);
                InfoWindow mInfoWindow = new InfoWindow(viewCache, marker.getPosition(), -47);
                m_BaiduMap.showInfoWindow(mInfoWindow);
                return true;
            }
        });
        overlayManager.addToMap();

        overlayManager.zoomToSpan();

        m_BaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {

            @Override
            public void onMapLoaded() {
                if (mList.size() == 1) {
                    // 设置缩放级别
                    MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
                    m_BaiduMap.setMapStatus(msu);
                }
            }
        });
        m_BaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                m_BaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                m_BaiduMap.hideInfoWindow();
                return false;
            }
        });
    }

}
