package com.honeywell.hch.airtouch.plateform.devices.feature.autospeed;

import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.airtouch.model.AirtouchRunStatus;
import com.honeywell.hch.airtouch.plateform.devices.feature.speed.NineLeveFeature;

/**
 * Created by h127856 on 16/10/25.
 */
public class AutoSpeedWithTvocValueNumberImpl implements IAutoSpeedFeature, NineLeveFeature {

    private AirtouchRunStatus mAirtouchRunstatus;

    public AutoSpeedWithTvocValueNumberImpl(AirtouchRunStatus airtouchRunStatus) {
        mAirtouchRunstatus = airtouchRunStatus;
    }


    @Override
    public int getAutoSpeed() {
        int pm25Value = mAirtouchRunstatus.getmPM25Value();
        int tvocValue = mAirtouchRunstatus.getTvocValue();
        if ((pm25Value <= HPlusConstants.PM25_MEDIUM_LIMIT || pm25Value >= HPlusConstants.ERROR_SENSOR)
                &&
                (tvocValue <= HPlusConstants.TVOC_LOW_LIMIT_FOR_PREMIUM * 1000 || tvocValue >= HPlusConstants.ERROR_SENSOR)) {
            return HPlusConstants.SPEED1;
        } else if ((pm25Value > HPlusConstants.PM25_HIGH_LIMIT)
                || (tvocValue > HPlusConstants.TVOC_HIGH_LIMIT_FOR_PREMIUM * 1000)) {
            return NineLeveFeature.mWorstSpeed;
        } else {
            return NineLeveFeature.mWorseSpeed;
        }
    }
}
