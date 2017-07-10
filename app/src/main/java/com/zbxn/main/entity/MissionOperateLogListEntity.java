package com.zbxn.main.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by wj on 2016/11/15.
 * 评论列表的实体类 任务
 */
public class MissionOperateLogListEntity  implements Serializable {


    /**
     * ID : 4150
     * OperatedTime : 2016-12-05 18:17
     * Resion : 新增任务
     * OperateType : 9
     * OperateUserName : 刘凯
     */
    @Expose
    private int ID;
    @Expose
    private String OperatedTime;
    @Expose
    private String Resion;
    @Expose
    private int OperateType;
    @Expose
    private String OperateUserName;
    @Expose
    private String UserHeaderImg;


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getOperatedTime() {
        return OperatedTime;
    }

    public void setOperatedTime(String OperatedTime) {
        this.OperatedTime = OperatedTime;
    }

    public String getResion() {
        return Resion;
    }

    public void setResion(String Resion) {
        this.Resion = Resion;
    }

    public int getOperateType() {
        return OperateType;
    }

    public void setOperateType(int OperateType) {
        this.OperateType = OperateType;
    }

    public String getOperateUserName() {
        return OperateUserName;
    }

    public void setOperateUserName(String OperateUserName) {
        this.OperateUserName = OperateUserName;
    }

    public String getUserHeaderImg() {
        return UserHeaderImg;
    }

    public void setUserHeaderImg(String userHeaderImg) {
        UserHeaderImg = userHeaderImg;
    }
}
