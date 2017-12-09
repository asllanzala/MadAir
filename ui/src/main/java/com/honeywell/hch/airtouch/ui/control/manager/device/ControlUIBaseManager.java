package com.honeywell.hch.airtouch.ui.control.manager.device;

import android.content.Context;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.airtouch.model.AirtouchRunStatus;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.plateform.devices.common.Filter;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;
import com.honeywell.hch.airtouch.plateform.devices.water.model.WaterDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.device.AirtouchCapability;
import com.honeywell.hch.airtouch.plateform.http.model.device.AquaTouchRunstatus;
import com.honeywell.hch.airtouch.plateform.http.model.device.SmartROCapability;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.view.DropDownAnimationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * UI login management for Device control and filter page
 * Created by Qian Jin on 4/15/16.
 */
public abstract class ControlUIBaseManager {

    public static final int PRESS_SPEED = 2;
    public static final int PRESS_MODE = 3;
    public static final int FILTER_INITIAL = -1;
    protected IControlManager mControlManager;
    private final String NO_AUTHORIZE_STRING = "ActivationFailed";
    private final int INTERVAL = 1000;
    private long mClickTime = 0;

    public ControlUIBaseManager() {
    }

    public void createFilters(HomeDevice homeDevice, List<Filter> filters) {
        filters.clear();
        for (int i = 0; i < homeDevice.getiFilterFeature().getFilterNumber(); i++) {
            Filter filter = new Filter();
            setupFilterName(homeDevice, i, filter);
            setupFilterImage(homeDevice, i, filter);
            setupFilterDesc(homeDevice, i, filter);
            setupFilterPurchaseUrl(homeDevice, i, filter);
            setupRuntimeLife(homeDevice, i, filter);
            setupFilterDeviceType(homeDevice, filter);
            setmWaterRunTimeLife(filter);
            filters.add(filter);
        }
    }

    public void setupFilterDeviceType(HomeDevice homeDevice, Filter filter) {
        filter.setmDeviceType(homeDevice.getDeviceType());
    }

    public void setupFilterName(HomeDevice homeDevice, int index, Filter filter) {
        filter.setName(homeDevice.getiFilterFeature().getFilterNames()[index]);
    }

    public void setupFilterImage(HomeDevice homeDevice, int index, Filter filter) {
        filter.setmFilterImage(homeDevice.getiFilterFeature().getFilterImages()[index]);
    }

    public void setupFilterDesc(HomeDevice homeDevice, int index, Filter filter) {
        filter.setDescription(homeDevice.getiFilterFeature().getFilterDescriptions()[index]);
    }

    public void setupFilterPurchaseUrl(HomeDevice homeDevice, int index, Filter filter) {
        filter.setPurchaseUrl(homeDevice.getiFilterFeature().getFilterPurchaseUrls()[index]);
    }

    public void setupRuntimeLife(HomeDevice homeDevice, int index, Filter filter) {
        if (homeDevice != null) {
            if (DeviceType.isAirTouchSeries(homeDevice.getDeviceType()) && ((AirTouchDeviceObject) homeDevice).getAirtouchDeviceRunStatus() != null) {
                if (index == 0) {
                    filter.setRuntimeLife(((AirTouchDeviceObject) homeDevice).getAirtouchDeviceRunStatus().getFilter1Runtime());
                } else if (index == 1) {
                    filter.setRuntimeLife(((AirTouchDeviceObject) homeDevice).getAirtouchDeviceRunStatus().getFilter2Runtime());
                } else if (index == 2) {
                    filter.setRuntimeLife(((AirTouchDeviceObject) homeDevice).getAirtouchDeviceRunStatus().getFilter3Runtime());
                }
            }
        }
    }

    public static int parseMode(AirtouchRunStatus runStatus) {
        return runStatus.getScenarioMode();
    }

    public static int parseSpeed(AirtouchRunStatus runStatus) {
        String speedString = runStatus.getFanSpeedStatus();
        if (speedString != null) {
            if (speedString.contains("Speed")) {
                return Integer.parseInt(speedString.substring(6, 7));
            }
        }

        return 0;
    }

    /**
     * Before controlling, check device error status (No auth, offline, remote, power off)
     *
     * @param currentDevice            - current device
     * @param pressWhichButton         - press speed, power or mode button
     * @param dropDownAnimationManager - drag down animation manager
     * @return
     */
    public static boolean isErrorBeforeControlDevice(HomeDevice currentDevice, int pressWhichButton,
                                                     DropDownAnimationManager dropDownAnimationManager, Context context) {

        // no authorize
        if (!AppManager.getLocalProtocol().getRole().canControlDevice(currentDevice.getDeviceInfo())) {
            dropDownAnimationManager.showDragDownLayout(context.getString(R.string.device_no_auth), true);
            return true;
        }

        return false;
    }

    /**
     * After controlling successfully,
     * APP update mode into AirtouchRunStatus data model instead get cloud data due to cloud delay
     *
     * @param runStatus - the runStatus after controlling successfully
     * @param command   - the controlling command
     */
    public static void updateModeToRunStatus(String command, AirtouchRunStatus runStatus) {
        switch (command) {
            case HPlusConstants.MODE_AUTO:
                runStatus.setScenarioMode(HPlusConstants.MODE_AUTO_INT);
                break;
            case HPlusConstants.MODE_SLEEP:
                runStatus.setScenarioMode(HPlusConstants.MODE_SLEEP_INT);
                break;
            case HPlusConstants.MODE_QUICK:
                runStatus.setScenarioMode(HPlusConstants.MODE_QUICK_INT);
                break;
            case HPlusConstants.MODE_SILENT:
                runStatus.setScenarioMode(HPlusConstants.MODE_SILENT_INT);
                break;
            case HPlusConstants.MODE_OFF:
                runStatus.setFanSpeedStatus("");
                runStatus.setScenarioMode(HPlusConstants.MODE_OFF_INT);
                break;
            default:
                runStatus.setFanSpeedStatus(command);
                runStatus.setScenarioMode(HPlusConstants.MODE_DEFAULT_INT);
                break;
        }
    }

    /**
     * After controlling and displaying, store AirtouchRunStatus to UserLocationData model
     *
     * @param currentDevice    - current device
     * @param userLocationData - UserLocationData model need to be updated
     * @param runStatus        - latest runStatus
     */
    public static void updateRunStatusToModel(AirTouchDeviceObject currentDevice, AirtouchRunStatus runStatus,
                                              UserLocationData userLocationData) {
        ArrayList<AirTouchDeviceObject> pm25List = userLocationData.getAirTouchSeriesList();
        for (AirTouchDeviceObject homeDevicePM25Item : pm25List) {
            if (homeDevicePM25Item.getDeviceInfo().getDeviceID() == currentDevice.getDeviceInfo().getDeviceID()) {
                homeDevicePM25Item.setAirtouchDeviceRunStatus(runStatus);
                break;
            }
        }
        currentDevice.setAirtouchDeviceRunStatus(runStatus);
    }


    //init aqua filter
    public void setmWaterRunTimeLife(Filter filter) {
        filter.setmWaterRunTimeLife(FILTER_INITIAL);
    }

    public void setupMaxLife(AirtouchCapability capabilityResponse, List<Filter> mFilters) {
        if (capabilityResponse == null)
            return;
        for (int i = 0; i < mFilters.size(); i++) {
            switch (i) {
                case 0:
                    mFilters.get(0).setMaxLife(capabilityResponse.getFilter1ExpiredTime());
                    break;
                case 1:
                    mFilters.get(1).setMaxLife(capabilityResponse.getFilter2ExpiredTime());
                    break;
                case 2:
                    mFilters.get(2).setMaxLife(capabilityResponse.getFilter3ExpiredTime());
                    break;
            }
        }
    }

    public void setUnAuthFilter(AirtouchRunStatus airtouchRunStatus, List<Filter> mFilters) {
        if (airtouchRunStatus == null) {
            return;
        }
        for (int i = 0; i < mFilters.size(); i++) {
            switch (i) {
                case 0:
                    if (NO_AUTHORIZE_STRING.equals(airtouchRunStatus.getFilter1RfidFlag())) {
                        mFilters.get(i).setUnAuthenticFilter(true);
                    }
                    break;
                case 1:
                    if (NO_AUTHORIZE_STRING.equals(airtouchRunStatus.getFilter2RfidFlag())) {
                        mFilters.get(i).setUnAuthenticFilter(true);
                    }
                    break;
                case 2:
                    if (NO_AUTHORIZE_STRING.equals(airtouchRunStatus.getFilter3RfidFlag())) {
                        mFilters.get(i).setUnAuthenticFilter(true);
                    }
                    break;
            }
        }
    }

    public boolean canClcikable() {
        if (System.currentTimeMillis() - mClickTime > INTERVAL) {
            mClickTime = System.currentTimeMillis();
            return true;
        }
        mClickTime = System.currentTimeMillis();
        return false;
    }

    //update filter runtime from server
    public void setmWaterRunTimeLife(int i, HomeDevice mCurrentDevice, List<Filter> filters, SmartROCapability smartROCapability) {
        if (mCurrentDevice != null) {
            if (DeviceType.isWaterSeries(mCurrentDevice.getDeviceType()) && ((WaterDeviceObject) mCurrentDevice).getAquaTouchRunstatus() != null) {
                float maxTimeSetting = smartROCapability.getmEveryFilterCapabilityDetailList().get(i).getmMaxTimeSetting();
                float maxVolumeSetting = smartROCapability.getmEveryFilterCapabilityDetailList().get(i).getmMaxVolumeSetting();
                float usageVolume = ((WaterDeviceObject) mCurrentDevice).getAquaTouchRunstatus().getmFilterInfoList().get(i).getUsageVolume();
                float usageTime = ((WaterDeviceObject) mCurrentDevice).getAquaTouchRunstatus().getmFilterInfoList().get(i).getUserTime();
                int timePercentage = 0;
                int volumePercentage = 0;
                if (usageTime >= 0 && maxTimeSetting > 0) {
                    timePercentage = Math.round((maxTimeSetting - usageTime) * 100 / maxTimeSetting);
                }
                if (usageVolume >= 0 && maxVolumeSetting > 0) {
                    volumePercentage = Math.round((maxVolumeSetting - usageVolume) * 100 / maxVolumeSetting);
                }
                int percentage = timePercentage <= volumePercentage ? timePercentage : volumePercentage;
                percentage = percentage <= 0 ? 0 : percentage;

                filters.get(i).setmWaterRunTimeLife(percentage);
            }
        }
    }


    public static void updateModeToWaterRunStatus(int command, AquaTouchRunstatus runStatus) {
        runStatus.setScenarioMode(command);
    }


    /**
     * 设置success callBack
     *
     * @param successCallback
     */
    public void setSuccessCallback(IControlManager.SuccessCallback successCallback) {
        mControlManager.setSuccessCallback(successCallback);
    }

    /**
     * 设置error callBack
     *
     * @param errorCallback
     */
    public void setErrorCallback(IControlManager.ErrorCallback errorCallback) {
        mControlManager.setErrorCallback(errorCallback);
    }


    public abstract void controlDevice(int deviceId, String scenarioOrSpeed, String productName);

    public abstract void controlWaterDevice(int deviceId, int scenario, String productName);

    public abstract UserLocationData getLocationWithId(int mLocationId);

    public abstract HomeDevice getHomeDeviceByDeviceId(int deviceId);

    public abstract Map<Integer, AirtouchCapability> getDeviceCapabilityMap();

    public abstract String getControlModePre(int deviceId);


    public abstract boolean getIsFlashing(int deviceId);

    public abstract void getConfigFromServer();

    public abstract Map<Integer, SmartROCapability> getSmartRoCapabilityMap();

    public abstract int getMadAirDeviceImg(MadAirDeviceModel madAirDeviceModel);

    public abstract boolean isDuplicateMadAirAddress(String deviceName);

    public abstract void saveDeviceName(String macAddress, String name);
}
