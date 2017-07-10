package com.zbxn.crm.activity.custom;

import com.pub.utils.DateUtils;

/**
 * @author: ysj
 * @date: 2017-01-12 17:01
 */
public class CustomFiltrateSave {
    public static String date = DateUtils.getDate("yyyy-MM-dd");
    //记录筛选左边的item的id
    public static String itemSelect = "";
    //记录状态
    public static String mStateSelect = "";
    //记录来源
    public static String mSourceSelect = "";
    //记录行业
    public static String mTradeSelect = "";
    //记录选择的创建时间
    public static String creatTime = date;
    //记录选择的更新时间
    public static String updateTime = date;
    //记录选择的跟进人的姓名
    public static String peopleName = "请选择跟进人";
    //记录选择的跟进人的id
    public static String peopleId = "";
    //记录客户名称
    public static String mCustomName = "";
    //记录客户地址
    public static String mCustomAddress = "";
    //记录客户电话
    public static String mCustomPhone = "";
    //记录备注
    public static String mNote = "";
    //省
    public static String mProvinceName = "";
    public static String mProvinceId;
    //市
    public static String mCityName = "";
    public static String mCityId;
    //区
    public static String mRegionName = "";
    public static String mRegionId;

}
