package com.pub.base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.lidroid.xutils.DbUtils;
import com.pub.utils.ConfigUtils;
import com.pub.utils.DeviceUtils;
import com.pub.utils.FileAccessor;
import com.pub.utils.ImageLoaderConfig;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import cn.jpush.android.api.JPushInterface;


public class BaseApp extends Application {
    public static Context CONTEXT = null;
    public static DbUtils DBLoader = null;

    //友盟分享三方平台appkey
    {
        PlatformConfig.setWeixin("wx9bb363fafd76660e", "b399ae72821cf2b61cd31adeacee7130");
        //PlatformConfig.setSinaWeibo("", "");
//        PlatformConfig.setQQZone("1105740972", "qtrJRc4zRjfHE8Jp");
        PlatformConfig.setQQZone("1105735303", "WUYGMADwPBRSw0HB");
    }

    // 百度地图定位
    private LocationClient mLocationClient;

    public static boolean DEBUG = false;

    public static Context getContext() {
        if (CONTEXT != null) {
            return CONTEXT.getApplicationContext();
        } else {
            return new BaseApp().getApplicationContext();
        }
    }

    @Override
    public void onCreate() {
        CONTEXT = this;

        String debug = ConfigUtils.getConfig(ConfigUtils.KEY.debug);
        if (debug != null && debug.equals("true")) {
            DEBUG = true;
        }

        initDatabase();

        ImageLoaderConfig.initImageLoader(this);

        FileAccessor.initFileAccess();

        // 将“12345678”替换成您申请的APPID，申请地址：http://open.voicecloud.cn
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=587da70c");

        //极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        // 注册百度地图定位事件
        mLocationClient = new LocationClient(this);
        setLocationOption();

        //友盟分享
        UMShareAPI.get(this);
//        Config.REDIRECT_URL = "您新浪后台的回调地址";

        super.onCreate();
    }

    //接下来是百度地图的一些方法

    /**
     * 获取mLocationClient是否启动
     *
     * @return
     */
    public boolean isStartedLocationClient() {
        return mLocationClient.isStarted();
    }

    /**
     * 启动定位
     *
     * @param listener
     */
    public void startLocationClient(BDLocationListener listener) {
        mLocationClient.registerLocationListener(listener);
        mLocationClient.start();
    }

    /**
     * 停止定位
     */
    public void stopLocationClient(BDLocationListener listener) {
        mLocationClient.unRegisterLocationListener(listener);
        mLocationClient.stop();
    }

    /**
     * 重新定位
     *
     * @param listener
     */
    public void requestLocationClient(BDLocationListener listener) {
        mLocationClient.registerLocationListener(listener);
        mLocationClient.requestLocation();
    }

    // 设置相关参数
    public void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
//		option.setScanSpan(600000);// 设置发起定位请求的间隔时间为5000ms
        // option.setScanSpan(3000);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
    }

    //解决在Android Studio 运行的时候报E/dalvikvm: Could not find class ‘xxx’,
    //但是在android5.0以上不会报此错误能运行成功。
    //minifyEnabled false 意思是 是否进行混淆
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //   MultiDex.install(this);
    }


    /**
     * 获取该应用公共缓存路径
     *
     * @return
     * @author GISirFive
     */
    public static String getDiskCachePath() {
        return Environment.getExternalStorageDirectory() + "/SeaDreams/";
    }

    public void initDatabase() {
        // 获取应用程序版本号
        int appVersion = DeviceUtils.getInstance(this).getAppVersion();

        DbUtils.DaoConfig config = new DbUtils.DaoConfig(this);
        config.setDbName(ConfigUtils.getConfig(ConfigUtils.KEY.privateInfo) + ".db");
        config.setDbVersion(appVersion);
        config.setDbUpgradeListener(new DbUtils.DbUpgradeListener() {

            /** 当程序目录中的数据库版本与当前应用程序的版本不一致时，会调用此方法 */
            @Override
            public void onUpgrade(DbUtils db, int oldVersion, int newVersion) {
            }
        });
        BaseApp.DBLoader = DbUtils.create(config);
        BaseApp.DBLoader.configDebug(DEBUG);
    }
}
