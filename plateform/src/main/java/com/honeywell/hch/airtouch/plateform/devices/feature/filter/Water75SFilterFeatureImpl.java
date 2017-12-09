package com.honeywell.hch.airtouch.plateform.devices.feature.filter;

import android.content.Context;

import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;

/**
 * Created by wuyuan on 4/19/16.
 */
public class Water75SFilterFeatureImpl implements IFilterFeature {

    private Context mContext = AppManager.getInstance().getApplication();

    @Override
    public int getFilterNumber() {
        return HPlusConstants.SMART_RO_FILTER_NUMBER;
    }


    @Override
    public String[] getFilterNames() {
        return new String[]{mContext.getString(R.string.composite_filter),
                mContext.getString(R.string.aqua_75_membrane_filter), mContext.getString(R.string.carbon_filter)};
    }

    @Override
    public String[] getFilterDescriptions() {
        return new String[]{mContext.getString(R.string.composite_filter_instruction),
                mContext.getString(R.string.membrane_filter_instruction), mContext.getString(R.string.carbon_filter_instruction)};
    }

    @Override
    public String[] getFilterPurchaseUrls() {
//        return new String[]{AppConfig.shareInstance().getBasePurchaseUrl() +
//                "model=YCX-FX150-101&product=YCZ-CT11-HRO-001&country=China&version=3",
//                AppConfig.shareInstance().getBasePurchaseUrl() + "model=YCX-MXRO11-101&product=YCZ-CT11-HRO-001&country=China&version=3",
//                AppConfig.shareInstance().getBasePurchaseUrl() + "model=YCX-XXSAC80-101&product=YCZ-CT11-HRO-001&country=China&version=3"};

        return new String[]{AppConfig.shareInstance().getDevicePurchaseUrl("3","YCX-FX150-101","YCZ-CT11-HRO-001"),
                AppConfig.shareInstance().getDevicePurchaseUrl("3","YCX-MXRO11-101","YCZ-CT8-HRO-001"),
                AppConfig.shareInstance().getDevicePurchaseUrl("3","YCX-XXSAC80-101","YCZ-CT8-HRO-001")};
    }

    @Override
    public int[] getFilterImages() {
        return new int[]{R.drawable.aqua_filter, R.drawable.aqua_filter, R.drawable.aqua_filter,};
    }

}
