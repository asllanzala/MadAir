package com.honeywell.hch.airtouch.plateform.config;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.honeywell.hch.airtouch.library.util.DateTimeUtil;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserCacheDataManager;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;
import com.honeywell.hch.airtouch.plateform.timereceive.CheckUpdateReceiver;
import com.honeywell.hch.airtouch.plateform.timereceive.MorningAlarmReceiver;
import com.honeywell.hch.airtouch.plateform.timereceive.NightAlarmReceiver;

import java.util.Calendar;
import java.util.List;

/**
 * Created by wuyuan on 3/10/16.
 */
public class LibraryInitUtil {

    private static LibraryInitUtil mLibraryInitUitl;

    private Application mHPlusApplication = null;
    private AppConfig mAppConfig;

    private static final int INTERVAL = 1000 * 60 * 60 * 24;

    public static LibraryInitUtil getInstance(){
        if (mLibraryInitUitl == null){
            mLibraryInitUitl = new LibraryInitUtil();
        }
        return mLibraryInitUitl;
    }

    public void initApplication(Application application){
        mHPlusApplication = application;
    }


    public void init(Application application) {

        mHPlusApplication = application;
        mAppConfig = AppConfig.shareInstance();

        initLogSettings();
        initAppConfig();
        initAlarm();

        if (UserInfoSharePreference.isUserAccountHasData()) {
            UserCacheDataManager.getInstance().setLocationDataFromCache();
            UserCacheDataManager.getInstance().setRunstatusDataFromCache();
            UserCacheDataManager.getInstance().setDeviceConfigFromCache();
            UserCacheDataManager.getInstance().setMessageDataFromCache();
            UserAllDataContainer.shareInstance().updateMadAirData();
            List<UserLocationData> userLocationDataList = UserAllDataContainer.shareInstance().getUserLocationDataList();
            if (userLocationDataList != null && userLocationDataList.size() > 0) {
                for (UserLocationData userLocationData : userLocationDataList) {
                    UserCacheDataManager.getInstance().setGroupDataFromCache(userLocationData.getLocationID());
                }
            }
        }
    }

    /**
     * initial mHPlusApplication log setting
     */
    private void initLogSettings() {
        LogUtil.setIsLogEnabled(true);
        LogUtil.setLogFileName(DateTimeUtil.getNowDateTimeString(DateTimeUtil.LOG_TIME_FORMAT) + ".log");
    }

    /**
     * initial app information
     */
    private void initAppConfig() {
        mAppConfig.loadAppInfo();
    }



    private void initAlarm() {
        AlarmManager alarmManager = (AlarmManager) mHPlusApplication.getSystemService(Context.ALARM_SERVICE);

        // Morning alarm
        Intent intent1 = new Intent(mHPlusApplication, MorningAlarmReceiver.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(mHPlusApplication, 0, intent1,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar1 = Calendar.getInstance();
//        calendar1.setTimeInMillis(System.currentTimeMillis());
        calendar1.set(Calendar.HOUR_OF_DAY, 6);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), INTERVAL,
                pendingIntent1);

        // Night alarm
        Intent intent2 = new Intent(mHPlusApplication, NightAlarmReceiver.class);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(mHPlusApplication,
                0, intent2, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(System.currentTimeMillis());
        calendar2.set(Calendar.HOUR_OF_DAY, 18);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(),
                INTERVAL, pendingIntent2);



        Intent intent3 = new Intent(mHPlusApplication, CheckUpdateReceiver.class);
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(mHPlusApplication,
                0, intent3, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + INTERVAL,
                INTERVAL, pendingIntent3);
    }


}
