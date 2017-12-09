package com.honeywell.hch.airtouch.plateform.devices.feature.status;

/**
 * Created by h127856 on 16/10/25.
 */
public class UnknownDeviceStausFeatureImpl implements IDeviceStatusFeature {


    public UnknownDeviceStausFeatureImpl(){

    }

    @Override
    public boolean isOffline() {
       return false;
    }

    @Override
    public boolean isDeviceStatusWorse() {
        return false;
    }

    @Override
    public boolean isPowerOff() {
        return false;
    }



    @Override
    public boolean isNormal() {
        return true;
    }


}
