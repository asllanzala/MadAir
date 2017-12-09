package com.honeywell.hch.airtouch.plateform.devices.feature.filter;

import android.content.Context;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;

/**
 * Created by wuyuan on 4/19/16.
 */
public class UnKnownFilterFeatureImpl implements IFilterFeature {

    private Context mContext = AppManager.getInstance().getApplication();

    @Override
    public int getFilterNumber() {
        return HPlusConstants.SMART_RO_FILTER_NUMBER;
    }


    @Override
    public String[] getFilterNames() {
        return new String[]{};
    }

    @Override
    public String[] getFilterDescriptions() {
        return new String[]{};
    }

    @Override
    public String[] getFilterPurchaseUrls() {
        return new String[]{};
    }

    @Override
    public int[] getFilterImages() {
        return new int[]{};
    }

}
