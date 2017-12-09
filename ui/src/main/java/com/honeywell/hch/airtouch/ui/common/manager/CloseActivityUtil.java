package com.honeywell.hch.airtouch.ui.common.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.ui.main.ui.common.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 25/5/16.
 */
public class CloseActivityUtil {
    private static final String TAG = "CloseActivityUtil";
    public static List<Activity> activityList = new ArrayList<Activity>();

    public static List<Activity> beforeLoginActivityList = new ArrayList<>();

    public static List<Activity> enrollActivityList = new ArrayList<>();


    //登陆成功后关闭activity
    public static void exitBeforeLoginClient(Context ctx) {
        // 关闭所有Activity
        for (int i = 0; i < beforeLoginActivityList.size(); i++) {
            if (null != beforeLoginActivityList.get(i)) {
                Activity activity = beforeLoginActivityList.get(i);
                if (!(activity instanceof MainActivity)) {
                    activity.finish();
                    LogUtil.log(LogUtil.LogLevel.INFO, TAG, activity.getLocalClassName());
                }
            }
        }
    }

    //enroll成功后关闭activity
    public static void exitEnrollClient(Context ctx) {
        // 关闭所有Activity
        for (int i = 0; i < enrollActivityList.size(); i++) {
            if (null != enrollActivityList.get(i)) {
                Activity activity = enrollActivityList.get(i);
                if (!(activity instanceof MainActivity)) {
                    activity.finish();
                    LogUtil.log(LogUtil.LogLevel.INFO, TAG, activity.getLocalClassName());
                }
            }
        }
    }

    public static void printEnrollActivity(Context context) {
        for (Activity enrollActivity : enrollActivityList) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "activity name: " + enrollActivity.getClass().getName());
            isExsitMianActivity(context, enrollActivity.getClass());
        }
    }

    /* 判断某一个类是否存在任务栈里面
    * @return
            */
    private static void isExsitMianActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                    LogUtil.log(LogUtil.LogLevel.INFO, TAG, "activity name: " + cls.getClass().getName() + " 已经启动了");
                    flag = true;
                    break;  //跳出循环，优化效率
                }
            }
        }
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "activity name: " + cls.getClass().getName() + " 不存在");
    }
}
