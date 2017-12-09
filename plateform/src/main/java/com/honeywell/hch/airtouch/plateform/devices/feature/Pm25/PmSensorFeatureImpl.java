package com.honeywell.hch.airtouch.plateform.devices.feature.Pm25;

import com.honeywell.hch.airtouch.library.LibApplication;
import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.airtouch.model.AirtouchRunStatus;

/**
 * Created by h127856 on 16/10/24.
 */
public class PmSensorFeatureImpl implements IPmSensorFeature {

    private AirtouchRunStatus mAirtouchDeviceRunStatus;

    public PmSensorFeatureImpl(AirtouchRunStatus mAirtouchDeviceRunStatus){
          this.mAirtouchDeviceRunStatus = mAirtouchDeviceRunStatus;
    }

    @Override
    public String getPm25Value() {
        if (mAirtouchDeviceRunStatus == null) {
            return HPlusConstants.DATA_LOADING_STATUS;
        } else if (!mAirtouchDeviceRunStatus.getIsAlive()) {
            return HPlusConstants.DATA_LOADING_FAILED_STATUS;
        }

        int pm25Value = mAirtouchDeviceRunStatus.getmPM25Value();
        if (pm25Value >= 0 && pm25Value < HPlusConstants.PM25_MAX_VALUE) {
            return String.valueOf(pm25Value);
        } else if (pm25Value >= HPlusConstants.PM25_MAX_VALUE
                && pm25Value != HPlusConstants.ERROR_MAX_VALUE && pm25Value != HPlusConstants.ERROR_SENSOR) {
            return String.valueOf(HPlusConstants.PM25_MAX_VALUE);
        } else if (pm25Value == HPlusConstants.ERROR_MAX_VALUE) {
            return HPlusConstants.DATA_LOADING_STATUS;
        } else if (pm25Value == HPlusConstants.ERROR_SENSOR) {
            return HPlusConstants.DATA_LOADING_FAILED_STATUS;
        }
        return HPlusConstants.DATA_LOADING_STATUS;
    }

    @Override
    public int getPm25Color() {
        if (mAirtouchDeviceRunStatus == null) {
            return LibApplication.getContext().getResources().getColor(R.color.pm_25_good);
        } else if (!mAirtouchDeviceRunStatus.getIsAlive()) {
            return LibApplication.getContext().getResources().getColor(R.color.ds_clean_now);
        }

        int pm25Value = mAirtouchDeviceRunStatus.getmPM25Value();
        if (pm25Value <= HPlusConstants.PM25_MEDIUM_LIMIT || pm25Value == HPlusConstants.ERROR_MAX_VALUE) {
            return LibApplication.getContext().getResources().getColor(R.color.pm_25_good);
        } else if (pm25Value <= HPlusConstants.PM25_HIGH_LIMIT) {
            return LibApplication.getContext().getResources().getColor(R.color.pm_25_bad);
        } else if (pm25Value == HPlusConstants.ERROR_SENSOR) {
            return LibApplication.getContext().getResources().getColor(R.color.ds_clean_now);
        } else {
            return LibApplication.getContext().getResources().getColor(R.color.pm_25_worst);
        }
    }
}
