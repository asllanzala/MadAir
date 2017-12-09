package com.honeywell.hch.airtouch.plateform.devices.feature.tvoc;

import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.airtouch.model.AirtouchRunStatus;

/**
 * Created by h127856 on 16/10/25.
 * tvoc的值为字符串的，如：“Good”,等
 */
public class TvocValueStringFeatureImpl extends TvocFeatureBaseImpl {

    public TvocValueStringFeatureImpl(AirtouchRunStatus airtouchRunStatus) {
        super(airtouchRunStatus);
    }

    @Override
    protected String getDetailFeatureValue(float tvocValueF) {
        switch (parseStringTvoc(tvocValueF)) {
            case HPlusConstants.GOOD:
                return AppManager.getInstance().getApplication().getText(R.string.tvoc450_good).toString();
            case HPlusConstants.AVERAGE:
                return AppManager.getInstance().getApplication().getText(R.string.tvoc450_average).toString();
            case HPlusConstants.POOR:
                return AppManager.getInstance().getApplication().getText(R.string.tvoc450_poor).toString();
            default:
                return HPlusConstants.DATA_LOADING_STATUS;
        }

    }


    @Override
    protected int getDetailFeatureLevel(double tvocValueF) {
        if (tvocValueF <  HPlusConstants.ERROR_SENSOR){
            switch (parseStringTvoc(tvocValueF)) {
                case HPlusConstants.GOOD:
                    return HPlusConstants.TVOC_GOOD_LEVEL;
                case HPlusConstants.AVERAGE:
                    return HPlusConstants.TVOC_MID_LEVEL;
                case HPlusConstants.POOR:
                    return HPlusConstants.TVOC_BAD_LEVEL;
            }
        }
        return HPlusConstants.TVOC_ERROR_LEVEL;
    }

    @Override
    protected int getDetailFeatureColor(float tvocValueF) {
        switch (parseStringTvoc(tvocValueF)) {
            case HPlusConstants.GOOD:
                return AppManager.getInstance().getApplication().getResources().getColor(R.color.pm_25_good);
            case HPlusConstants.AVERAGE:
                return AppManager.getInstance().getApplication().getResources().getColor(R.color.pm_25_bad);
            case HPlusConstants.POOR:
                return AppManager.getInstance().getApplication().getResources().getColor(R.color.pm_25_worst);
            default:
                return AppManager.getInstance().getApplication().getResources().getColor(R.color.pm_25_good);
        }
    }

    private int parseStringTvoc(double tvocValue) {
        if (tvocValue > HPlusConstants.AVERAGE) {
            tvocValue = HPlusConstants.POOR;
        } else if (tvocValue > HPlusConstants.GOOD) {
            tvocValue = HPlusConstants.AVERAGE;
        } else {
            tvocValue = HPlusConstants.GOOD;
        }
        return (int)tvocValue;
    }
}
