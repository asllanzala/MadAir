package com.honeywell.hch.airtouch.ui.control.manager.umeng;

import com.honeywell.hch.airtouch.ui.enroll.manager.EnrollDeviceManager;

/**
 * Created by h127856 on 16/10/10.
 */
public class UmengUiManager {

    public static String getEnrollProductName() {
        EnrollDeviceManager enrollDeviceManager = new EnrollDeviceManager();
        return enrollDeviceManager.getDeviceWifiStr();
    }
}
