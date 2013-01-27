package com.vouov.ailk.app.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.vouov.ailk.app.model.AppInfo;

import java.io.File;

/**
 * User: yuml
 * Date: 13-1-24
 * Time: 下午8:37
 */
public class AppUpdateManager {
    private static AppUpdateManager instance = new AppUpdateManager();

    public static AppUpdateManager getInstance() {
        return instance;
    }

    private AppUpdateManager() {
    }

    public AppInfo getVersion() {
        return null;
    }

    public void update(Context context, boolean isShowMsg) {

    }

    public void download(Context context) {

    }

    public void install(Context context, File apkFile) {
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
