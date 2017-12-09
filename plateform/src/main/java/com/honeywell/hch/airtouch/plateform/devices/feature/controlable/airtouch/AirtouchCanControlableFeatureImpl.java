package com.honeywell.hch.airtouch.plateform.devices.feature.controlable.airtouch;

import android.content.Context;

import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.airtouch.model.AirtouchRunStatus;
import com.honeywell.hch.airtouch.plateform.devices.common.AirTouchControl;
import com.honeywell.hch.airtouch.plateform.devices.feature.controlable.IControlFeature;

/**
 * Created by h127856 on 16/10/27.
 */
public class AirtouchCanControlableFeatureImpl implements IControlFeature {

    private Context mContext = AppManager.getInstance().getApplication();
    private AirtouchRunStatus mAirtouchDeviceRunStatus;

    public AirtouchCanControlableFeatureImpl(AirtouchRunStatus mAirtouchDeviceRunStatus){
        this.mAirtouchDeviceRunStatus = mAirtouchDeviceRunStatus;
    }

    @Override
    public boolean canRemoteControl() {
        return AirTouchControl.canRemoteControl(mAirtouchDeviceRunStatus);
    }


    @Override
    public int getAllDeviceStatusImage() {
        if (AirTouchControl.isOffline(mAirtouchDeviceRunStatus)) {
            return R.drawable.offline_mode_small;
        } else {
            return R.drawable.all_device_fan_small;
        }
    }

    @Override
    public String getScenerioModeAction() {
        if (mAirtouchDeviceRunStatus == null)
            return mContext.getString(R.string.offline);

        int mode = mAirtouchDeviceRunStatus.getScenarioMode();
        String modeOrSpeed = "";

        if (AirTouchControl.isOffline(mAirtouchDeviceRunStatus)) {
            modeOrSpeed = mContext.getString(R.string.offline);
        } else if (mode == HPlusConstants.MODE_AUTO_INT) {
            modeOrSpeed = mContext.getString(R.string.control_auto);
        } else if (mode == HPlusConstants.MODE_SLEEP_INT) {
            modeOrSpeed = mContext.getString(R.string.control_sleep);
        } else if (mode == HPlusConstants.MODE_QUICK_INT) {
            modeOrSpeed = mContext.getString(R.string.control_quick);
        } else if (mode == HPlusConstants.MODE_SILENT_INT) {
            modeOrSpeed = mContext.getString(R.string.control_silent);
        } else if (mode == HPlusConstants.MODE_OFF_INT) {
            modeOrSpeed = mContext.getString(R.string.off);
        } else if (mode != HPlusConstants.MODE_DEFAULT_NONE_MODE) {
            modeOrSpeed = mAirtouchDeviceRunStatus.getFanSpeedStatus();
            if (modeOrSpeed.contains("Speed")) {
                return mContext.getString(R.string.all_device_fan_speed, modeOrSpeed.substring(6, 7));
            } else {
                return mContext.getString(R.string.all_device_fan_speed, "0");
            }
//            return getSpeedLevel(modeOrSpeed);
        }

        return modeOrSpeed;
    }
    @Override
    public boolean isCanControlable() {
        return true;
    }

    @Override
    public int getAllDeviceBigImg() {
        return R.drawable.all_device_air_icon;
    }
}
