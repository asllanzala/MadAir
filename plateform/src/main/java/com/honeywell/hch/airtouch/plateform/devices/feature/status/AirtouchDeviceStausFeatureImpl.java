package com.honeywell.hch.airtouch.plateform.devices.feature.status;

import android.content.Context;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.airtouch.model.AirtouchRunStatus;
import com.honeywell.hch.airtouch.plateform.devices.common.AirTouchControl;

/**
 * Created by h127856 on 16/10/25.
 */
public class AirtouchDeviceStausFeatureImpl implements IDeviceStatusFeature {

    private AirtouchRunStatus mAirtouchDeviceRunStatus;
    private Context mContext = AppManager.getInstance().getApplication();

    public AirtouchDeviceStausFeatureImpl(AirtouchRunStatus mAirtouchRunstatus){
        this.mAirtouchDeviceRunStatus = mAirtouchRunstatus;
    }

    @Override
    public boolean isOffline() {
        return AirTouchControl.isOffline(mAirtouchDeviceRunStatus);
    }

    @Override
    public boolean isDeviceStatusWorse() {
        if (mAirtouchDeviceRunStatus == null) {
            return false;
        }

        int pm25Value = mAirtouchDeviceRunStatus.getmPM25Value();
        if (pm25Value < HPlusConstants.PM25_MEDIUM_LIMIT || pm25Value == HPlusConstants.ERROR_MAX_VALUE
                || pm25Value == HPlusConstants.ERROR_SENSOR) {
            return false;
        }

        return true;
    }

    @Override
    public boolean isPowerOff() {
        return AirTouchControl.isPowerOff(mAirtouchDeviceRunStatus);
    }



    @Override
    public boolean isNormal() {
        return true;
    }

}
