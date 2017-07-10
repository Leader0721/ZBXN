package com.zbxn.main.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wj on 2016/11/15.
 * 评论列表的实体类 任务
 */
public class MissionCommendListEntity  implements Serializable {


    /**
     * Id : 993
     * Content : 这是要被牵连扣分吗？
     * CommentDate : 2016-12-30 21:10:02
     * UserName : 丁峰
     * UserId : 600
     * UserHeaderImg : /attached/Image/UserPhoto/20161127/14801791784980.jpg
     * Replaies : []
     */
    @Expose
    private int Id;
    @Expose
    private String Content;
    @Expose
    private String CommentDate;
    @Expose
    private String UserName;
    @Expose
    private String UserId;
    @Expose
    private String UserHeaderImg;
    @Expose
    private List<?> Replaies;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getCommentDate() {
        return CommentDate;
    }

    public void setCommentDate(String CommentDate) {
        this.CommentDate = CommentDate;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getUserHeaderImg() {
        return UserHeaderImg;
    }

    public void setUserHeaderImg(String UserHeaderImg) {
        this.UserHeaderImg = UserHeaderImg;
    }

    public List<?> getReplaies() {
        return Replaies;
    }

    public void setReplaies(List<?> Replaies) {
        this.Replaies = Replaies;
    }
}
