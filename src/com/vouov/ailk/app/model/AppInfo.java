package com.vouov.ailk.app.model;

/**
 * User: yuml
 * Date: 13-1-24
 * Time: 下午9:01
 */
public class AppInfo {
    private String versionName;
    private long versionCode;
    private String appId;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public long getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(long versionCode) {
        this.versionCode = versionCode;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
