package com.vouov.ailk.app.common;

import android.app.Application;
import com.vouov.ailk.app.api.AppApiClient;
import com.vouov.ailk.app.model.Employee;

/**
 * User: yuml
 * Date: 13-1-17
 * Time: 下午7:49
 */
public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppApiClient.init(this);
    }

    private String userName;
    private String password;
    private Employee userInfo;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Employee getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Employee userInfo) {
        this.userInfo = userInfo;
    }
}
