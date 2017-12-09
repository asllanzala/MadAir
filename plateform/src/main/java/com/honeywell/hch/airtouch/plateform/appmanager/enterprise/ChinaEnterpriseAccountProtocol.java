package com.honeywell.hch.airtouch.plateform.appmanager.enterprise;


import com.honeywell.hch.airtouch.plateform.appmanager.protocol.LocalizationProtocol;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;

/**
 * Created by wuyuan on 3/11/16.
 */
public class ChinaEnterpriseAccountProtocol extends EnterpriseAccountProtocol implements LocalizationProtocol {

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
    public boolean canShowIndiaLayout(){
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
    public boolean canShowDeleteDeviceMessageBox() {
        return false;
    }

    @Override
    public boolean canShowCurrentLocationSideBar() {
        return false;
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
        return true;
    }

    @Override
    public boolean isCanShowTryDemo() {
        return false;
    }

    @Override
    public boolean isTryDemoShowWaterDevice() {
        return false;
    }

    @Override
    public boolean canShowEmotionOutDoorLayout() {
        return true;
    }
}
