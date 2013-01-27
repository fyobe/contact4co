package com.vouov.ailk.app.config;

import android.app.Application;
import com.vouov.ailk.app.api.AppApiClient;

/**
 * User: yuml
 * Date: 13-1-17
 * Time: 下午7:49
 */
public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        setUserName("ailk\\yuml");
        setPassword("Long1234");
        AppApiClient.init(this);
    }

    private String userName;
    private String password;

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


}
