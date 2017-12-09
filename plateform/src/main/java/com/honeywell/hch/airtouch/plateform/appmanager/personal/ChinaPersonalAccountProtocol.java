package com.honeywell.hch.airtouch.plateform.appmanager.personal;


import com.honeywell.hch.airtouch.plateform.appmanager.protocol.LocalizationProtocol;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;

/**
 * Created by wuyuan on 3/11/16.
 */
public class ChinaPersonalAccountProtocol extends PersonalAccountProtocol implements LocalizationProtocol {

    @Override
    public boolean canShowWeatherView() {
//        if (UserAllDataContainer.getInstance().getUserLocationDataList().size() > AppConfig.WEATHER_CHART_EFFECT_NUMBER_THRESHOLD){
//            return false;
//        }
        return true;
    }

    @Override
    public boolean canShowNoDeviceView() {
        return false;
    }

    @Override
    public boolean canShowThreeTitleBtn() {
        return true;
    }

    @Override
    public boolean canShowIndiaLayout() {
        return false;
    }

    @Override
    public boolean currentLocationIsShowGpsSuccess() {
        return true;
    }

    @Override
    public boolean currentLocationIsShowGpsFail() {
        return false;
    }

    @Override
    public boolean currentLocationIsShowIndiaLayout() {
        return false;
    }

    @Override
    public boolean canShowCurrentLocationSideBar() {
        return true;
    }

    @Override
    public boolean canShowDeleteDeviceMessageBox() {
        return false;
    }

    @Override
    public boolean canShowEmotion(UserLocationData userLocationData) {
        return true;
    }

    @Override
    public boolean canShowArriveHome(UserLocationData userLocationData) {
        return true;
    }

    @Override
    public boolean canShowWeatherEffect() {
        if (UserAllDataContainer.shareInstance().getUserLocationDataList().size() > AppConfig.WEATHER_CHART_EFFECT_NUMBER_THRESHOLD) {
            return false;
        }
        return true;
    }

    @Override
    public boolean canUseDefaultBackground() {
        return false;
    }

    @Override
    public boolean canNeedLocatingInGpsFragment() {
        return true;
    }

    @Override
    public boolean canShowFilterPurchase() {
        return true;
    }

    @Override
    public int[] getEnrollDeviceType() {
        return new int[]{HPlusConstants.MAD_AIR_TYPE, HPlusConstants.WATER_SMART_RO_100_TYPE, HPlusConstants.WATER_SMART_RO_75_TYPE,
                HPlusConstants.WATER_SMART_RO_50_TYPE, HPlusConstants.WATER_SMART_RO_400_TYPE, HPlusConstants.AIRTOUCH_X3COMPACT_TYPE,
                HPlusConstants.AIRTOUCH_XCOMPACT_TYPE, HPlusConstants.WATER_SMART_RO_600_TYPE, HPlusConstants.AIRTOUCH_450_TYPE, HPlusConstants.AIRTOUCH_X_TYPE, HPlusConstants.AIRTOUCH_S_TYPE};
    }

    @Override
    public boolean isCanShowTryDemo() {
        return true;
    }

    @Override
    public boolean isTryDemoShowWaterDevice() {
        return true;
    }

    @Override
    public boolean canShowEmotionOutDoorLayout(){
        return true;
    }
}
