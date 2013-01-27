package com.vouov.ailk.app.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.util.Stack;

/**
 * User: yuml
 * Date: 13-1-24
 * Time: 下午12:24
 */
public class AppManager {
    private static final String TAG = "ailk_app_manager";
    private static AppManager instance = new AppManager();
    private Stack<Activity> activityStack;

    public static AppManager getInstance() {
        return instance;
    }

    private AppManager() {
    }

    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.push(activity);
    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            Log.d(TAG, "finish activity:"+activity.getLocalClassName());
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack != null) {
            while (!activityStack.isEmpty()) {
                finishActivity(activityStack.remove(0));
            }
        }
    }

    public void exitApp(Context context) {
        try {
            Log.d(TAG, "exit app start");
            finishAllActivity();
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO) {
                Log.d(TAG, "quit call restart");
                am.restartPackage(context.getPackageName());
            } else {
                Log.d(TAG, "quit call killBackgroundProcesses ");
                am.killBackgroundProcesses(context.getPackageName());
            }
            System.exit(0);
        } catch (Exception e) {
            Log.e(TAG, "exit App Exception", e);
        }
    }
}
