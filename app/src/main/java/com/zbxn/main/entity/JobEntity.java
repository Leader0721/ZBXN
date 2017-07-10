package com.zbxn.main.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * @author: ysj
 * @date: 2016-12-29 12:03
 */
public class JobEntity implements Serializable {

    /**
     * PositionName : 员工
     * PositionID : 111
     */
    @Expose
    private String PositionName;
    @Expose
    private int PositionID;
    @Expose
    private int Count;
    @Expose
    private String Desc;

    public String getPositionDesc() {
        return PositionDesc;
    }

    public void setPositionDesc(String positionDesc) {
        PositionDesc = positionDesc;
    }

    @Expose
    private String PositionDesc;

    @Expose
    private String PerList;


    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getPerList() {
        return PerList;
    }

    public void setPerList(String perList) {
        PerList = perList;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public String getPositionName() {
        return PositionName;
    }

    public void setPositionName(String PositionName) {
        this.PositionName = PositionName;
    }

    public int getPositionID() {
        return PositionID;
    }

    public void setPositionID(int PositionID) {
        this.PositionID = PositionID;
    }
}
