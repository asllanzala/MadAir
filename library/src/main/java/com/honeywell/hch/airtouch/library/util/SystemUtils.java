package com.honeywell.hch.airtouch.library.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;

/**
 * Created by Vincent on 29/3/16.
 */
public class SystemUtils {
    private final static String TAG = "SystemUtils";

    /**
     * 判断应用是否已经启动
     *
     * @param context     一个context
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAlive(Context context) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "processInfos size: " + processInfos.size());

        List<ActivityManager.RunningTaskInfo> list = activityManager.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(context.getPackageName()) &&
                    info.baseActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /*
    判断app是否在前台
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> processInfos = activityManager.getRunningTasks(1);
        if (!processInfos.isEmpty()) {
            ComponentName topActivity = processInfos.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return false;
            }
        }
        return true;
    }
}
