package com.zbxn.crm.entity;

import com.google.gson.annotations.Expose;

/**
 * @author: ysj
 * @date: 2017-01-19 15:34
 */
public class CustomListEntity {

    /**
     * ID : 93faaef6-cc5a-499e-b171-34e87a3abfb4
     * CustName : 34534
     * FollowUserName :
     * UpdateTimeStr : 2017-01-13 23:15:53
     * CustState : 4
     */
    @Expose
    private String ID;
    @Expose
    private String CustName;
    @Expose
    private String FollowUserName;
    @Expose
    private String UpdateTimeStr;
    @Expose
    private int CustState;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String CustName) {
        this.CustName = CustName;
    }

    public String getFollowUserName() {
        return FollowUserName;
    }

    public void setFollowUserName(String FollowUserName) {
        this.FollowUserName = FollowUserName;
    }

    public String getUpdateTimeStr() {
        return UpdateTimeStr;
    }

    public void setUpdateTimeStr(String UpdateTimeStr) {
        this.UpdateTimeStr = UpdateTimeStr;
    }

    public int getCustState() {
        return CustState;
    }

    public void setCustState(int CustState) {
        this.CustState = CustState;
    }

}
