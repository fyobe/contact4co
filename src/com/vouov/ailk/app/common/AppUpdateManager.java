package com.vouov.ailk.app.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;
import com.vouov.ailk.app.api.AppApiClient;
import com.vouov.ailk.app.model.AppInfo;
import com.vouov.ailk.app.task.ApkDownloadTask;
import com.vouov.ailk.app.util.FileUtils;

import java.io.File;

/**
 * User: yuml
 * Date: 13-1-24
 * Time: 下午8:37
 */
public class AppUpdateManager {
    private static AppUpdateManager instance = new AppUpdateManager();
    private Context context;
    public final static String UPDATE_URL = "http://yuminglong.github.com/blog/update/update_info.json";
    private final static String LOCAL_APK_URL = "/vouov/AilkContact.apk";

    public static AppUpdateManager getInstance(Context context) {
        instance.context = context;
        return instance;
    }

    private AppUpdateManager() {
    }

    public AppInfo getVersion() {
        AppInfo appInfo = null;
        PackageManager packageManager = this.context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            appInfo = new AppInfo();
            packInfo = packageManager.getPackageInfo(this.context.getPackageName(), 0);
            appInfo.setVersionCode(packInfo.versionCode);
            appInfo.setVersionName(packInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appInfo;
    }

    public void update(boolean isShowMsg){
        AppInfo currentAppInfo = getVersion();
        AppInfo updateAppInfo = AppApiClient.updateAppInfo();
        if (updateAppInfo.getVersionCode() > currentAppInfo.getVersionCode()) {
            File downloadApk = download(updateAppInfo);
            install(downloadApk);
        }else {
            Toast.makeText(this.context, "此应用已经是最新版本", Toast.LENGTH_LONG).show();
        }
    }

    public File download(AppInfo appInfo) {
        File saveFile = null;
        if (appInfo != null) {
            String url = appInfo.getDownloadUrl();
            saveFile = FileUtils.getSDFile(LOCAL_APK_URL);
            new ApkDownloadTask(this.context, url, saveFile).execute();
        }
        return saveFile;
    }

    public void install(File apkFile) {
        if (apkFile == null || !apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
