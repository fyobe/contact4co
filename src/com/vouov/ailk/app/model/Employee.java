package com.vouov.ailk.app.model;

import java.io.Serializable;

/**
 * User: yuml
 * Date: 13-1-17
 * Time: 下午8:40
 */
public class Employee implements Serializable {
    private String name;
    private String id;
    private String account;
    private String deptName;
    private String mobile;
    private String officePhone;
    private String homeCity;
    private String email;
    private String parentEmployeeId;
    private String parentEmployeeName;
    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getHomeCity() {
        return homeCity;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParentEmployeeId() {
        return parentEmployeeId;
    }

    public void setParentEmployeeId(String parentEmployeeId) {
        this.parentEmployeeId = parentEmployeeId;
    }

    public String getParentEmployeeName() {
        return parentEmployeeName;
    }

    public void setParentEmployeeName(String parentEmployeeName) {
        this.parentEmployeeName = parentEmployeeName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
