package com.zbxn.crm.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2017/1/13.
 */
public class CostDetailEntity {
    @Expose
    private String FeeProject;
    @Expose
    private String Fee;
    @Expose
    private String FeeTime;
    @Expose
    private String Remark;
    @Expose
    private String ID;
    @Expose
    private String CustID;
    @Expose
    private String FeeTimeStr;

    @Override
    public String toString() {
        return "CostDetailEntity{" +
                "FeeProject='" + FeeProject + '\'' +
                ", Fee='" + Fee + '\'' +
                ", FeeTime='" + FeeTime + '\'' +
                ", Remark='" + Remark + '\'' +
                ", ID='" + ID + '\'' +
                ", CustID='" + CustID + '\'' +
                ", FeeTimeStr='" + FeeTimeStr + '\'' +
                '}';
    }

    public String getFeeProject() {
        return FeeProject;
    }

    public void setFeeProject(String feeProject) {
        FeeProject = feeProject;
    }

    public String getFee() {
        return Fee;
    }

    public void setFee(String fee) {
        Fee = fee;
    }

    public String getFeeTime() {
        return FeeTime;
    }

    public void setFeeTime(String feeTime) {
        FeeTime = feeTime;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCustID() {
        return CustID;
    }

    public void setCustID(String custID) {
        CustID = custID;
    }

    public String getFeeTimeStr() {
        return FeeTimeStr;
    }

    public void setFeeTimeStr(String feeTimeStr) {
        FeeTimeStr = feeTimeStr;
    }
}
