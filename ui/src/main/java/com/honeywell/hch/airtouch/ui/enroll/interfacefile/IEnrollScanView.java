package com.honeywell.hch.airtouch.ui.enroll.interfacefile;

import com.honeywell.hch.airtouch.library.http.model.ResponseResult;

/**
 * Created by Vincent on 17/10/16.
 */
public interface IEnrollScanView {

    void unKnowDevice();

    void disMissDialog();

    void dealSuccessCallBack();

    void deviceModelErrorl();

    void unSupportSmartLinkDevice();

    void updateWifi();

    void registedAuthDevice();

    void showNoNetWorkDialog();

    void startIntent(Class mClass);

    void updateVersion();

    void errorHandle(ResponseResult responseResult, String errorMsg);

    void invalidDevice();

    void madAirDeviceAlreadyEnrolled();

}
