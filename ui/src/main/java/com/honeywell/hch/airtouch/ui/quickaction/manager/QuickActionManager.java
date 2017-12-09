package com.honeywell.hch.airtouch.ui.quickaction.manager;

import com.honeywell.hch.airtouch.plateform.devices.DeviceConstant;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.water.model.WaterDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.manager.model.SelectStatusDeviceItem;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.ui.common.manager.model.Category;
import com.honeywell.hch.airtouch.ui.common.ui.DashBoadConstant;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.DeviceInfoBaseUIManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 9/8/16.
 */
public class QuickActionManager {

    public static final String QUICKTYPE = "quick_type";
    public static final String LOCATIONIDPARAMETER = "location_Id";

    public enum QuickActionType {
        PM25,
        TVOC,
        WATERQUALITY
    }

    public List<HomeDevice> getQuickHomeDevice(UserLocationData mUserLocation, Object mQuickType) {
        if (mQuickType == QuickActionType.PM25) {
            return getPm25HomeDevice(mUserLocation);
        } else if (mQuickType == QuickActionType.TVOC) {
            return getTVOCHomeDevice(mUserLocation);
        } else if (mQuickType == QuickActionType.WATERQUALITY) {
            return getWaterHomeDevice(mUserLocation);
        }
        return new ArrayList<HomeDevice>();
    }

    public List<HomeDevice> getPm25HomeDevice(UserLocationData mUserLocation) {
        List<HomeDevice> homeDeviceTempleList = new ArrayList<>();
        for (HomeDevice homeDevice : mUserLocation.getAirTouchSeriesList()) {
            int mPmValue = ((AirTouchDeviceObject) homeDevice).getAirtouchDeviceRunStatus().getmPM25Value();
            if (mPmValue >= HPlusConstants.PM25_MEDIUM_LIMIT && mPmValue < HPlusConstants.ERROR_SENSOR && !homeDevice.getiDeviceStatusFeature().isOffline()) {
                homeDeviceTempleList.add(homeDevice);
            }
        }
        return homeDeviceTempleList;
    }

    public List<HomeDevice> getTVOCHomeDevice(UserLocationData mUserLocation) {
        List<HomeDevice> homeDeviceTempleList = new ArrayList<>();
        for (HomeDevice homeDevice : mUserLocation.getAirTouchSeriesList()) {
            if (((AirTouchDeviceObject)homeDevice).canShowTvoc()) {
                int mTVOCValue = ((AirTouchDeviceObject) homeDevice).getTvocFeature().getTvocLevel();
                if (mTVOCValue >= DashBoadConstant.TVOC_MID_LEVEL && mTVOCValue < HPlusConstants.ERROR_SENSOR && !homeDevice.getiDeviceStatusFeature().isOffline()) {
                    homeDeviceTempleList.add(homeDevice);
                }
            }
        }
        return homeDeviceTempleList;
    }

    public List<HomeDevice> getWaterHomeDevice(UserLocationData mUserLocation) {
        List<HomeDevice> homeDeviceTempleList = new ArrayList<>();
        for (HomeDevice homeDevice : mUserLocation.getWaterDeviceList()) {
            int worseQuality = ((WaterDeviceObject) homeDevice).getAquaTouchRunstatus().getWaterQualityLevel();
            if (worseQuality >= DeviceConstant.WATER_QUALITY_MID && worseQuality < DeviceConstant.SENSOR_ERROR && !homeDevice.getiDeviceStatusFeature().isOffline()) {
                homeDeviceTempleList.add(homeDevice);
            }
        }
        return homeDeviceTempleList;
    }

    public HomeDevice getWorstDevice(UserLocationData mUserLocation, Object mQuickType) {
        if (mQuickType == QuickActionType.PM25) {
            return mUserLocation.getDefaultPMDevice();
        } else if (mQuickType == QuickActionType.TVOC) {
            return mUserLocation.getDefaultTVOCDevice();
        } else if (mQuickType == QuickActionType.WATERQUALITY) {
            return mUserLocation.getDefaultWaterDevice();
        }
        return null;
    }

    public String getPm25Status(AirTouchDeviceObject device) {
        if (device.getAirtouchDeviceRunStatus() == null) {
            return HPlusConstants.DATA_LOADING_STATUS;
        }
        int pm25Value = device.getAirtouchDeviceRunStatus().getmPM25Value();
        if (pm25Value < HPlusConstants.PM25_MEDIUM_LIMIT || pm25Value == HPlusConstants.ERROR_MAX_VALUE) {
            return AppManager.getInstance().getApplication().getText(R.string.tvoc450_good).toString();
        } else if (pm25Value < HPlusConstants.PM25_HIGH_LIMIT) {
            return AppManager.getInstance().getApplication().getText(R.string.tvoc450_average).toString();
        } else if (pm25Value == HPlusConstants.ERROR_SENSOR) {
            return HPlusConstants.DATA_LOADING_FAILED_STATUS;
        } else {
            return AppManager.getInstance().getApplication().getText(R.string.tvoc450_poor).toString();
        }
    }

    public String getTVOCStatus(AirTouchDeviceObject device) {
        if (device.canShowTvoc()){
            switch (device.getTvocFeature().getTvocLevel()) {
                case DashBoadConstant.TVOC_GOOD_LEVEL:
                    return AppManager.getInstance().getApplication().getText(R.string.tvoc450_good).toString();
                case DashBoadConstant.TVOC_MID_LEVEL:
                    return AppManager.getInstance().getApplication().getText(R.string.tvoc450_average).toString();
                case DashBoadConstant.TVOC_BAD_LEVEL:
                    return AppManager.getInstance().getApplication().getText(R.string.tvoc450_poor).toString();
                case DashBoadConstant.TVOC_ERROR_LEVEL:
                    return HPlusConstants.DATA_LOADING_FAILED_STATUS;
            }
        }

        return HPlusConstants.DATA_LOADING_FAILED_STATUS;
    }

    /*
    set adapter data
     */
    public ArrayList<Category> getCategoryData(List<HomeDevice> mQuickHomeDeviceList) {
        ArrayList<Category> listData = new ArrayList<Category>();
        ArrayList<SelectStatusDeviceItem> selectStatusDeviceItemArrayList = new ArrayList<>();
        for (HomeDevice homeDevice : mQuickHomeDeviceList) {
            SelectStatusDeviceItem selectStatusDeviceItem = new SelectStatusDeviceItem();
            selectStatusDeviceItem.setDeviceItem(homeDevice);
            selectStatusDeviceItemArrayList.add(selectStatusDeviceItem);
        }
        if (selectStatusDeviceItemArrayList.size() != 0) {
            Category category = new Category(AppManager.getInstance().getApplication().getString(R.string.quick_action_device_list_title));
            category.addSelectItem(selectStatusDeviceItemArrayList);
            category.setType(DeviceInfoBaseUIManager.DEVICE_TYPE);
            listData.add(category);
        }

        return listData;
    }

}
