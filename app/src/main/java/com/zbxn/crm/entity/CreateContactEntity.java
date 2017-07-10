package com.zbxn.crm.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2017/1/13.
 */
public class CreateContactEntity {
    @Expose
    private String ContactMobile;
    @Expose
    private String ContactName;

    public String getContactMobile() {
        return ContactMobile;
    }

    public void setContactMobile(String contactMobile) {
        ContactMobile = contactMobile;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    @Override
    public String toString() {
        return "CreateContactEntity{" +
                "ContactMobile='" + ContactMobile + '\'' +
                ", ContactName='" + ContactName + '\'' +
                '}';
    }
}
