package com.honeywell.hch.airtouch.plateform.devices.feature.autospeed;

import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.airtouch.model.AirtouchRunStatus;
import com.honeywell.hch.airtouch.plateform.devices.feature.speed.SevenLeveFeature;

/**
 * Created by h127856 on 16/10/25.
 */
public class AutoSpeedOnlyPmIForFFACImpl implements IAutoSpeedFeature, SevenLeveFeature {

    private AirtouchRunStatus mRunstatus;

    public AutoSpeedOnlyPmIForFFACImpl(AirtouchRunStatus airtouchRunStatus) {
        mRunstatus = airtouchRunStatus;
    }


    @Override
    public int getAutoSpeed() {
        int pm25Value = mRunstatus.getmPM25Value();
        if (pm25Value <= HPlusConstants.PM25_LOW_LIMIT ||
                pm25Value == HPlusConstants.ERROR_MAX_VALUE || pm25Value == HPlusConstants.ERROR_SENSOR) {
            return HPlusConstants.SPEED1;
        } else if (pm25Value > HPlusConstants.PM25_LOW_LIMIT && pm25Value <= HPlusConstants.PM25_MEDIUM_LIMIT) {
            return HPlusConstants.SPEED2;
        } else if (pm25Value > HPlusConstants.PM25_MEDIUM_LIMIT && pm25Value <= HPlusConstants.PM25_MEDIUMER_LIMIT) {
            return HPlusConstants.SPEED3;
        } else if (pm25Value > HPlusConstants.PM25_MEDIUMER_LIMIT && pm25Value <= HPlusConstants.PM25_HIGH_LIMIT) {
            return HPlusConstants.SPEED4;
        } else {
            return HPlusConstants.SPEED6;
        }
    }
}
