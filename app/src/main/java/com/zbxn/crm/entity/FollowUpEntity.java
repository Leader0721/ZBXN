package com.zbxn.crm.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: ysj
 * @date: 2017-01-13 19:07
 */
public class FollowUpEntity implements Parcelable {


    /**
     * ID : a734d3eb-69f7-4357-a954-0b08da44df8a
     * RecordType : 4
     * RecordContent : 测试附件
     * CustID : 837fbbca-c43a-44b7-995d-39ca4ca0b8c4
     * AttachmentGUID : 8f042337-e592-4d4b-89d1-f7279c86ba97
     * CreateTime : 2017-01-23T14:06:56.38
     * AttachmentStr : [{"Type":null,"Url":null}]
     * CreateUserID : 3
     * CreateUserName : 刘凯
     * CreateTimeStr : 2017-01-23 14:06:56
     * ReplyCount : 0
     */
    @Expose
    private String CreateUserIcon;
    @Expose
    private String ID;
    @Expose
    private int RecordType;
    @Expose
    private String RecordContent;
    @Expose
    private String CustID;
    @Expose
    private String AttachmentGUID;
    @Expose
    private String CreateTime;
    @Expose
    private int CreateUserID;
    @Expose
    private String CreateUserName;
    @Expose
    private String CreateTimeStr;
    @Expose
    private int ReplyCount;
    @Expose
    private List<AttachmentStrBean> AttachmentStr;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getRecordType() {
        return RecordType;
    }

    public void setRecordType(int RecordType) {
        this.RecordType = RecordType;
    }

    public String getRecordContent() {
        return RecordContent;
    }

    public void setRecordContent(String RecordContent) {
        this.RecordContent = RecordContent;
    }

    public String getCustID() {
        return CustID;
    }

    public void setCustID(String CustID) {
        this.CustID = CustID;
    }

    public String getAttachmentGUID() {
        return AttachmentGUID;
    }

    public void setAttachmentGUID(String AttachmentGUID) {
        this.AttachmentGUID = AttachmentGUID;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
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

    public String getCreateTimeStr() {
        return CreateTimeStr;
    }

    public void setCreateTimeStr(String CreateTimeStr) {
        this.CreateTimeStr = CreateTimeStr;
    }

    public int getReplyCount() {
        return ReplyCount;
    }

    public void setReplyCount(int ReplyCount) {
        this.ReplyCount = ReplyCount;
    }

    public List<AttachmentStrBean> getAttachmentStr() {
        return AttachmentStr;
    }

    public void setAttachmentStr(List<AttachmentStrBean> AttachmentStr) {
        this.AttachmentStr = AttachmentStr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ID);
        dest.writeInt(this.RecordType);
        dest.writeString(this.RecordContent);
        dest.writeString(this.CustID);
        dest.writeString(this.AttachmentGUID);
        dest.writeString(this.CreateTime);
        dest.writeList(this.AttachmentStr);
        dest.writeInt(this.CreateUserID);
        dest.writeString(this.CreateUserName);
        dest.writeString(this.CreateTimeStr);
        dest.writeInt(this.ReplyCount);
        dest.writeString(this.CreateUserIcon);
    }

    protected FollowUpEntity(Parcel in) {
        this.ID = in.readString();
        this.RecordType = in.readInt();
        this.RecordContent = in.readString();
        this.CustID = in.readString();
        this.AttachmentGUID = in.readString();
        this.CreateTime = in.readString();
        this.AttachmentStr = new ArrayList<AttachmentStrBean>();
        in.readList(this.AttachmentStr, AttachmentStrBean.class.getClassLoader());
        this.CreateUserID = in.readInt();
        this.CreateUserName = in.readString();
        this.CreateTimeStr = in.readString();
        this.ReplyCount = in.readInt();
        this.CreateUserIcon = in.readString();
    }

    public FollowUpEntity() {
    }

    public String getCreateUserIcon() {
        return CreateUserIcon;
    }

    public void setCreateUserIcon(String createUserIcon) {
        CreateUserIcon = createUserIcon;
    }

    public static class AttachmentStrBean implements Serializable {
        /**
         * Type : null
         * Url : null
         */

        private String Type;
        private String Url;

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
        }

        public String getUrl() {
            return Url;
        }

        public void setUrl(String Url) {
            this.Url = Url;
        }
    }

    public static final Creator<FollowUpEntity> CREATOR = new Creator<FollowUpEntity>() {
        @Override
        public FollowUpEntity createFromParcel(Parcel source) {
            return new FollowUpEntity(source);
        }

        @Override
        public FollowUpEntity[] newArray(int size) {
            return new FollowUpEntity[size];
        }
    };
}
