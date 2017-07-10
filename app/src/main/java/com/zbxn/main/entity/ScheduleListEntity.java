package com.zbxn.main.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by U on 2017/2/17.
 */

public class ScheduleListEntity {

    /**
     * ScheduleID : 1054
     * CreateUserID : 1374
     * CreateUserName : 555555
     * ForUserID : 1374
     * ForUserName : 555555
     * Title : 33333
     * IsAllday : true
     * StartTime : 2017-02-13T00:00:00
     * EndTime : 2017-02-13T00:00:00
     * Permission : 111
     */
    @Expose
    private int ScheduleID;
    @Expose
    private int CreateUserID;
    @Expose
    private String CreateUserName;
    @Expose
    private int ForUserID;
    @Expose
    private String ForUserName;
    @Expose
    private String Title;
    @Expose
    private boolean IsAllday;
    @Expose
    private String StartTime;
    @Expose
    private String EndTime;
    @Expose
    private int Permission;
    @Expose
    private int IsCrossday;

    public int getIsCrossday() {
        return IsCrossday;
    }

    public void setIsCrossday(int isCrossday) {
        IsCrossday = isCrossday;
    }

    public int getScheduleID() {
        return ScheduleID;
    }

    public void setScheduleID(int ScheduleID) {
        this.ScheduleID = ScheduleID;
    }

    public int getCreateUserID() {
        return CreateUserID;
    }

    public void setCreateUserID(int CreateUserID) {
        this.CreateUserID = CreateUserID;
    }

    public String getCreateUserName() {
        return CreateUserName;
    }

    public void setCreateUserName(String CreateUserName) {
        this.CreateUserName = CreateUserName;
    }

    public int getForUserID() {
        return ForUserID;
    }

    public void setForUserID(int ForUserID) {
        this.ForUserID = ForUserID;
    }

    public String getForUserName() {
        return ForUserName;
    }

    public void setForUserName(String ForUserName) {
        this.ForUserName = ForUserName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public boolean isIsAllday() {
        return IsAllday;
    }

    public void setIsAllday(boolean IsAllday) {
        this.IsAllday = IsAllday;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String StartTime) {
        this.StartTime = StartTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String EndTime) {
        this.EndTime = EndTime;
    }

    public int getPermission() {
        return Permission;
    }

    public void setPermission(int Permission) {
        this.Permission = Permission;
    }


    @Override
    public String toString() {
        return "ScheduleListEntity{" +
                "ScheduleID=" + ScheduleID +
                ", CreateUserID=" + CreateUserID +
                ", CreateUserName='" + CreateUserName + '\'' +
                ", ForUserID=" + ForUserID +
                ", ForUserName='" + ForUserName + '\'' +
                ", Title='" + Title + '\'' +
                ", IsAllday=" + IsAllday +
                ", StartTime='" + StartTime + '\'' +
                ", EndTime='" + EndTime + '\'' +
                ", Permission=" + Permission +
                ", IsCrossday=" + IsCrossday +
                '}';
    }
}
