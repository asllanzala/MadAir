package com.honeywell.hch.airtouch.plateform.appmanager.personal;


import com.honeywell.hch.airtouch.plateform.appmanager.protocol.LocalizationProtocol;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;

/**
 * Created by wuyuan on 3/11/16.
 */
public class IndiaPersonalAccountProtocol extends PersonalAccountProtocol implements LocalizationProtocol {

    @Override
    public boolean canShowWeatherView() {
        return false;
    }

    @Override
    public boolean canShowNoDeviceView() {
        return true;
    }

    @Override
    public boolean canShowThreeTitleBtn() {
        return false;
    }

    @Override
    public boolean canShowIndiaLayout() {
        return true;
    }

    @Override
    public boolean currentLocationIsShowGpsSuccess() {
        return false;
    }

    @Override
    public boolean currentLocationIsShowGpsFail() {
        return false;
    }

    @Override
    public boolean currentLocationIsShowIndiaLayout() {
        return true;
    }

    @Override
    public boolean canShowCurrentLocationSideBar() {
        return false;
    }

    @Override
    public boolean canShowDeleteDeviceMessageBox() {
        return true;
    }

    @Override
    public boolean canShowEmotion(UserLocationData userLocationData) {
        return false;
    }

    @Override
    public boolean canShowArriveHome(UserLocationData userLocationData) {
        return false;
    }

    @Override
    public boolean canShowWeatherEffect() {
        return false;
    }

    @Override
    public boolean canUseDefaultBackground() {
        return true;
    }

    @Override
    public boolean canNeedLocatingInGpsFragment() {
        return false;
    }

    @Override
    public boolean canShowFilterPurchase() {
        return false;
    }

    @Override
    public int[] getEnrollDeviceType() {
        return new int[]{HPlusConstants.AIRTOUCH_450_TYPE, HPlusConstants.AIRTOUCH_X_TYPE, HPlusConstants.AIRTOUCH_S_TYPE};
    }

    @Override
    public boolean isCanShowTryDemo() {
        return true;
    }

    @Override
    public boolean isTryDemoShowWaterDevice() {
        return false;
    }

    @Override
    public boolean canShowSelectCity() {
        return false;
    }

    @Override
    public boolean canShowEmotionOutDoorLayout(){
        return false;
    }
}
