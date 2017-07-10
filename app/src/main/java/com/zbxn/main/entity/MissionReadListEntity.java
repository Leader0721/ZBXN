package com.zbxn.main.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by wj on 2016/11/15.
 * 评论列表的实体类 任务
 */
public class MissionReadListEntity  implements Serializable {


    /**
     * UserName : 刘凯
     * IsRead : true
     * ReadTime : 2016-12-05 18:18
     */
    @Expose
    private String UserName;
    @Expose
    private String UserHeaderImg;
    @Expose
    private boolean IsRead;
    @Expose
    private String ReadTime;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public boolean isIsRead() {
        return IsRead;
    }

    public void setIsRead(boolean IsRead) {
        this.IsRead = IsRead;
    }

    public String getReadTime() {
        return ReadTime;
    }

    public void setReadTime(String ReadTime) {
        this.ReadTime = ReadTime;
    }

    public String getUserHeaderImg() {
        return UserHeaderImg;
    }

    public void setUserHeaderImg(String userHeaderImg) {
        UserHeaderImg = userHeaderImg;
    }
}
