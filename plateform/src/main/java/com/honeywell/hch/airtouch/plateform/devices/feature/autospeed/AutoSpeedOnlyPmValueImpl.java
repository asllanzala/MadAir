package com.honeywell.hch.airtouch.plateform.devices.feature.autospeed;

import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.airtouch.model.AirtouchRunStatus;
import com.honeywell.hch.airtouch.plateform.devices.feature.speed.SevenLeveFeature;

/**
 * Created by h127856 on 16/10/25.
 */
public class AutoSpeedOnlyPmValueImpl implements IAutoSpeedFeature, SevenLeveFeature {

    private AirtouchRunStatus mRunstatus;

    public AutoSpeedOnlyPmValueImpl(AirtouchRunStatus airtouchRunStatus) {
        mRunstatus = airtouchRunStatus;
    }

    @Override
    public int getAutoSpeed() {
        int pm25Value = mRunstatus.getmPM25Value();
        if (pm25Value <= HPlusConstants.PM25_MEDIUM_LIMIT ||
                pm25Value == HPlusConstants.ERROR_MAX_VALUE || pm25Value == HPlusConstants.ERROR_SENSOR) {
            return HPlusConstants.SPEED1;
        } else if (pm25Value > HPlusConstants.PM25_MEDIUM_LIMIT && pm25Value <= HPlusConstants.PM25_HIGH_LIMIT) {
            return SEVEN_WORSE_SPEED;
        } else {
            return SEVEN_WORST_SPEED;
        }
    }
}
