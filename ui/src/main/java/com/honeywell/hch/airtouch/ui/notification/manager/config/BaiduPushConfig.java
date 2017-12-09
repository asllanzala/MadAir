package com.honeywell.hch.airtouch.ui.notification.manager.config;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.http.model.notification.PushMessageModel;
import com.honeywell.hch.airtouch.ui.main.ui.common.MainActivity;
import com.honeywell.hch.airtouch.ui.main.ui.messagecenter.MessageHandleActivity;

import java.util.List;

/**
 * Created by Vincent on 23/10/15.
 */
public class BaiduPushConfig {

    //test environment
    public static String DEBUG_NOTIFICATION_HUBNAME = "baidunh";
    public static String DEBUG_NOTIFICATION_HUBCONNECTING_STRING = "Endpoint=sb://baidunotification-ns.servicebus.chinacloudapi.cn/;SharedAccessKeyName=DefaultListenSharedAccessSignature;SharedAccessKey=TKao5xPc7HoUZwci7iZTWQpdNt67fRq97drmCL33GpQ=";
    //product environment
    public static String RELEASE_NOTIFICATIONHUBNAME = "tccnotificationpro";
    public static String RELEASE__NOTIFICATION_HUBCONNECTING_STRING = "Endpoint=sb://tccnotification-ns.servicebus.chinacloudapi.cn/;SharedAccessKeyName=DefaultListenSharedAccessSignature;SharedAccessKey=wiHEzF8JtKUqqaECqE7pk+ZQ8SDjJ3ZKN7+98ZNPQf4=";
    private static BaiduPushConfig instance;

    private static final int TOUCHTYPE = 100;
    private static final int PREMIUMTYPE = 101;
    private static final int SENSOREOR = 999001;
    public static final int REMOTEENABLE = 999002;
    public static final int REMOTEDISABLE = 999003;

    public static final int GRANTAUTHDEVICE = 999004;
    public static final int GRANTAUTHGROUP = 999005;
    public static final int REMOVEAUTHDEVICE = 999006;
    public static final int REMOVEAUTHGROUP = 999007;

    public static final int PUMPOVERTIMEERR = 999008;
    public static final int PUMPFREQBOOTUPERR = 999009;
    public static final int PUMPFAULT = 999010;
    public static final int PIPEBLOCKERR = 999011;
    public static final int INFTDSFAULT = 999012;
    public static final int OUTFTDSFAULT = 999013;
    public static final int WATERLEAKAGEERR = 999014;
    public static final int EEPROMERR = 999015;
    public static final int NO_WATER = 999016;
    public static final int REGULAR_NOTICE = 999017;

//    private static final int TVOCSENSOR = 999004;


    public static final String BAIDUPUSHAPIKEY = "com.baidu.push.API_KEY";


    private BaiduPushConfig() {
    }

    public static BaiduPushConfig getInstance() {
        if (instance == null) {
            instance = new BaiduPushConfig();
        }
        return instance;
    }

    public boolean isAppOnForeground(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return false;
        }
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(ctx.getPackageName())
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return false;
            }
        }
        return true;
    }

    public static Class startEntrance(PushMessageModel pushMessageModel) {
        switch (pushMessageModel.getmMessageType()) {
            case GRANTAUTHDEVICE:
            case GRANTAUTHGROUP:
            case REMOVEAUTHDEVICE:
            case REMOVEAUTHGROUP:

            case PUMPOVERTIMEERR:
            case PUMPFREQBOOTUPERR:
            case PUMPFAULT:
            case PIPEBLOCKERR:
            case INFTDSFAULT:
            case OUTFTDSFAULT:
            case WATERLEAKAGEERR:
            case EEPROMERR:
            case NO_WATER:

            case REMOTEENABLE:
            case REMOTEDISABLE:
                return MessageHandleActivity.class;
            case REGULAR_NOTICE:
                return MainActivity.class;
            default:
                return null;
        }
    }


    // get ApiKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return apiKey;
    }

    public static String getNotificationHubName() {
        if (AppConfig.isDebugMode) {
            return DEBUG_NOTIFICATION_HUBNAME;
        }
        return RELEASE_NOTIFICATIONHUBNAME;
    }


    public static String getNotificationHubConnectionString() {
        if (AppConfig.isDebugMode) {
            return DEBUG_NOTIFICATION_HUBCONNECTING_STRING;
        }
        return RELEASE__NOTIFICATION_HUBCONNECTING_STRING;
    }

}
