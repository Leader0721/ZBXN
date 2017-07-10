package com.zbxn.crm.entity;

import com.google.gson.annotations.Expose;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/13.
 */
@Table(name = "base_Region")
public class ProvinceEntity implements Serializable {
    @Expose
    @Column
    private String RegionName;
    @Expose
    @Column
    private String RegionCode;
    @Expose
    @Column
    private String ParentCode;
    @Expose
    @Column
    private String RegionLevel;
    @Expose
    @Column
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParentCode() {
        return ParentCode;
    }

    public void setParentCode(String parentCode) {
        this.ParentCode = parentCode;
    }

    public String getRegionLevel() {
        return RegionLevel;
    }

    public void setRegionLevel(String regionLevel) {
        this.RegionLevel = regionLevel;
    }

    public String getRegionCode() {
        return RegionCode;
    }

    public void setRegionCode(String regionCode) {
        this.RegionCode = regionCode;
    }

    public String getRegionName() {
        return RegionName;
    }

    public void setRegionName(String regionName) {
        this.RegionName = regionName;
    }

    @Override
    public String toString() {
        return "ProvinceEntity{" +
                "RegionName='" + RegionName + '\'' +
                ", RegionCode='" + RegionCode + '\'' +
                ", ParentCode='" + ParentCode + '\'' +
                ", RegionLevel='" + RegionLevel + '\'' +
                ", id=" + id +
                '}';
    }
}
