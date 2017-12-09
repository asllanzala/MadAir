package com.honeywell.hch.airtouch.plateform.http.manager.model;

import com.honeywell.hch.airtouch.plateform.devices.feature.controlable.IControlFeature;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.IEnrollFeature;
import com.honeywell.hch.airtouch.plateform.devices.feature.filter.IFilterFeature;
import com.honeywell.hch.airtouch.plateform.devices.feature.status.IDeviceStatusFeature;

/**
 * Created by wuyuan on 7/30/15.
 * <p/>
 * by Stephen(H127856)
 * data model reconstruction
 */
public abstract class HomeDevice {

    protected int isMasterDevice;

    protected DeviceInfo mDeviceInfo;

    protected IFilterFeature iFilterFeature;

    protected IDeviceStatusFeature iDeviceStatusFeature;

    protected IEnrollFeature iEnrollFeature;

    protected IControlFeature iControlFeature;

    public DeviceInfo getDeviceInfo() {
        return mDeviceInfo;
    }

    public abstract int getDeviceId();

    /**
     * get device type
     *
     * @return device type,like HPlusConstants.AIRTOUCH_S_TYPE or HPlusConstants.AIRTOUCH_X_TYPE
     */
    public int getDeviceType() {
        if (mDeviceInfo == null)
            return 0;

        return mDeviceInfo.getDeviceType();
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        mDeviceInfo = deviceInfo;
    }

    public int getIsMasterDevice() {
        return isMasterDevice;
    }

    public void setIsMasterDevice(int isMasterDevice) {
        this.isMasterDevice = isMasterDevice;
    }

    public IFilterFeature getiFilterFeature() {
        return iFilterFeature;
    }

    public IDeviceStatusFeature getiDeviceStatusFeature() {
        return iDeviceStatusFeature;
    }

    public IEnrollFeature getiEnrollFeature() {
        return iEnrollFeature;
    }

    public IControlFeature getControlFeature() {
        return iControlFeature;
    }
}