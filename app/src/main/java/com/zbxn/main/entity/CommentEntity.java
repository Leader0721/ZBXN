package com.zbxn.main.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.Date;
import java.util.List;

/**
 * 评论
 * <br>
 * <b>功能：</b>CommentController<br>
 * <b>作者：</b>GISirFive<br>
 * <b>日期：</b> 2015-8-15 <br>
 * <b>版权所有：</b><br>
 */
public class CommentEntity implements Parcelable {

    @Expose
    private int id;//
    @Expose
    private String attachmentguid;//
    @Expose
    private String type;//
    @Expose
    private int dataId;//
    @Expose
    private String commentcontent;//
    @Expose
    private int createuserid;//
    @Expose
    private Date createtime;//
    @Expose
    private String createUserName; //评论发布人
    @Expose
    private List<ReplyEntity> replyEntityList; //回复列表

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAttachmentguid() {
        return attachmentguid;
    }

    public void setAttachmentguid(String attachmentguid) {
        this.attachmentguid = attachmentguid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public String getCommentcontent() {
        return commentcontent;
    }

    public void setCommentcontent(String commentcontent) {
        this.commentcontent = commentcontent;
    }

    public int getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(int createuserid) {
        this.createuserid = createuserid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public List<ReplyEntity> getReplyEntityList() {
        return replyEntityList;
    }

    public void setReplyEntityList(List<ReplyEntity> replyEntityList) {
        this.replyEntityList = replyEntityList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.attachmentguid);
        dest.writeString(this.type);
        dest.writeInt(this.dataId);
        dest.writeString(this.commentcontent);
        dest.writeInt(this.createuserid);
        dest.writeLong(this.createtime != null ? this.createtime.getTime() : -1);
        dest.writeString(this.createUserName);
        dest.writeTypedList(this.replyEntityList);
    }

    public CommentEntity() {
    }

    protected CommentEntity(Parcel in) {
        this.id = in.readInt();
        this.attachmentguid = in.readString();
        this.type = in.readString();
        this.dataId = in.readInt();
        this.commentcontent = in.readString();
        this.createuserid = in.readInt();
        long tmpCreateTime = in.readLong();
        this.createtime = tmpCreateTime == -1 ? null : new Date(tmpCreateTime);
        this.createUserName = in.readString();
        this.replyEntityList = in.createTypedArrayList(ReplyEntity.CREATOR);
    }

    public static final Creator<CommentEntity> CREATOR = new Creator<CommentEntity>() {
        @Override
        public CommentEntity createFromParcel(Parcel source) {
            return new CommentEntity(source);
        }

        @Override
        public CommentEntity[] newArray(int size) {
            return new CommentEntity[size];
        }
    };
}

