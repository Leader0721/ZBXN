package com.zbxn.main.entity;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by ysj on 2016/11/8.
 */
public class MissionEntity {
    @Expose
    private int PersonTaskState;//弃用
    @Expose
    private ArrayList<MissionButtonsEntity> Buttons = new ArrayList<>();//操作按钮列表  没返回

    @Expose
    private String ID;//
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
    private int ChargerId;//负责人ID
    @Expose
    private String ChargerName;//负责人
    @Expose
    private String Transactors;//执行人列表
    @Expose
    private int ReviewerId;//审核人ID
    @Expose
    private String ReviewerName;//审核人名称
    @Expose
    private int TaskStatus;//任务状态

    @Expose
    private String EndTimeLength;//超期

    @Expose
    private int ChildTaskCount;//子任务数量
    @Expose
    private int FinshChildTaskCount;//完成子任务数量

    //任务各状态
    @Expose
    private boolean IsVerfiy;
    @Expose
    private boolean IsOverTime;
    @Expose
    private boolean IsRebut;
    @Expose
    private boolean IsAccepted;
    @Expose
    private boolean IsRefused;
    @Expose
    private boolean IsNew;
    @Expose
    private boolean IsInvalid;
    @Expose
    private boolean IsAbnormal;
    @Expose
    private boolean IsUnFinish;
    @Expose
    private boolean IsUrgent;


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


    public String getCreatorName() {
        return CreatorName;
    }

    public void setCreatorName(String PersonCreateName) {
        this.CreatorName = PersonCreateName;
    }


    public int getDifferentlyLevel() {
        return DifferentlyLevel;
    }

    public void setDifferentlyLevel(int DifficultyLevel) {
        this.DifferentlyLevel = DifficultyLevel;
    }

    public int getTaskStatus() {
        return TaskStatus;
    }

    public void setTaskStatus(int TaskState) {
        this.TaskStatus = TaskState;
    }

    public int getProgress() {
        return Progress;
    }

    public void setProgress(int DoneProgress) {
        this.Progress = DoneProgress;
    }

    public String getEndTimeLength() {
        return EndTimeLength;
    }

    public void setEndTimeLength(String endTimeLength) {
        EndTimeLength = endTimeLength;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String EndTime) {
        this.EndTime = EndTime;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String TaskTitle) {
        this.Title = TaskTitle;
    }


    public int getPersonTaskState() {
        return PersonTaskState;
    }

    public void setPersonTaskState(int personTaskState) {
        PersonTaskState = personTaskState;
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

    public String getTransactors() {
        return Transactors;
    }

    public void setTransactors(String transactors) {
        Transactors = transactors;
    }

    public int getChildTaskCount() {
        return ChildTaskCount;
    }

    public void setChildTaskCount(int childTaskCount) {
        ChildTaskCount = childTaskCount;
    }

    public int getFinshChildTaskCount() {
        return FinshChildTaskCount;
    }

    public void setFinshChildTaskCount(int finshChildTaskCount) {
        FinshChildTaskCount = finshChildTaskCount;
    }

    public ArrayList<MissionButtonsEntity> getButtons() {
        return Buttons;
    }

    public void setButtons(ArrayList<MissionButtonsEntity> buttons) {
        Buttons = buttons;
    }

    public boolean isVerfiy() {
        return IsVerfiy;
    }

    public void setVerfiy(boolean verfiy) {
        IsVerfiy = verfiy;
    }

    public boolean isOverTime() {
        return IsOverTime;
    }

    public void setOverTime(boolean overTime) {
        IsOverTime = overTime;
    }

    public boolean isRebut() {
        return IsRebut;
    }

    public void setRebut(boolean rebut) {
        IsRebut = rebut;
    }

    public boolean isAccepted() {
        return IsAccepted;
    }

    public void setAccepted(boolean accepted) {
        IsAccepted = accepted;
    }

    public boolean isRefused() {
        return IsRefused;
    }

    public void setRefused(boolean refused) {
        IsRefused = refused;
    }

    public boolean isNew() {
        return IsNew;
    }

    public void setNew(boolean aNew) {
        IsNew = aNew;
    }

    public boolean isInvalid() {
        return IsInvalid;
    }

    public void setInvalid(boolean invalid) {
        IsInvalid = invalid;
    }

    public boolean isAbnormal() {
        return IsAbnormal;
    }

    public void setAbnormal(boolean abnormal) {
        IsAbnormal = abnormal;
    }

    public boolean isUnFinish() {
        return IsUnFinish;
    }

    public void setUnFinish(boolean unFinish) {
        IsUnFinish = unFinish;
    }

    public boolean isUrgent() {
        return IsUrgent;
    }

    public void setUrgent(boolean urgent) {
        IsUrgent = urgent;
    }
}
