package com.zbxn.main.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * 项目名称：ZBXMobile
 * 创建人：LiangHanXin
 * 创建时间：2016/9/30 10:00
 */
public class AttendanceRecordEntity implements Parcelable {

    /**
     * ID : 304246
     * UserID : 1372
     * UserName : 1372
     * CreateTime : 2017/3/1 0:05:56
     * CheckInState : -1
     * CheckInStateName : 未签到
     * CheckOutState : -1
     * CheckOutStateName : 未签退
     * CheckOutApprealState : -2
     * CheckInApprealState : -2
     * CheckInAddress : null
     * CheckOutAddress : null
     * Year : 2017
     * Month : 3
     * Day : 1
     * AttendanceDate : 2017-3-1
     * CheckInTime : 00:00
     * CheckOutTime : 00:00
     * CheckInLongitude : null
     * CheckInLatitude : null
     * CheckOutLongitude : null
     * CheckOutLatitude : null
     * CheckInAttendanceSource : web端
     * CheckOutAttendanceSource : web端
     */
    @Expose
    private int ID;
    @Expose
    private int UserID;
    @Expose
    private String UserName;
    @Expose
    private String CreateTime;
    @Expose
    private String CheckInState;
    @Expose
    private String CheckInStateName;
    @Expose
    private String CheckOutState;
    @Expose
    private String CheckOutStateName;
    @Expose
    private String CheckOutAppealState;
    @Expose
    private String CheckInAppealState;
    @Expose
    private String CheckInAddress;
    @Expose
    private String CheckOutAddress;
    @Expose
    private String Year;
    @Expose
    private String Month;
    @Expose
    private String Day;
    @Expose
    private String AttendanceDate;
    @Expose
    private String CheckInTime;
    @Expose
    private String CheckOutTime;
    @Expose
    private String CheckInLongitude;
    @Expose
    private String CheckInLatitude;
    @Expose
    private String CheckOutLongitude;
    @Expose
    private String CheckOutLatitude;
    @Expose
    private String CheckInAttendanceSource;
    @Expose
    private String CheckOutAttendanceSource;

    protected AttendanceRecordEntity(Parcel in) {
        ID = in.readInt();
        UserID = in.readInt();
        UserName = in.readString();
        CreateTime = in.readString();
        CheckInState = in.readString();
        CheckInStateName = in.readString();
        CheckOutState = in.readString();
        CheckOutStateName = in.readString();
        CheckOutAppealState = in.readString();
        CheckInAppealState = in.readString();
        CheckInAddress = in.readString();
        CheckOutAddress = in.readString();
        Year = in.readString();
        Month = in.readString();
        Day = in.readString();
        AttendanceDate = in.readString();
        CheckInTime = in.readString();
        CheckOutTime = in.readString();
        CheckInLongitude = in.readString();
        CheckInLatitude = in.readString();
        CheckOutLongitude = in.readString();
        CheckOutLatitude = in.readString();
        CheckInAttendanceSource = in.readString();
        CheckOutAttendanceSource = in.readString();
    }

    public static final Creator<AttendanceRecordEntity> CREATOR = new Creator<AttendanceRecordEntity>() {
        @Override
        public AttendanceRecordEntity createFromParcel(Parcel in) {
            return new AttendanceRecordEntity(in);
        }

        @Override
        public AttendanceRecordEntity[] newArray(int size) {
            return new AttendanceRecordEntity[size];
        }
    };

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

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

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getCheckInState() {
        return CheckInState;
    }

    public void setCheckInState(String CheckInState) {
        this.CheckInState = CheckInState;
    }

    public String getCheckInStateName() {
        return CheckInStateName;
    }

    public void setCheckInStateName(String CheckInStateName) {
        this.CheckInStateName = CheckInStateName;
    }

    public String getCheckOutState() {
        return CheckOutState;
    }

    public void setCheckOutState(String CheckOutState) {
        this.CheckOutState = CheckOutState;
    }

    public String getCheckOutStateName() {
        return CheckOutStateName;
    }

    public void setCheckOutStateName(String CheckOutStateName) {
        this.CheckOutStateName = CheckOutStateName;
    }

    public String getCheckOutApprealState() {
        return CheckOutAppealState;
    }

    public void setCheckOutApprealState(String CheckOutApprealState) {
        this.CheckOutAppealState = CheckOutApprealState;
    }

    public String getCheckInApprealState() {
        return CheckInAppealState;
    }

    public void setCheckInApprealState(String CheckInApprealState) {
        this.CheckInAppealState = CheckInApprealState;
    }

    public String getCheckInAddress() {
        return CheckInAddress;
    }

    public void setCheckInAddress(String CheckInAddress) {
        this.CheckInAddress = CheckInAddress;
    }

    public String getCheckOutAddress() {
        return CheckOutAddress;
    }

    public void setCheckOutAddress(String CheckOutAddress) {
        this.CheckOutAddress = CheckOutAddress;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String Year) {
        this.Year = Year;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String Month) {
        this.Month = Month;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String Day) {
        this.Day = Day;
    }

    public String getAttendanceDate() {
        return AttendanceDate;
    }

    public void setAttendanceDate(String AttendanceDate) {
        this.AttendanceDate = AttendanceDate;
    }

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

    public String getCheckInLongitude() {
        return CheckInLongitude;
    }

    public void setCheckInLongitude(String CheckInLongitude) {
        this.CheckInLongitude = CheckInLongitude;
    }

    public String getCheckInLatitude() {
        return CheckInLatitude;
    }

    public void setCheckInLatitude(String CheckInLatitude) {
        this.CheckInLatitude = CheckInLatitude;
    }

    public String getCheckOutLongitude() {
        return CheckOutLongitude;
    }

    public void setCheckOutLongitude(String CheckOutLongitude) {
        this.CheckOutLongitude = CheckOutLongitude;
    }

    public String getCheckOutLatitude() {
        return CheckOutLatitude;
    }

    public void setCheckOutLatitude(String CheckOutLatitude) {
        this.CheckOutLatitude = CheckOutLatitude;
    }

    public String getCheckInAttendanceSource() {
        return CheckInAttendanceSource;
    }

    public void setCheckInAttendanceSource(String CheckInAttendanceSource) {
        this.CheckInAttendanceSource = CheckInAttendanceSource;
    }

    public String getCheckOutAttendanceSource() {
        return CheckOutAttendanceSource;
    }

    public void setCheckOutAttendanceSource(String CheckOutAttendanceSource) {
        this.CheckOutAttendanceSource = CheckOutAttendanceSource;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeInt(UserID);
        dest.writeString(UserName);
        dest.writeString(CreateTime);
        dest.writeString(CheckInState);
        dest.writeString(CheckInStateName);
        dest.writeString(CheckOutState);
        dest.writeString(CheckOutStateName);
        dest.writeString(CheckOutAppealState);
        dest.writeString(CheckInAppealState);
        dest.writeString(CheckInAddress);
        dest.writeString(CheckOutAddress);
        dest.writeString(Year);
        dest.writeString(Month);
        dest.writeString(Day);
        dest.writeString(AttendanceDate);
        dest.writeString(CheckInTime);
        dest.writeString(CheckOutTime);
        dest.writeString(CheckInLongitude);
        dest.writeString(CheckInLatitude);
        dest.writeString(CheckOutLongitude);
        dest.writeString(CheckOutLatitude);
        dest.writeString(CheckInAttendanceSource);
        dest.writeString(CheckOutAttendanceSource);
    }
}
