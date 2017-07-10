package com.zbxn.main.entity;


/**
 * Created by Administrator on 2016/12/29.
 */
public class InviteEmplEntity {
    String employeeName;
    String employeePhone;

    public String getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeePhone(String employeePhone) {
        this.employeePhone = employeePhone;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    @Override
    public String toString() {
        return "InviteEmplEntity{" +
                "employeeName='" + employeeName + '\'' +
                ", employeePhone='" + employeePhone + '\'' +
                '}';
    }
}
