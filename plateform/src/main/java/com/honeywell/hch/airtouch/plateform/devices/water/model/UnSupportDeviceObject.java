package com.honeywell.hch.airtouch.plateform.devices.water.model;


import android.content.Context;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.devices.feature.controlable.UnknowDeviceControlableFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.UnknownDeviceEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.filter.UnKnownFilterFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.status.UnknownDeviceStausFeatureImpl;
import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceInfo;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;


public class UnSupportDeviceObject extends HomeDevice {

    protected Context mContext = AppManager.getInstance().getApplication();


    public UnSupportDeviceObject() {

    }

    public UnSupportDeviceObject(DeviceInfo deviceInfo) {
        mDeviceInfo = deviceInfo;

        iFilterFeature = new UnKnownFilterFeatureImpl();
        iDeviceStatusFeature = new UnknownDeviceStausFeatureImpl();
        iEnrollFeature = new UnknownDeviceEnrollFeatureImpl();
        iControlFeature = new UnknowDeviceControlableFeatureImpl();
    }

    @Override
    public int getDeviceId() {
        return mDeviceInfo.getDeviceID();
    }
}
