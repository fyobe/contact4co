package com.vouov.ailk.app.task;

import android.content.Context;
import com.vouov.ailk.app.common.AppUpdateManager;

import java.io.File;

/**
 * User: yuml
 * Date: 13-1-26
 * Time: 下午1:53
 */
public class ApkDownloadTask extends FileDownloadTask {
    public ApkDownloadTask(Context context, String url, File saveFile) {
        super(context, url, saveFile, "应用更新下载中");
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if (!this.isCancelled() && mSaveFile.exists()) AppUpdateManager.getInstance().install(mContext, mSaveFile);
    }
}
