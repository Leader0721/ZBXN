package com.zbxn.crm.entity;

import com.google.gson.annotations.Expose;

/**
 * 行业
 *
 * @author: ysj
 * @date: 2017-01-21 19:20
 */
public class IndustryEntity {

    /**
     * ParentCatalogName : 交通运输、仓储和邮政业
     * ParentCatalogCode : F
     */
    @Expose
    private String ParentCatalogName;
    @Expose
    private String ParentCatalogCode;

    public String getParentCatalogName() {
        return ParentCatalogName;
    }

    public void setParentCatalogName(String ParentCatalogName) {
        this.ParentCatalogName = ParentCatalogName;
    }

    public String getParentCatalogCode() {
        return ParentCatalogCode;
    }

    public void setParentCatalogCode(String ParentCatalogCode) {
        this.ParentCatalogCode = ParentCatalogCode;
    }
}
