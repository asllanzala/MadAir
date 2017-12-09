package com.honeywell.hch.airtouch.plateform.devices.feature.filter;

import android.content.Context;

import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;

/**
 * Created by h127856 on 16/10/26.
 */
public class Water600SFilterFeatureImpl implements IFilterFeature {

    private Context mContext = AppManager.getInstance().getApplication();


    @Override
    public int getFilterNumber() {
        return HPlusConstants.SMART_RO_FILTER_NUMBER;
    }

    @Override
    public String[] getFilterNames() {
        return new String[]{mContext.getString(R.string.composite_filter),
                mContext.getString(R.string.aqua_600_membrane_filter), mContext.getString(R.string.carbon_filter)};
    }

    @Override
    public String[] getFilterDescriptions() {
        return new String[]{mContext.getString(R.string.composite_filter_instruction),
                mContext.getString(R.string.membrane_filter_instruction), mContext.getString(R.string.carbon_filter_instruction)};
    }

    @Override
    public String[] getFilterPurchaseUrls() {
//        return new String[]{AppConfig.shareInstance().getBasePurchaseUrl() +
//                "model=YCX-FX180-601&product=WTE-P-D-(FST)-90-HRO-600-S&country=China&version=3",
//                AppConfig.shareInstance().getBasePurchaseUrl() + "model=YCX-MXRO90-601&product=WTE-P-D-(FST)-90-HRO-600-S&country=China&version=3",
//                AppConfig.shareInstance().getBasePurchaseUrl() + "model=YCX-XXSAC90-601&product=WTE-P-D-(FST)-90-HRO-600-S&country=China&version=3"};

        return new String[]{AppConfig.shareInstance().getDevicePurchaseUrl("3","YCX-FX180-601","WTE-P-D-(FST)-90-HRO-600-S"),
                AppConfig.shareInstance().getDevicePurchaseUrl("3","YCX-MXRO90-601","WTE-P-D-(FST)-90-HRO-600-S"),
                AppConfig.shareInstance().getDevicePurchaseUrl("3","YCX-XXSAC90-601","WTE-P-D-(FST)-90-HRO-600-S")};
    }


    @Override
    public int[] getFilterImages() {
        return new int[]{R.drawable.aqua_filter, R.drawable.aqua_filter, R.drawable.aqua_filter,};
    }
}
