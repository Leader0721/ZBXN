package com.zbxn.main.entity;

import com.google.gson.annotations.Expose;

/**
 * @author: ysj
 * @date: 2017-03-01 18:05
 */
public class AttendanceCountEntity {

    /**
     * UserID : 1372
     * UserName : 333333
     * LateCount : 0
     * OutCount : 0
     * Late : 0
     * NoCheckInCount : 2
     * LeaveEarlyCount : 0
     * NoCheckOutCount : 2
     * LeaveCount : 0
     */
    @Expose
    private int UserID;
    @Expose
    private String UserName;
    @Expose
    private int LateCount;
    @Expose
    private int OutCount;
    @Expose
    private int Late;
    @Expose
    private int NoCheckInCount;
    @Expose
    private int LeaveEarlyCount;
    @Expose
    private int NoCheckOutCount;
    @Expose
    private int LeaveCount;

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int UserID) {
        this.UserID = UserID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public int getLateCount() {
        return LateCount;
    }

    public void setLateCount(int LateCount) {
        this.LateCount = LateCount;
    }

    public int getOutCount() {
        return OutCount;
    }

    public void setOutCount(int OutCount) {
        this.OutCount = OutCount;
    }

    public int getLate() {
        return Late;
    }

    public void setLate(int Late) {
        this.Late = Late;
    }

    public int getNoCheckInCount() {
        return NoCheckInCount;
    }

    public void setNoCheckInCount(int NoCheckInCount) {
        this.NoCheckInCount = NoCheckInCount;
    }

    public int getLeaveEarlyCount() {
        return LeaveEarlyCount;
    }

    public void setLeaveEarlyCount(int LeaveEarlyCount) {
        this.LeaveEarlyCount = LeaveEarlyCount;
    }

    public int getNoCheckOutCount() {
        return NoCheckOutCount;
    }

    public void setNoCheckOutCount(int NoCheckOutCount) {
        this.NoCheckOutCount = NoCheckOutCount;
    }

    public int getLeaveCount() {
        return LeaveCount;
    }

    public void setLeaveCount(int LeaveCount) {
        this.LeaveCount = LeaveCount;
    }
}
