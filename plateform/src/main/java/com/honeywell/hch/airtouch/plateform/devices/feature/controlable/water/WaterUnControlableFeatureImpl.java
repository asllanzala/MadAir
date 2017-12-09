package com.honeywell.hch.airtouch.plateform.devices.feature.controlable.water;

import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.devices.feature.controlable.IControlFeature;
import com.honeywell.hch.airtouch.plateform.devices.water.model.SmartROControl;
import com.honeywell.hch.airtouch.plateform.http.model.device.AquaTouchRunstatus;

/**
 * Created by h127856 on 16/10/27.
 */
public class WaterUnControlableFeatureImpl implements IControlFeature {

    private AquaTouchRunstatus mAquaTouchRunStatus;

    public WaterUnControlableFeatureImpl(AquaTouchRunstatus mAquaTouchRunStatus){
        this.mAquaTouchRunStatus = mAquaTouchRunStatus;
    }

    @Override
    public boolean canRemoteControl() {
        return false;
    }


    @Override
    public int getAllDeviceStatusImage() {
        if (!mAquaTouchRunStatus.isAlive()) {
            return R.drawable.offline_mode_small;
        } else if (!mAquaTouchRunStatus.isNormal()) {
            return R.drawable.alert_mode_small;
        }
        return R.drawable.regular_small;
    }

    @Override
    public String getScenerioModeAction() {
        if (SmartROControl.isOffline(mAquaTouchRunStatus)) {
            return AppManager.getInstance().getApplication().getString(R.string.offline);
        } else if (!mAquaTouchRunStatus.isNormal()) {
            return AppManager.getInstance().getApplication().getString(R.string.water_abnormal);
        }
        return AppManager.getInstance().getApplication().getString(R.string.water_home_mode);
    }

    @Override
    public boolean isCanControlable() {
        return false;
    }

    @Override
    public int getAllDeviceBigImg() {
        if (mAquaTouchRunStatus.isNormal()) {
            return R.drawable.all_device_ro_icon;
        } else {
            return R.drawable.all_device_ro_icon_abnormal;
        }
    }
}
