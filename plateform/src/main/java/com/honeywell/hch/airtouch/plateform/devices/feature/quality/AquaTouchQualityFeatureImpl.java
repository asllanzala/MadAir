package com.honeywell.hch.airtouch.plateform.devices.feature.quality;

import android.content.Context;

import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.DeviceConstant;
import com.honeywell.hch.airtouch.plateform.devices.water.model.SmartROControl;
import com.honeywell.hch.airtouch.plateform.http.model.device.AquaTouchRunstatus;

/**
 * Created by h127856 on 16/10/26.
 */
public class AquaTouchQualityFeatureImpl implements IWaterQualityFeature {

    private Context mContext = AppManager.getInstance().getApplication();
    private AquaTouchRunstatus mAquaTouchRunstatus;

    private int level;
    private boolean isOffline;

    public AquaTouchQualityFeatureImpl(AquaTouchRunstatus mAquaTouchRunstatus){
        this.mAquaTouchRunstatus = mAquaTouchRunstatus;
        level = mAquaTouchRunstatus.getWaterQualityLevel();
        isOffline = SmartROControl.isOffline(mAquaTouchRunstatus);
    }

    @Override
    public String showQualityLevel() {
        if (isOffline) {
            return HPlusConstants.DATA_LOADING_FAILED_STATUS;
        }
        switch (level) {
            case DeviceConstant.WATER_QUALITY_GOOD:
                return AppManager.getInstance().getApplication().getString(R.string.tvoc450_good);
            case DeviceConstant.WATER_QUALITY_MID:
                return AppManager.getInstance().getApplication().getString(R.string.tvoc450_average);
            case DeviceConstant.WATER_QUALITY_BAD:
                return AppManager.getInstance().getApplication().getString(R.string.tvoc450_poor);
            case DeviceConstant.SENSOR_ERROR:
                return HPlusConstants.DATA_LOADING_FAILED_STATUS;
            default:
                return HPlusConstants.DATA_LOADING_STATUS;
        }
    }

    @Override
    public int showQualityColor() {
        if (isOffline) {
            return mContext.getResources().getColor(R.color.ds_clean_now);
        }
        switch (level) {
            case DeviceConstant.WATER_QUALITY_GOOD:
                return AppManager.getInstance().getApplication().getResources().getColor(R.color.pm_25_good);
            case DeviceConstant.WATER_QUALITY_MID:
                return AppManager.getInstance().getApplication().getResources().getColor(R.color.pm_25_bad);
            case DeviceConstant.WATER_QUALITY_BAD:
                return AppManager.getInstance().getApplication().getResources().getColor(R.color.pm_25_worst);
            case DeviceConstant.SENSOR_ERROR:
                return AppManager.getInstance().getApplication().getResources().getColor(R.color.ds_clean_now);
            default:
                return AppManager.getInstance().getApplication().getResources().getColor(R.color.pm_25_good);
        }
    }

    @Override
    public int showQualityAllDeviceColor() {
        if (isOffline) {
            return mContext.getResources().getColor(R.color.ds_clean_now);
        }
        switch (level) {
            case DeviceConstant.WATER_QUALITY_GOOD:
                return AppManager.getInstance().getApplication().getResources().getColor(R.color.pm_25_good);
            case DeviceConstant.WATER_QUALITY_MID:
                return AppManager.getInstance().getApplication().getResources().getColor(R.color.pm_25_bad);
            case DeviceConstant.WATER_QUALITY_BAD:
                return AppManager.getInstance().getApplication().getResources().getColor(R.color.pm_25_worst);
            case DeviceConstant.SENSOR_ERROR:
                return AppManager.getInstance().getApplication().getResources().getColor(R.color.ds_clean_now);
            default:
                return AppManager.getInstance().getApplication().getResources().getColor(R.color.pm_25_good);
        }
    }

}
