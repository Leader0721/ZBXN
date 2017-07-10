package com.zbxn.crm.entity;

import com.google.gson.annotations.Expose;

/**
 * @author: ysj
 * @date: 2017-01-17 14:52
 */
public class OperationLogEntity {

    /**
     * OperateRecord : 新增客户
     * CreateUserID : 3
     * CreateUserName : 刘凯
     * CreateTime : 2017-01-13T23:15:53.557
     * CreateTimeStr : 2017-01-13 23:15:53
     */
    @Expose
    private String OperateRecord;
    @Expose
    private int CreateUserID;
    @Expose
    private String CreateUserName;
    @Expose
    private String CreateTime;
    @Expose
    private String CreateTimeStr;

    public String getOperateRecord() {
        return OperateRecord;
    }

    public void setOperateRecord(String OperateRecord) {
        this.OperateRecord = OperateRecord;
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

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getCreateTimeStr() {
        return CreateTimeStr;
    }

    public void setCreateTimeStr(String CreateTimeStr) {
        this.CreateTimeStr = CreateTimeStr;
    }
}
