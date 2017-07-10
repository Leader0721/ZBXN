package com.zbxn.crm.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;


/**
 * Created by Administrator on 2017/1/20.
 */
public class CustomDetailEntity implements Serializable{
    @Expose
    private String ID;
    @Expose
    private String UserName;
    @Expose
    private String UserIcon;
    @Expose
    private String Name;
    @Expose
    private String Mobile;
    @Expose
    private String Position;
    @Expose
    private String UserID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserIcon() {
        return UserIcon;
    }

    public void setUserIcon(String userIcon) {
        UserIcon = userIcon;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}
