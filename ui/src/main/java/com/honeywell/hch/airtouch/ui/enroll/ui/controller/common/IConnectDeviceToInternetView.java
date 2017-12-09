package com.honeywell.hch.airtouch.ui.enroll.ui.controller.common;

/**
 * Created by h127856 on 16/10/14.
 */
public interface IConnectDeviceToInternetView {

    void sendDeviceWifiInfoError();

    void deviceIsNotOnline();

    void updateDeviceWifiSuccess();

    void deviceHasConnectToInternet();

    void phoneNotConnectWifi();
}
