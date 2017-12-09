package com.honeywell.hch.airtouch.plateform.devices.feature.tvoc;

import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.airtouch.model.AirtouchRunStatus;

/**
 * Created by h127856 on 16/10/25.
 */
public class TvocFeatureBaseImpl implements ITvocFeature {

    protected AirtouchRunStatus mAirtouchDeviceRunStatus;

    public TvocFeatureBaseImpl(AirtouchRunStatus airtouchRunStatus){
        mAirtouchDeviceRunStatus = airtouchRunStatus;
    }

    @Override
    public String getTVOC() {
        if (mAirtouchDeviceRunStatus == null) {
            return HPlusConstants.DATA_LOADING_STATUS;
        } else if (!mAirtouchDeviceRunStatus.getIsAlive()) {
            return HPlusConstants.DATA_LOADING_FAILED_STATUS;
        }
        float tvocValueF = mAirtouchDeviceRunStatus.getTvocValue();
        if (tvocValueF == HPlusConstants.ERROR_MAX_VALUE) {
            return HPlusConstants.DATA_LOADING_STATUS;
        } else if (tvocValueF == HPlusConstants.ERROR_SENSOR) {
            return HPlusConstants.DATA_LOADING_FAILED_STATUS;
        }
        return getDetailFeatureValue(tvocValueF);
    }

    @Override
    public int getTVOCColor() {
        if (mAirtouchDeviceRunStatus == null) {
            return AppManager.getInstance().getApplication().getResources().getColor(R.color.pm_25_good);
        } else if (!mAirtouchDeviceRunStatus.getIsAlive()) {
            return AppManager.getInstance().getApplication().getResources().getColor(R.color.ds_clean_now);
        }
        float tvocValueF = mAirtouchDeviceRunStatus.getTvocValue();
        if (tvocValueF == HPlusConstants.ERROR_MAX_VALUE || tvocValueF <= 0) {
            return AppManager.getInstance().getApplication().getResources().getColor(R.color.pm_25_good);
        } else if (tvocValueF == HPlusConstants.ERROR_SENSOR) {
            return AppManager.getInstance().getApplication().getResources().getColor(R.color.ds_clean_now);
        }
        return getDetailFeatureColor(tvocValueF);
    }

    @Override
    public int getTvocLevel() {
        if (mAirtouchDeviceRunStatus == null) {
            return HPlusConstants.TVOC_ERROR_LEVEL;
        } else if (!mAirtouchDeviceRunStatus.getIsAlive()) {
            return HPlusConstants.TVOC_ERROR_LEVEL;
        }
        double tvocValueF = mAirtouchDeviceRunStatus.getTvocValue();
        return getDetailFeatureLevel(tvocValueF);
    }


    /**
     * 需要子类进行重写
     * @param tvocValueF
     * @return
     */
    protected String getDetailFeatureValue(float tvocValueF){
        return HPlusConstants.DATA_LOADING_STATUS;
    }


    /**
     * 需要子类进行重写
     * @param tvocValueF
     * @return
     */
    protected int getDetailFeatureColor(float tvocValueF){
        return AppManager.getInstance().getApplication().getResources().getColor(R.color.pm_25_good);
    }


    /**
     * 需要子类进行重写
     * @param tvocValueF
     * @return
     */
    protected int getDetailFeatureLevel(double tvocValueF){
        return HPlusConstants.TVOC_ERROR_LEVEL;
    }
}
