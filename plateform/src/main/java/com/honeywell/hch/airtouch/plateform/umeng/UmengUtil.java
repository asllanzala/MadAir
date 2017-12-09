package com.honeywell.hch.airtouch.plateform.umeng;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Qian Jin on 9/10/15.
 */
public class UmengUtil {


    public static void onActivityResume(Context context, String tag) {
        MobclickAgent.onPageStart(tag);
        MobclickAgent.onResume(context);
    }

    public static void onActivityPause(Context context, String tag) {
        MobclickAgent.onPageEnd(tag);
        MobclickAgent.onPause(context);
    }

    public static void onFragmentActivityResume(Context context, String tag) {
        MobclickAgent.onResume(context);
    }

    public static void onFragmentActivityPause(Context context, String tag) {
        MobclickAgent.onResume(context);
    }

    public static void onFragmentActivityStart() {
        MobclickAgent.onPageStart("MainScreen"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    public static void onFragmentActivityEnd() {
        MobclickAgent.onPageEnd("MainScreen");
    }

    public static void onKillProcess(Context context) {
        MobclickAgent.onKillProcess(context);
    }

    public static void onActivityCreate(Context context) {
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.startWithConfigure(
                new MobclickAgent.UMAnalyticsConfig(context, getMetaValue(context, "UMENG_APPKEY"), "Umeng",
                        MobclickAgent.EScenarioType.E_UM_NORMAL));
    }

    // get ApiKey
    private static String getMetaValue(Context context, String metaKey) {
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

    public static void onEvent(Context context, String event, String msg) {
        Map<String, String> map = new HashMap<>();
        String macId = "";

        // get current time
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = formatter.format(date);

        // get phone's mac id
        if (DIYInstallationState.getWAPIDeviceResponse() != null) {
            macId = DIYInstallationState.getWAPIDeviceResponse().getMacID();
        }

        map.put("userId", UserInfoSharePreference.getUserId() + "_" + msg);
//        if (macId.equals("")) {
//            map.put("userId", UserAllDataContainer.getInstance().getCurrentUserAccount().getUserID() + "_" + msg + "_" + time);
//        } else {
//            map.put("userId", UserAllDataContainer.getInstance().getCurrentUserAccount().getUserID()
//                    + "_macId_" + macId + "_" + msg + "_" + time);
//        }

        MobclickAgent.onEvent(context, event, map);
    }


    public enum EnrollEventType {
        ENROLL_START,
        ENROLL_SUCCESS,
        ENROLL_FAIL,
        ENROLL_CANCEL,
        UPDATE_WIFI_SUCCESS,
        SMARTLINK_TIMEOUT
    }

    public enum HomeControlType {
        HOME_CONTROL_SUCCESS,
        HOME_CONTROL_FAIL,
    }

    public enum GroupControlType {
        GROUP_CONTROL_SUCCESS,
        GROUP_CONTROL_FAIL
    }

    public enum DeviceControlType {
        DEVICE_CONTROL_SUCCESS,
        DEVICE_CONTROL_FAIL
    }

    /**
     * enroll 相关 event id
     */
    public static final String ENROLL_EVNET_CANCEL_ID = "enroll_cancel";
    public static final String ENROLL_EVNET_START_ID = "enroll_start";
    public static final String ENROLL_EVNET_SUCCESS_ID = "enroll_success";
    public static final String ENROLL_EVNET_FAIL_ID = "enroll_fail";
    public static final String ENROLL_EVNET_SMARTLINK_ID = "enroll_smartlink_timeout";

    /**
     * 控制相关 event id
     */
    public static final String CONTROL_HOME_SUCCESS_EVENT_ID = "control_home_success";
    public static final String CONTROL_HOME_FAIL_EVENT_ID = "control_home_fail";

    public static final String CONTROL_GROUP_SUCCES_EVENT_ID = "control_group_success";
    public static final String CONTROL_GROUP_FAIL_EVENT_ID = "control_group_fail";


    public static final String CONTROL_DEVICE_SUCCES_EVENT_ID = "control_device_success";
    public static final String CONTROL_DEVICE_FAIL_EVENT_ID = "control_device_fail";

    /**
     * 界面访问 event id
     */
    public static final String VISIT_PAGE_EVENT_ID = "visit_page";

    /**
     * 点击事件 event id
     */
    public static final String CLICKABLE_EVENT_ID = "clickable_event";


    /**
     * 扫码时候上报的wifi ssid event id
     */
    public static final String SCAN_SSID_STR_ID = "scan_ssid";


    public static final String INPUT_WIFI_PASSWORD_SSID_STR_ID = "device_enroll_ssid";


    /**
     * enroll key
     */
    public static final String ENROLL_KEY = "enroll_userid_macid_log";
    public static final String ENROLL_USERID_KEY = "enroll_userid";

    /**
     * home control key
     */
    public static final String HOME_CONTROL_KEY = "control_userid_type_locationid_log";
    public static final String CONTROL_USERID_KEY = "control_userid";

    /**
     * group control key
     */
    public static final String GROUP_CONTROL_KEY = "control_userid_type_groupid_log";

    /**
     * device control key
     */
    public static final String DEVICE_CONTROL_KEY = "control_userid_type_deviceProductName_log";

    /**
     * 界面访问 key
     */
    public static final String VISIT_PAGE_KEY = "visit_userid_pageName_data";
    public static final String VISIT_USERID = "visit_userid";
    /**
     * 点击事件 key
     */
    public static final String CLICKABLE_KEY = "click_userid_viewname_date";

    public static final String START_STATUS_STR = "start";
    public static final String CANCEL_STATUS_STR = "cancel";
    public static final String SUCCESS_STATUS_STR = "success";
    public static final String FAIL_STATUS_STR = "fail";
    public static final String SMARTLINK_TIMEOUT_STATUS_STR = "smartlink_timeout";

    public static final String SPERATE_LINE = "_";

    public static void enrollEvent(String productName, EnrollEventType enrollStep, String otherMessage) {
        constructedEnrollEvent(productName, enrollStep, otherMessage);
    }

    public static void homeControlEvent(int locationId, int type, HomeControlType homeControlStep, String otherMessage) {
        constructedHomeControlEvent(locationId, type, homeControlStep, otherMessage);
    }


    public static void groupControlEvent(int groupid, int type, GroupControlType homeControlStep, String otherMessage) {
        constructedGroupControlEvent(groupid, type, homeControlStep, otherMessage);
    }

    public static void deviceControlEvent(String deviceProductName, String type, DeviceControlType deviceControlStep, String otherMessage) {
        constructedDeviceControlEvent(deviceProductName, type, deviceControlStep, otherMessage);
    }

    public static void visitPageEvent(String className) {
        contructedVisitPageEvent(className);
    }


    public static void clickEvent(String viewname) {
        contructedClickEvent(viewname);
    }

    private static void constructedEnrollEvent(String productName, EnrollEventType enrollStep, String otherMessage) {
        String enrollStatusStr = "";
        String eventId = "";
        if (enrollStep == EnrollEventType.ENROLL_START) {
            enrollStatusStr = START_STATUS_STR;
            eventId = ENROLL_EVNET_START_ID;
        } else if (enrollStep == EnrollEventType.ENROLL_SUCCESS) {
            enrollStatusStr = SUCCESS_STATUS_STR;
            eventId = ENROLL_EVNET_SUCCESS_ID;
        } else if (enrollStep == EnrollEventType.ENROLL_FAIL) {
            enrollStatusStr = FAIL_STATUS_STR;
            eventId = ENROLL_EVNET_FAIL_ID;
        } else if (enrollStep == EnrollEventType.ENROLL_CANCEL) {
            enrollStatusStr = CANCEL_STATUS_STR;
            eventId = ENROLL_EVNET_CANCEL_ID;
        } else if (enrollStep == EnrollEventType.UPDATE_WIFI_SUCCESS) {
            enrollStatusStr = SUCCESS_STATUS_STR + "_update_wifi";
            eventId = ENROLL_EVNET_SUCCESS_ID;
        } else if (enrollStep == EnrollEventType.SMARTLINK_TIMEOUT) {
            enrollStatusStr = SMARTLINK_TIMEOUT_STATUS_STR;
            eventId = ENROLL_EVNET_SMARTLINK_ID;
        } else {
            enrollStatusStr = clipErrorMessage(otherMessage);
            eventId = ENROLL_EVNET_FAIL_ID;
        }
        String vaule = getUserId() + SPERATE_LINE + getEnrollDeviceMacId() + SPERATE_LINE
                + productName + SPERATE_LINE + getPlateformInfor() + SPERATE_LINE
                + getCurrentTime() + SPERATE_LINE + enrollStatusStr;

        Map<String, String> map = new HashMap<>();
        map.put(ENROLL_KEY, vaule);
        map.put(ENROLL_USERID_KEY, getUserId());
        MobclickAgent.onEvent(AppManager.getInstance().getApplication(), eventId, map);
    }


    private static void constructedHomeControlEvent(int locationId, int type, HomeControlType homeControlType, String otherMessage) {
        String homeControlStatusStr = "";
        String eventId = "";
        if (homeControlType == HomeControlType.HOME_CONTROL_SUCCESS) {
            homeControlStatusStr = SUCCESS_STATUS_STR;
            eventId = CONTROL_HOME_SUCCESS_EVENT_ID;
        } else {
            homeControlStatusStr = clipErrorMessage(otherMessage);
            eventId = CONTROL_HOME_FAIL_EVENT_ID;
        }
        String vaule = getUserId() + SPERATE_LINE + type + SPERATE_LINE
                + locationId + SPERATE_LINE
                + getCurrentTime() + SPERATE_LINE + homeControlStatusStr;

        Map<String, String> map = new HashMap<>();
        map.put(HOME_CONTROL_KEY, vaule);
        map.put(CONTROL_USERID_KEY, getUserId());
        MobclickAgent.onEvent(AppManager.getInstance().getApplication(), eventId, map);
    }


    private static void constructedGroupControlEvent(int groupid, int type, GroupControlType groupControlType, String otherMessage) {
        String groupControlStatusStr = "";
        String eventId = "";
        if (groupControlType == GroupControlType.GROUP_CONTROL_SUCCESS) {
            groupControlStatusStr = SUCCESS_STATUS_STR;
            eventId = CONTROL_GROUP_SUCCES_EVENT_ID;
        } else {
            groupControlStatusStr = clipErrorMessage(otherMessage);
            eventId = CONTROL_GROUP_FAIL_EVENT_ID;
        }
        String vaule = getUserId() + SPERATE_LINE + type + SPERATE_LINE
                + groupid + SPERATE_LINE
                + getCurrentTime() + SPERATE_LINE + groupControlStatusStr;

        Map<String, String> map = new HashMap<>();
        map.put(GROUP_CONTROL_KEY, vaule);
        map.put(CONTROL_USERID_KEY, getUserId());
        MobclickAgent.onEvent(AppManager.getInstance().getApplication(), eventId, map);
    }

    private static void constructedDeviceControlEvent(String deviceProductName, String type, DeviceControlType deviceControlType, String otherMessage) {
        String deviceControlStatusStr = "";
        String eventId = "";
        if (deviceControlType == DeviceControlType.DEVICE_CONTROL_SUCCESS) {
            deviceControlStatusStr = SUCCESS_STATUS_STR;
            eventId = CONTROL_DEVICE_SUCCES_EVENT_ID;
        } else {
            deviceControlStatusStr = clipErrorMessage(otherMessage);
            eventId = CONTROL_DEVICE_FAIL_EVENT_ID;
        }
        String vaule = getUserId() + SPERATE_LINE + type + SPERATE_LINE
                + deviceProductName + SPERATE_LINE
                + getCurrentTime() + SPERATE_LINE + deviceControlStatusStr;

        Map<String, String> map = new HashMap<>();
        map.put(DEVICE_CONTROL_KEY, vaule);
        map.put(CONTROL_USERID_KEY, getUserId());
        MobclickAgent.onEvent(AppManager.getInstance().getApplication(), eventId, map);
    }

    private static void contructedVisitPageEvent(String activityName) {
        String vaule = getUserId() + SPERATE_LINE + activityName + SPERATE_LINE
                + getCurrentTime();
        Map<String, String> map = new HashMap<>();
        map.put(VISIT_PAGE_KEY, vaule);
        map.put(VISIT_USERID, getUserId());
        MobclickAgent.onEvent(AppManager.getInstance().getApplication(), VISIT_PAGE_EVENT_ID, map);
    }

    private static void contructedClickEvent(String viewName) {
        String vaule = getUserId() + SPERATE_LINE + viewName + SPERATE_LINE
                + getCurrentTime();
        Map<String, String> map = new HashMap<>();
        map.put(VISIT_PAGE_KEY, vaule);
        map.put(VISIT_USERID, getUserId());
        MobclickAgent.onEvent(AppManager.getInstance().getApplication(), VISIT_PAGE_EVENT_ID, map);
    }


    private static String getUserId() {
        return UserInfoSharePreference.getUserId();
    }

    /**
     * 获取机型和build号
     *
     * @return
     */
    private static String getPlateformInfor() {
        return android.os.Build.MODEL + SPERATE_LINE + android.os.Build.VERSION.RELEASE;
    }


    private static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    private static String getEnrollDeviceMacId() {
        String macId = "";
        if (DIYInstallationState.getWAPIDeviceResponse() != null) {
            macId = DIYInstallationState.getWAPIDeviceResponse().getMacID();
        }
        return macId;
    }

    private static String clipErrorMessage(String mesgLog) {
        if (!StringUtil.isEmpty(mesgLog)) {
            if (mesgLog.length() > 100) {
                return mesgLog.substring(0, 100);
            }
        }
        return mesgLog;
    }

}
