package com.zbxn.crm.entity;

import com.google.gson.annotations.Expose;

/**
 * 跟进记录评论
 *
 * @author: ysj
 * @date: 2017-01-23 17:53
 */
public class FollowCommentEntity {

    /**
     * ID : 927e3ada-29d5-4c45-a9f3-0707ddccdc7e
     * FollowRecordID : 4a4f52b2-6817-40c9-bc4b-3c4b40f81448
     * ReplyToID : 4a4f52b2-6817-40c9-bc4b-3c4b40f81448
     * ReplyToUserName : 333333
     * ReplyToUserID : 1372
     * ReplyContent : 7777777777
     * CreateUserID : 1372
     * CreateUserName : 333333
     * CreateUserIcon : /Assets/oa_InformNotice/img/ZMS_Message/touxiang.png
     * CreateTime : 2017-01-23T17:33:32.073
     * CreateTimeStr : 2017-01-23 17:33:32
     * ZMSCompanyID : 1
     */
    @Expose
    private String ID;
    @Expose
    private String FollowRecordID;
    @Expose
    private String ReplyToID;
    @Expose
    private String ReplyToUserName;
    @Expose
    private int ReplyToUserID;
    @Expose
    private String ReplyContent;
    @Expose
    private int CreateUserID;
    @Expose
    private String CreateUserName;
    @Expose
    private String CreateUserIcon;
    @Expose
    private String CreateTime;
    @Expose
    private String CreateTimeStr;
    @Expose
    private int ZMSCompanyID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFollowRecordID() {
        return FollowRecordID;
    }

    public void setFollowRecordID(String FollowRecordID) {
        this.FollowRecordID = FollowRecordID;
    }

    public String getReplyToID() {
        return ReplyToID;
    }

    public void setReplyToID(String ReplyToID) {
        this.ReplyToID = ReplyToID;
    }

    public String getReplyToUserName() {
        return ReplyToUserName;
    }

    public void setReplyToUserName(String ReplyToUserName) {
        this.ReplyToUserName = ReplyToUserName;
    }

    public int getReplyToUserID() {
        return ReplyToUserID;
    }

    public void setReplyToUserID(int ReplyToUserID) {
        this.ReplyToUserID = ReplyToUserID;
    }

    public String getReplyContent() {
        return ReplyContent;
    }

    public void setReplyContent(String ReplyContent) {
        this.ReplyContent = ReplyContent;
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

    public String getCreateUserIcon() {
        return CreateUserIcon;
    }

    public void setCreateUserIcon(String CreateUserIcon) {
        this.CreateUserIcon = CreateUserIcon;
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

    public int getZMSCompanyID() {
        return ZMSCompanyID;
    }

    public void setZMSCompanyID(int ZMSCompanyID) {
        this.ZMSCompanyID = ZMSCompanyID;
    }
}
