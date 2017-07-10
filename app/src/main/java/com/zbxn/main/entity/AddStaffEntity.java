package com.zbxn.main.entity;

import com.google.gson.annotations.Expose;

/**
 * 添加员工
 * Created by U on 2016/12/27.
 * //{"tokenid":"748B3FC71C3416B3F654110A1BADE4FB20170110091426","CurrentCompanyId":1,"UserID":"1571","UserName":"1031",
 * "UserDeptPosition":"[{\"UDPID\":\"\",\"departmentId\":\"\"},{\"UDPID\":\"\",\"departmentId\":\"531\",\"positionId\":\"8\"}]"}}
 */
public class AddStaffEntity {
    @Expose
    int UserID;
    @Expose
    String UserName;
    @Expose
    String PassWord;
    @Expose
    String Gender;
    @Expose
    String DepartmentPosition;

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDepartmentPosition() {
        return DepartmentPosition;
    }

    public void setDepartmentPosition(String departmentPosition) {
        DepartmentPosition = departmentPosition;
    }
}
