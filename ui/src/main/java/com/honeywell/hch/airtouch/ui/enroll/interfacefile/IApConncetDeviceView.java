package com.honeywell.hch.airtouch.ui.enroll.interfacefile;

import android.net.wifi.ScanResult;

import com.honeywell.hch.airtouch.plateform.ap.WAPIRouter;

import java.util.ArrayList;

/**
 * Created by Vincent on 14/10/16.
 */
public interface IApConncetDeviceView {

    void settingDialog();

    void showNetWorkListItem(ArrayList<WAPIRouter> mWAPIRouters);

    void wifiIsOff();

    void connectWifiSuccess();

    void disableNextButton();

    void dealWithSelfDeviceAlreadyEnrolled();

    void dealWithAuthDeviceAlreadyEnrolled();

    void dealWithConnectDeviceSuccess();

    void reConncetDeviceWifiError();

    void connectWifiButCantCommunication();

    void connectWifiButSocektTimeOut();

    void connectWifiError(String errorMsg);
}
