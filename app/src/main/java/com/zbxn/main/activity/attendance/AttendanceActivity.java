package com.zbxn.main.activity.attendance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.http.ResultNetData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.MyGridView;
import com.pub.widget.MyListView;
import com.umeng.analytics.MobclickAgent;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.adapter.AttendanceRecordAdapter;
import com.zbxn.main.adapter.AttendanceRuleTimeAdapter;
import com.zbxn.main.entity.AttendanceRecordEntity;
import com.zbxn.main.entity.AttendanceRuleTimeEntity;
import com.zbxn.main.listener.ICustomListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by Administrator on 2016/12/19.
 */
public class AttendanceActivity extends BaseActivity {
    /**
     * 回调处理
     */
    private static final int Flag_Callback_Record = 1002;

    @BindView(R.id.mapview)
    MapView mapview;
    @BindView(R.id.listview)
    MyListView listview;
    @BindView(R.id.listview1)
    MyListView listview1;
    @BindView(R.id.image_location)
    ImageView imageLocation;
    @BindView(R.id.mTime)
    TextView mTime;
    @BindView(R.id.mAddr)
    TextView mAddr;
    @BindView(R.id.mAdd)
    TextView mAdd;
    @BindView(R.id.mGridView)
    MyGridView mGridView;

    private List<AttendanceRuleTimeEntity> mList;
    private AttendanceRuleTimeAdapter mAdapter;

    private List<AttendanceRecordEntity> mList1;
    private AttendanceRecordAdapter mAdapter1;

    private MyLocationConfiguration.LocationMode m_CurrentMode;
    private BaiduMap m_BaiduMap = null;

    private double mLatitude = 0.0;
    private double mLongitude = 0.0;
    private String mAddress = "";

    private SimpleDateFormat sfDay = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

    private MyLocationListenner myLocationListenner;

    private String date = "";

    private boolean isFirst;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_attendance);
        ButterKnife.bind(this);
        setTitle("考勤");

        myLocationListenner = new MyLocationListenner();

        initView();
        initData();
        mList = new ArrayList<>();
        mList1 = new ArrayList<>();

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        RuleTimeDataList(this, mICustomListener);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        date = sf.format(new Date());
        dataList(this, date, mICustomListener);

        isFirst = PreferencesUtils.getBoolean(this, "attendanceFirst", true);

    }

    /**
     * 显示蒙版引导
     */
    private void showGuide(float x, final float y, final int height) {
        final AlertDialog ad = new AlertDialog.Builder(this, R.style.Dialog_Fullscreen).create();// 创建
        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(true);
        // 显示对话框
        ad.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = ad.getWindow().getAttributes();
        lp.width = display.getWidth(); //设置宽度
        ad.getWindow().setAttributes(lp);
        Window window = ad.getWindow();
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setContentView(R.layout.dialog_guide_attendance);

        final ImageView imgTop = (ImageView) window.findViewById(R.id.img_top);
        final ImageView imgMiddle = (ImageView) window.findViewById(R.id.img_middle);
        imgMiddle.setX(x);
        imgMiddle.setY(y);

        final ImageView img = (ImageView) window.findViewById(R.id.img_bt);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
//        ViewGroup.LayoutParams params = imgMiddle.getLayoutParams();
//        params.height = height;
        imgMiddle.post(new Runnable() {
            @Override
            public void run() {
                imgTop.setY(y - imgTop.getHeight());
                img.setY(y + imgMiddle.getHeight() + 50);
            }
        });
    }

    @Override
    public void initRight() {
        setRight1Show(true);
        setRight1("考勤详情");
        setRight1Icon(0);
        setRight2Show(false);
        super.initRight();
    }

    @Override
    public void actionRight1(MenuItem menuItem) {
        startActivity(new Intent(this, AttendanceDetailsActivity.class));
        super.actionRight1(menuItem);
    }

    private void initView() {
        m_BaiduMap = mapview.getMap();
        m_BaiduMap.setMyLocationEnabled(true);
        m_BaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
    }

    private void initData() {
        m_CurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        m_BaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(m_CurrentMode, false, BitmapDescriptorFactory.fromResource(R.mipmap.mylocation_red)));
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

        String date = StringUtils.StringData("");
        mTime.setText(date);
    }

    /**
     * 回调需要接收的
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Flag_Callback_Record) {
            if (resultCode == RESULT_OK) {
                dataList(this, date, mICustomListener);
            } else {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.image_location, R.id.mAdd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_location://标题
                // 启动百度地图获取当前经纬度
                if (((BaseApp) getApplication()).isStartedLocationClient()) {
                    ((BaseApp) getApplication()).requestLocationClient(myLocationListenner);
                } else {
                    ((BaseApp) getApplication()).startLocationClient(myLocationListenner);
                }
                break;
            case R.id.mAdd://
                save(getApplicationContext(), sf.format(new Date()), "", mAddress, mLongitude + "", mLatitude + "", mICustomListener);
                break;
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
        MobclickAgent.onPageStart("AttendanceActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapview.onPause();
        MobclickAgent.onPageEnd("AttendanceActivity");
        MobclickAgent.onPause(this);
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

            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            try {

                mAddress = location.getProvince() + location.getCity() + location.getDistrict() + location.getStreet() + location.getStreetNumber();

            } catch (Exception e) {
                mAddress = location.getAddrStr();
            }

            if (mAddress.equals("nullnullnullnullnull")) {
                mAddress = "";
            }

            mAddr.setText(mAddress);

        }
    }

    private ICustomListener mICustomListener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            switch (obj0) {
                case 0:
                    List<AttendanceRuleTimeEntity> list1 = (List<AttendanceRuleTimeEntity>) obj1;
                    mList.clear();
                    mList.addAll(list1);
                    mAdapter = new AttendanceRuleTimeAdapter(getApplicationContext(), mList, this);
                    mGridView.setAdapter(mAdapter);
                    break;
                case 1:
                    AttendanceRecordEntity entity1 = (AttendanceRecordEntity) obj1;
                    Intent intent1 = new Intent(AttendanceActivity.this, GrievanceActivity.class);
                    intent1.putExtra("item", entity1);
                    intent1.putExtra("type", "1");//1迟到  2早退
                    startActivityForResult(intent1, Flag_Callback_Record);
                    break;
                case 2:
                    AttendanceRecordEntity entity = (AttendanceRecordEntity) obj1;
                    Intent intent = new Intent(AttendanceActivity.this, GrievanceActivity.class);
                    intent.putExtra("item", entity);
                    intent.putExtra("type", "2");//1迟到  2早退
                    startActivityForResult(intent, Flag_Callback_Record);
                    break;
                case 3:
                    List<AttendanceRecordEntity> list2 = (List<AttendanceRecordEntity>) obj1;
                    mList1.clear();
                    mList1.addAll(list2);
                    mAdapter1 = new AttendanceRecordAdapter(getApplicationContext(), mList1, this);
                    listview1.setAdapter(mAdapter1);
                    //如果是第一次打开此界面
                    if (isFirst) {
                        PreferencesUtils.putBoolean(AttendanceActivity.this, "attendanceFirst", false);
                        isFirst = false;
                        listview1.post(new Runnable() {
                            @Override
                            public void run() {
                                int[] location = new int[2];
                                listview1.getLocationOnScreen(location);
                                int x = location[0];
                                int y = location[1];
                                showGuide(x, y, listview1.getHeight());
                            }
                        });
                    }


                    break;
                case 6:
                    MyToast.showToast("签到成功");
                    dataList(getApplicationContext(), date, mICustomListener);
                    break;
            }
        }
    };


    //下面是进行数据请求的几个方法

    /**
     * 获取考勤规则时间
     *
     * @param context
     * @param listener
     */
    public void RuleTimeDataList(Context context, final ICustomListener listener) {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String currentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().GetRuleTimeDataList(ssid, currentCompanyId);
        callRequest(call, new HttpCallBack(AttendanceRuleTimeEntity.class, this, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    List<AttendanceRuleTimeEntity> list = mResult.getRows();
                    listener.onCustomListener(0, list, 0);
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

    /**
     * 添加考勤
     *
     * @param context
     * @param checktime
     * @param ip
     * @param address
     * @param longitude
     * @param latitude
     * @param listener
     */
    public void save(Context context, String checktime, String ip, String address
            , String longitude, String latitude, final ICustomListener listener) {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String currentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().SaveRuleTimeDataList(ssid, currentCompanyId, ip, address, checktime, longitude, latitude);
        callRequest(call, new HttpCallBack(ResultNetData.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    listener.onCustomListener(6, null, 0);
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

    /**
     * 查看考勤记录
     *
     * @param context
     * @param date
     * @param listener
     */
    public void dataList(Context context, String date, final ICustomListener listener) {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String currentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID);
        String userId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_INPUT_ID);
        Call call = HttpRequest.getIResourceOANetAction().SearchDataList(ssid, currentCompanyId, userId, date);
        callRequest(call, new HttpCallBack(AttendanceRecordEntity.class, this, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    List<AttendanceRecordEntity> list = mResult.getRows();
                    listener.onCustomListener(3, list, 0);
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


}