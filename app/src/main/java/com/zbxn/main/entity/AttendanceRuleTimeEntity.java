package com.zbxn.main.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * 项目名称：ZBXMobile
 * 创建人：LiangHanXin
 * 创建时间：2016/9/30 10:00
 */
public class AttendanceRuleTimeEntity implements Serializable {

    /**
     * CheckInTime : 08:20
     * CheckOutTime : 10:10
     */
    @Expose
    private String CheckInTime;
    @Expose
    private String CheckOutTime;

    public String getCheckInTime() {
        return CheckInTime;
    }

    public void setCheckInTime(String CheckInTime) {
        this.CheckInTime = CheckInTime;
    }

    public String getCheckOutTime() {
        return CheckOutTime;
    }

    public void setCheckOutTime(String CheckOutTime) {
        this.CheckOutTime = CheckOutTime;
    }
}
