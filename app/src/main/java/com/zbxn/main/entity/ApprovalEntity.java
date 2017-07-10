package com.zbxn.main.entity;

import com.google.gson.annotations.Expose;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 审批主界面图标等
 *
 * @author: ysj
 * @date: 2016-10-12 09:25
 */
@Table(name = "RecTool")
public class ApprovalEntity implements Comparable<ApprovalEntity> {

    /**
     * typeid : 1
     * name : 请假
     */
//状态
//2进行中          3待审核4已超期5已驳回14紧急8已拒绝1待接受
//1待接受          10新任务14紧急
//8已拒绝
//0草稿箱
//6任务归档
//13任务统计
//16任务监督
//11任务交接

//类型
//0全部1我创建4我负责2我执行5我评审
    @Expose
    private int typeid; // id
    @Expose
    private int secondId; // secondid二级id
    @Expose
    private String name; //审批项目
    @Expose
    private String img; //图片地址

    public ApprovalEntity() {

    }

    public ApprovalEntity(int typeid, String name) {
        this.typeid = typeid;
        this.name = name;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public int compareTo(ApprovalEntity another) {
        int num = this.getTypeid() - another.getTypeid();
        return num;
    }
}
