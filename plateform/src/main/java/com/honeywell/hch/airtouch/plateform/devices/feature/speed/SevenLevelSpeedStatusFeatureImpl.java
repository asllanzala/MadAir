package com.honeywell.hch.airtouch.plateform.devices.feature.speed;

import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.airtouch.model.AirtouchRunStatus;

/**
 * Created by h127856 on 16/10/25.
 */
public class SevenLevelSpeedStatusFeatureImpl extends SpeedStatusBaseFeatureImpl implements SevenLeveFeature {

    public SevenLevelSpeedStatusFeatureImpl(AirtouchRunStatus mRunStatus, int mControlPoint, int mPointNumberPerSpped
            , int mSleepSpeed, int mSlientSpeed){
        super(mRunStatus, SEVEN_MAX_SPEED,SEVEN_WORSE_SPEED,SEVEN_WORST_SPEED,
                mControlPoint,mPointNumberPerSpped,mSleepSpeed,mSlientSpeed);
    }


    @Override
    public String getSpeedLevel(String speed) {
        switch (speed) {
            case HPlusConstants.SPEED_1:
            case HPlusConstants.SPEED_2:
                return AppManager.getInstance().getApplication().getString(R.string.speed_low);
            case HPlusConstants.SPEED_3:
            case HPlusConstants.SPEED_4:
            case HPlusConstants.SPEED_5:
                return AppManager.getInstance().getApplication().getString(R.string.speed_medium);
            case HPlusConstants.SPEED_6:
            case HPlusConstants.SPEED_7:
                return AppManager.getInstance().getApplication().getString(R.string.speed_high);
            default:
                return AppManager.getInstance().getApplication().getString(R.string.offline);
        }
    }
}
