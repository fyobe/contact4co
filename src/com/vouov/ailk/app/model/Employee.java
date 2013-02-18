package com.vouov.ailk.app.model;

import com.vouov.ailk.app.db.Column;
import com.vouov.ailk.app.db.EmployeeInfoDatabase;

import java.io.Serializable;
import java.util.Date;

/**
 * User: yuml
 * Date: 13-1-17
 * Time: 下午8:40
 */
public class Employee implements Serializable {
    @Column(EmployeeInfoDatabase.Employee._ID)
    private long _id;
    @Column(EmployeeInfoDatabase.Employee.NAME)
    private String name;
    @Column(EmployeeInfoDatabase.Employee.ID)
    private String id;
    @Column(EmployeeInfoDatabase.Employee.ACCOUNT)
    private String account;
    @Column(EmployeeInfoDatabase.Employee.DEPT_NAME)
    private String deptName;
    @Column(EmployeeInfoDatabase.Employee.MOBILE)
    private String mobile;
    @Column(EmployeeInfoDatabase.Employee.OFFICE_PHONE)
    private String officePhone;
    @Column(EmployeeInfoDatabase.Employee.HOME_CITY)
    private String homeCity;
    @Column(EmployeeInfoDatabase.Employee.EMAIL)
    private String email;
    @Column(EmployeeInfoDatabase.Employee.PARENT_EMPLOYEE_ID)
    private String parentEmployeeId;
    @Column(EmployeeInfoDatabase.Employee.PARENT_EMPLOYEE_NAME)
    private String parentEmployeeName;
    @Column(EmployeeInfoDatabase.Employee.DESCRIPTION)
    private String desc;
    @Column(EmployeeInfoDatabase.Employee.UPDATE_TIME)
    private Date updateTime;
    @Column(EmployeeInfoDatabase.Employee.IS_FAVORITE)
    private boolean favorite;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
