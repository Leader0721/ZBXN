package com.zbxn.crm.entity;

import com.google.gson.annotations.Expose;

/**
 * @author: ysj
 * @date: 2017-01-20 11:54
 */
public class StaticTypeEntity implements Comparable<StaticTypeEntity> {

    /**
     * Key : 6
     * value : 个人资源
     * order : null
     */
    @Expose
    private String Name;
    @Expose
    private String Key;
    @Expose
    private String value;
    @Expose
    private Object order;
    private int index;

    public StaticTypeEntity() {
    }

    public StaticTypeEntity(int index, String key, String value, Object order) {
        this.index = index;
        this.Key = key;
        this.value = value;
        this.order = order;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String Key) {
        this.Key = Key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Object getOrder() {
        return order;
    }

    public void setOrder(Object order) {
        this.order = order;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int compareTo(StaticTypeEntity another) {
        int num = this.getIndex() - another.getIndex();
        return num;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
