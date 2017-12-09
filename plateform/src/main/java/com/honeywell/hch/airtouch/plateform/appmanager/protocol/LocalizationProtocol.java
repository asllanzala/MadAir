package com.honeywell.hch.airtouch.plateform.appmanager.protocol;


import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;

/**
 * Created by wuyuan on 3/10/16.
 */
public interface LocalizationProtocol extends DistributionProtocol {

    boolean canShowWeatherView();

    boolean canShowNoDeviceView();

    boolean canShowThreeTitleBtn();

    boolean canShowIndiaLayout();

    boolean currentLocationIsShowGpsSuccess();
    boolean currentLocationIsShowGpsFail();
    boolean currentLocationIsShowIndiaLayout();

    boolean canShowCurrentLocationSideBar();

    boolean canShowEmotion(UserLocationData userLocationData);

    boolean canShowArriveHome(UserLocationData userLocationData);

    boolean canShowDeleteDeviceMessageBox();

    boolean canShowWeatherEffect();

    /**
     * 没有家的时候，登录账号后显示的背景图片是否要用默认的
     * 印度账号和企业账号是需要用默认的，中国个人账号根据定位结果来确定
     * @return
     */
    boolean canUseDefaultBackground();

    boolean canNeedLocatingInGpsFragment();

    boolean canShowFilterPurchase();

    int[] getEnrollDeviceType();

    boolean isTryDemoShowWaterDevice();

    boolean canShowEmotionOutDoorLayout();
}
