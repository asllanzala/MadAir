package com.honeywell.hch.airtouch.plateform.devices.feature.controlable;

import com.honeywell.hch.airtouch.plateform.R;

/**
 * Created by h127856 on 16/10/27.
 */
public class UnknowDeviceControlableFeatureImpl implements IControlFeature {


    public UnknowDeviceControlableFeatureImpl(){

    }

    @Override
    public boolean canRemoteControl() {
        return false;
    }


    @Override
    public int getAllDeviceStatusImage() {
        return R.drawable.all_device_fan_small;
    }

    @Override
    public String getScenerioModeAction() {


        return "";
    }
    @Override
    public boolean isCanControlable() {
        return true;
    }

    @Override
    public int getAllDeviceBigImg() {
        return R.drawable.unknow_device;
    }
}
