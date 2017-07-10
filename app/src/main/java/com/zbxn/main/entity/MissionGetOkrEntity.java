package com.zbxn.main.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * 项目名称：获取okr积分的Entity
 * 创建人：LiangHanXin
 * 创建时间：2016/10/10 10:07
 */
public class MissionGetOkrEntity implements Serializable {
    @Expose
    private int okr;

    public int getOkr() {
        return okr;
    }

    public void setOkr(int okr) {
        this.okr = okr;
    }
}
