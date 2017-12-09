package com.honeywell.hch.airtouch.plateform.devices.feature.status;

import com.honeywell.hch.airtouch.plateform.devices.DeviceConstant;
import com.honeywell.hch.airtouch.plateform.devices.water.model.SmartROControl;
import com.honeywell.hch.airtouch.plateform.http.model.device.AquaTouchRunstatus;

/**
 * Created by h127856 on 16/10/25.
 */
public class WaterDeviceStausFeatureImpl implements IDeviceStatusFeature {

    private AquaTouchRunstatus mAquaTouchRunStatus;

    public WaterDeviceStausFeatureImpl(AquaTouchRunstatus mAquaTouchRunStatus){
        this.mAquaTouchRunStatus = mAquaTouchRunStatus;
    }

    @Override
    public boolean isOffline() {
        return SmartROControl.isOffline(mAquaTouchRunStatus);
    }

    @Override
    public boolean isDeviceStatusWorse() {
        switch (mAquaTouchRunStatus.getWaterQualityLevel()) {
            case DeviceConstant.WATER_QUALITY_GOOD:
                return false;
            case DeviceConstant.WATER_QUALITY_MID:
            case DeviceConstant.WATER_QUALITY_BAD:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isPowerOff() {
        return SmartROControl.isPowerOff(mAquaTouchRunStatus);
    }



    @Override
    public boolean isNormal() {
        return isNormal(mAquaTouchRunStatus.getErrFlags()) && mAquaTouchRunStatus.getWaterQualityLevel() != DeviceConstant.SENSOR_ERROR;
    }


    private boolean isNormal(int[] errorFlag) {
        return errorFlag.length == 0;
    }

}
