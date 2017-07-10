package com.zbxn.main.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by ysj on 2016/11/8.
 */
public class MissionTransactorsEntity  implements Serializable{
    @Expose
    private String Id;//
    @Expose
    private String Name;//

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
