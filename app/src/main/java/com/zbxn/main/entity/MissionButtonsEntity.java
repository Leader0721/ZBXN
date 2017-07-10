package com.zbxn.main.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by wj on 2016/11/15.
 * 评论列表的实体类 任务
 */
public class MissionButtonsEntity  implements Serializable {


    /**
     * OperateName : 撤销
     * OperateClass : power_retract
     * ActionType : 13
     */
    @Expose
    private String OperateName;
    @Expose
    private String OperateClass;
    @Expose
    private int ActionType;

    public String getOperateName() {
        return OperateName;
    }

    public void setOperateName(String OperateName) {
        this.OperateName = OperateName;
    }

    public String getOperateClass() {
        return OperateClass;
    }

    public void setOperateClass(String OperateClass) {
        this.OperateClass = OperateClass;
    }

    public int getActionType() {
        return ActionType;
    }

    public void setActionType(int ActionType) {
        this.ActionType = ActionType;
    }
}
