package com.pub.utils;

import com.pub.base.BaseApp;

public class ConfigUtils {

    public enum KEY {
        /**
         * java服务器地址
         */
        server("http://n.zbzbx.com:8088/ZBXMobile/"),//正式
//        server("http://192.168.1.112:8060/ZBXMobile/"),//开发库吴泽羽
//        server("http://www.tt8ss.com:8080/ZBXMobile/"),//测试库
        /**
         * WebApi服务器地址(由.net提供)
         */
        NetServer_ACTION("http://n.zbzbx.com/webapi_action/"),//正式
//        NetServer_ACTION("http://test.zbzbx.com/WebApi_action/"),//测试库
//        NetServer_ACTION("http://dev.zbzbx.com/WebApi_action/"),//开发库
        /**
         * Web端服务器地址
         */
        webServer("http://n.zbzbx.com/"),
        /**
         * 配置文件名
         */
        privateInfo("zbzbx"),
        /**
         * 默认每一页可以装载的信息数量
         */
        pageSize(12),
        /**
         * 是否为调试模式
         */
        debug(true),
        /**
         * 用户信息
         */
        userInfo("tempInfo"),
        /**
         * 数据库版本
         */
        dbVersion(100);

        Object value;

        KEY(Object value) {
            this.value = value;
        }
    }

    public static final String getConfig(KEY key) {
        switch (key) {
//		case server:
//			return AESUtils.decode(BUNDLE.getString(name.toString()));
            default:
                return String.valueOf(key.value);
        }
    }

    //Hybrid混合开发压缩包下载地址 SDCard/Android/data/你的应用包名/cache/目录
    public static String HYBRID_DOWNLOAD_DIR = BaseApp.getContext().getExternalCacheDir() + "/";
    //Hybrid混合开发目录地址
    public static String HYBRID_DIR = BaseApp.getContext().getFilesDir().getAbsolutePath() + "/" + "H5Dir" + "/";
}