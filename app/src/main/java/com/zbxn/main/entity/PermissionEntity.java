package com.zbxn.main.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by U on 2017/1/11.
 */

public class PermissionEntity {
    @Expose
    private int ID;
    @Expose
    private int ParentID;
    @Expose
    private String PermissionName;
    @Expose
    private String PermissionDesc;
    @Expose
    private boolean IsCheck;



    /**
     * 是否有子节点元素
     */
    @Expose
    private boolean isHasChild;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getParentID() {
        return ParentID;
    }

    public void setParentID(int parentID) {
        ParentID = parentID;
    }

    public String getPermissionName() {
        return PermissionName;
    }

    public void setPermissionName(String permissionName) {
        PermissionName = permissionName;
    }

    public String getPermissionDesc() {
        return PermissionDesc;
    }

    public void setPermissionDesc(String permissionDesc) {
        PermissionDesc = permissionDesc;
    }

    public boolean isCheck() {
        return IsCheck;
    }

    public void setCheck(boolean check) {
        IsCheck = check;
    }

    public boolean isHasChild() {
        return isHasChild;
    }

    public void setHasChild(boolean hasChild) {
        isHasChild = hasChild;
    }
}
