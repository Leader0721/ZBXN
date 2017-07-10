package com.zbxn.main.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by U on 2017/1/10.
 */

public class UserEntity {

    @Expose
    private int ID;
    @Expose
    private String Username;
    @Expose
    private String Photo;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

}
