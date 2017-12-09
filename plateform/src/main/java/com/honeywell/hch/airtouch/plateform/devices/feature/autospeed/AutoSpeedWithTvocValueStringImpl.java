package com.honeywell.hch.airtouch.plateform.devices.feature.autospeed;

import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.airtouch.model.AirtouchRunStatus;
import com.honeywell.hch.airtouch.plateform.devices.feature.speed.SevenLeveFeature;

/**
 * Created by h127856 on 16/10/25.
 */
public class AutoSpeedWithTvocValueStringImpl implements IAutoSpeedFeature, SevenLeveFeature {

    private AirtouchRunStatus mAirtouchRunstatus;

    public AutoSpeedWithTvocValueStringImpl(AirtouchRunStatus airtouchRunStatus) {
        mAirtouchRunstatus = airtouchRunStatus;
    }

    @Override

    public int getAutoSpeed() {
        int pm25Value = mAirtouchRunstatus.getmPM25Value();
        int tvocValue = parseStringTvoc(mAirtouchRunstatus.getTvocValue());
        if (
                (pm25Value <= HPlusConstants.PM25_MEDIUM_LIMIT || pm25Value >= HPlusConstants.ERROR_SENSOR)
                        &&
                        (tvocValue == HPlusConstants.TVOC_LOW_LIMIT_FOR_450 || tvocValue >= HPlusConstants.ERROR_SENSOR)) {
            return HPlusConstants.SPEED1;
        } else if (pm25Value > HPlusConstants.PM25_HIGH_LIMIT
                || tvocValue == HPlusConstants.TVOC_HIGH_LIMIT_FOR_450) {
            return SEVEN_WORST_SPEED;
        } else {
            return SEVEN_WORSE_SPEED;
        }
    }

    private int parseStringTvoc(double tvocValue) {
        if (tvocValue > HPlusConstants.AVERAGE && tvocValue < HPlusConstants.ERROR_SENSOR) {
            tvocValue = HPlusConstants.POOR;
        } else if (tvocValue > HPlusConstants.GOOD && tvocValue <= HPlusConstants.AVERAGE) {
            tvocValue = HPlusConstants.AVERAGE;
        } else {
            tvocValue = HPlusConstants.GOOD;
        }
        return (int) tvocValue;
    }
}
