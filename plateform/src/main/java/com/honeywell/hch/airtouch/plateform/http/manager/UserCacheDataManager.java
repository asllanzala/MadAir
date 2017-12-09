package com.honeywell.hch.airtouch.plateform.http.manager;

import com.honeywell.hch.airtouch.library.util.DateTimeUtil;
import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

import org.json.JSONException;

/**
 * Created by h127856 on 16/9/18.
 */
public class UserCacheDataManager {

    private static UserCacheDataManager mUserCacheDataManager;

    public static UserCacheDataManager getInstance() {
        if (mUserCacheDataManager == null) {
            mUserCacheDataManager = new UserCacheDataManager();
        }
        return mUserCacheDataManager;
    }

    public void setLocationDataFromCache() {
        String locationData = UserInfoSharePreference.getLocationCachesData();
        try {
            LocationAndDeviceParseManager.getInstance().parseJsonDataToUserLocationObject(locationData);
            UserAllDataContainer.shareInstance().setDashboardLoadingCacheSuccess();
        } catch (JSONException e) {

        }
    }

    public void setRunstatusDataFromCache() {
        String runstatusData = UserInfoSharePreference.getDeviceRunstatusCachesData();
        try {
            LocationAndDeviceParseManager.getInstance().parseJsonDataToRunstatusObject(runstatusData);
        } catch (JSONException e) {

        }
    }

    public void setDeviceConfigFromCache() {
        String runstatusData = UserInfoSharePreference.getDeviceConfigCacheData();
        try {
            LocationAndDeviceParseManager.getInstance().parseJsonToDeviceConfig(runstatusData);
        } catch (JSONException e) {

        }
    }

    public void setMessageDataFromCache() {
        String messageData = UserInfoSharePreference.getMessageCachesData();
        try {
            LocationAndDeviceParseManager.getInstance().parseJsonDataToMessageSObject(messageData);
        } catch (JSONException e) {

        }
    }

    public void setGroupDataFromCache(int locationId) {
        String groupData = UserInfoSharePreference.getGroupCachesData(locationId);
        LocationAndDeviceParseManager.getInstance().parseJsonDataToGroupData(locationId, groupData, true);
    }


    public long getLastUpdateTimeOfCacheData() {
        long lastUpdate = UserInfoSharePreference.getDashBoardLastUpatetimeData();
        return lastUpdate;
    }

    public String getLastUpdateTimeOfCacheDataStr() {
        long lastUpdate = UserInfoSharePreference.getDashBoardLastUpatetimeData();
        return getLastUpdateTimeStr(lastUpdate);

    }

    public String getLastMessageTimeOfCacheDataStr() {
        long lastUpdate = UserInfoSharePreference.getMessageLastUpatetimeData();
        return getLastUpdateTimeStr(lastUpdate);

    }

    public long getGroupDataCacheDataUpdateTime(int locationId) {
        long lastUpdate = UserInfoSharePreference.getGroupUpdateTimeData(locationId);
        return lastUpdate;
    }

    public String getLastUpdateTimeStr(long time) {
        long lastMilSeconds = DateTimeUtil.compareNowTime(time);
        if (lastMilSeconds != DateTimeUtil.DEFAULT_TIME) {
            long second = lastMilSeconds / 1000;
            if (second <= 20) {
                return AppManager.getInstance().getApplication().getString(R.string.cache_loading_msg_justnow);
            } else if (second < 60) {
                return AppManager.getInstance().getApplication().getString(R.string.cache_loading_msg_seconds, second);
            } else if (second >= 60 && second < 60 * 60) {
                return AppManager.getInstance().getApplication().getResources().getQuantityString(R.plurals.update_minute, (int) second / 60, (int) second / 60);
            } else if (second >= 60 * 60 && second < 24 * 60 * 60l) {
                return AppManager.getInstance().getApplication().getResources().getQuantityString(R.plurals.update_hours, (int) (second / (60 * 60)), (int) (second / (60 * 60)));
            } else {
                return AppManager.getInstance().getApplication().getResources().getQuantityString(R.plurals.update_hours, (int) (second / (60 * 60 * 24)), (int) (second / (60 * 60 * 24)));
            }
        }
        return "";

    }

}
