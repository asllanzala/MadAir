package com.honeywell.hch.airtouch.plateform.devices.feature.controlable.water;

import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.feature.controlable.IControlFeature;
import com.honeywell.hch.airtouch.plateform.devices.water.model.SmartROControl;
import com.honeywell.hch.airtouch.plateform.http.model.device.AquaTouchRunstatus;

/**
 * Created by h127856 on 16/10/27.
 */
public class WaterCanControlableFeatureImpl implements IControlFeature {

    private AquaTouchRunstatus mAquaTouchRunStatus;

    public WaterCanControlableFeatureImpl(AquaTouchRunstatus mAquaTouchRunStatus) {
        this.mAquaTouchRunStatus = mAquaTouchRunStatus;
    }

    @Override
    public boolean canRemoteControl() {
        return SmartROControl.canRemoteControl(mAquaTouchRunStatus);
    }


    @Override
    public int getAllDeviceStatusImage() {
        if (!mAquaTouchRunStatus.isAlive()) {
            return R.drawable.offline_mode_small;
        } else if (!mAquaTouchRunStatus.isNormal()) {
            return R.drawable.alert_mode_small;
        } else if (mAquaTouchRunStatus.getScenarioMode() == HPlusConstants.WATER_MODE_HOME) {
            return R.drawable.regular_small;
        } else {
            return R.drawable.holiday_small;
        }
    }

    @Override
    public String getScenerioModeAction() {
        if (SmartROControl.isOffline(mAquaTouchRunStatus)) {
            return AppManager.getInstance().getApplication().getString(R.string.offline);
        } else if (!mAquaTouchRunStatus.isNormal()) {
            return AppManager.getInstance().getApplication().getString(R.string.water_abnormal);
        } else if (mAquaTouchRunStatus.getScenarioMode() == HPlusConstants.WATER_MODE_HOME) {
            return AppManager.getInstance().getApplication().getString(R.string.water_home_mode);
        } else if (mAquaTouchRunStatus.getScenarioMode() == HPlusConstants.WATER_MODE_AWAY) {
            return AppManager.getInstance().getApplication().getString(R.string.water_away_mode);
        }
        return "";
    }

    @Override
    public boolean isCanControlable() {
        return true;
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
