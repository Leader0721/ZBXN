package com.zbxn.main.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author: ysj
 * @date: 2016-11-14 14:35
 */
public class MissionDetailEntity implements Serializable {
    @Expose
    private int TaskState;//任务状态未返回
    @Expose
    private String CreatedTime;//创建时间没返回
    @Expose
    private ArrayList<MissionAttachmentEntity> AttachmentList = new ArrayList<>();//附件列表  没返回
    @Expose
    private int FinshChildTaskCount;//完成子任务数量   没返回

    @Expose
    private int TaskId;
    @Expose
    private String CreatorName;//创建人
    @Expose
    private int DifferentlyLevel;//难易等级
    @Expose
    private int Progress;//进度
    @Expose
    private String EndTime;//截至时间
    @Expose
    private String Title;//标题
    @Expose
    private String Info;//内容
    @Expose
    private int WorkHours;//小时
    @Expose
    private int WorkMins;//分钟
    @Expose
    private int ChargerId;//负责人ID
    @Expose
    private String ChargerName;//负责人
    @Expose
    private ArrayList<MissionTransactorsEntity> Transactors = new ArrayList<>();//执行人列表
    @Expose
    private int ReviewerId;//审核人ID
    @Expose
    private String ReviewerName;//审核人名称
    @Expose
    private int TaskLevel;//任务等级  是否紧急
    @Expose
    private String AttachmentGUID;//附件ID
    @Expose
    private int ChildTaskCount;//子任务数量
    @Expose
    private ArrayList<MissionCommendListEntity> Commments = new ArrayList<>();//评论列表
    @Expose
    private ArrayList<MissionOperateLogListEntity> OperateLog = new ArrayList<>();//操作日志列表
    @Expose
    private ArrayList<MissionReadListEntity> ReadList = new ArrayList<>();//阅读列表
    @Expose
    private ArrayList<MissionButtonsEntity> Buttons = new ArrayList<>();//操作按钮列表


    public int getFinshChildTaskCount() {
        return FinshChildTaskCount;
    }

    public void setFinshChildTaskCount(int finshChildTaskCount) {
        FinshChildTaskCount = finshChildTaskCount;
    }

    public String getCreateTime() {
        return CreatedTime;
    }

    public void setCreateTime(String createdTime) {
        CreatedTime = createdTime;
    }
    public int getChildTaskCount() {
        return ChildTaskCount;
    }

    public void setChildTaskCount(int childTaskCount) {
        ChildTaskCount = childTaskCount;
    }


    public int getTaskState() {
        return TaskState;
    }

    public void setTaskState(int taskState) {
        TaskState = taskState;
    }


    public ArrayList<MissionAttachmentEntity> getAttachmentList() {
        return AttachmentList;
    }

    public void setAttachmentList(ArrayList<MissionAttachmentEntity> attachmentList) {
        AttachmentList = attachmentList;
    }

    public int getTaskId() {
        return TaskId;
    }

    public void setTaskId(int taskId) {
        this.TaskId = taskId;
    }

    public String getCreatorName() {
        return CreatorName;
    }

    public void setCreatorName(String creatorName) {
        CreatorName = creatorName;
    }

    public int getDifferentlyLevel() {
        return DifferentlyLevel;
    }

    public void setDifferentlyLevel(int differentlyLevel) {
        DifferentlyLevel = differentlyLevel;
    }

    public int getProgress() {
        return Progress;
    }

    public void setProgress(int progress) {
        Progress = progress;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public int getWorkHours() {
        return WorkHours;
    }

    public void setWorkHours(int workHours) {
        WorkHours = workHours;
    }

    public int getWorkMins() {
        return WorkMins;
    }

    public void setWorkMins(int workMins) {
        WorkMins = workMins;
    }

    public int getChargerId() {
        return ChargerId;
    }

    public void setChargerId(int chargerId) {
        ChargerId = chargerId;
    }

    public String getChargerName() {
        return ChargerName;
    }

    public void setChargerName(String chargerName) {
        ChargerName = chargerName;
    }

    public ArrayList<MissionTransactorsEntity> getTransactors() {
        return Transactors;
    }

    public void setTransactors(ArrayList<MissionTransactorsEntity> transactors) {
        Transactors = transactors;
    }

    public int getReviewerId() {
        return ReviewerId;
    }

    public void setReviewerId(int reviewerId) {
        ReviewerId = reviewerId;
    }

    public String getReviewerName() {
        return ReviewerName;
    }

    public void setReviewerName(String reviewerName) {
        ReviewerName = reviewerName;
    }

    public int getTaskLevel() {
        return TaskLevel;
    }

    public void setTaskLevel(int taskLevel) {
        TaskLevel = taskLevel;
    }

    public String getAttachmentGUID() {
        return AttachmentGUID;
    }

    public void setAttachmentGUID(String attachmentGUID) {
        AttachmentGUID = attachmentGUID;
    }

    public ArrayList<MissionCommendListEntity> getCommments() {
        return Commments;
    }

    public void setCommments(ArrayList<MissionCommendListEntity> commments) {
        Commments = commments;
    }

    public ArrayList<MissionOperateLogListEntity> getOperateLog() {
        return OperateLog;
    }

    public void setOperateLog(ArrayList<MissionOperateLogListEntity> operateLog) {
        OperateLog = operateLog;
    }

    public ArrayList<MissionReadListEntity> getReadList() {
        return ReadList;
    }

    public void setReadList(ArrayList<MissionReadListEntity> readList) {
        ReadList = readList;
    }

    public ArrayList<MissionButtonsEntity> getButtons() {
        return Buttons;
    }

    public void setButtons(ArrayList<MissionButtonsEntity> buttons) {
        Buttons = buttons;
    }

}
