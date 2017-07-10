package com.zbxn.main.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by U on 2017/2/13.
 */

public class WorkmateEntity implements Serializable{

    @Expose
    private String UserId;
    @Expose
    private String PhotoUrl;
    @Expose
    private String OrderNum;
    @Expose
    private String ReadCount;
    @Expose
    private String UserName;
    @Expose
    private String Permission;//权限

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    public String getOrderNum() {
        return OrderNum;
    }

    public void setOrderNum(String orderNum) {
        OrderNum = orderNum;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPermission() {
        return Permission;
    }

    public void setPermission(String permission) {
        Permission = permission;
    }

    public String getReadCount() {
        return ReadCount;
    }

    public void setReadCount(String readCount) {
        ReadCount = readCount;
    }
}
