package com.honeywell.hch.airtouch.plateform.devices.feature.filter;


import android.content.Context;

import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;

/**
 * Created by Qian Jin on 3/28/16.
 */
public class AirtouchXFilterFeatureImpl implements IFilterFeature {

    protected Context mContext = AppManager.getInstance().getApplication();


    @Override
    public int getFilterNumber() {
        return HPlusConstants.AIR_TOUCH_X_FILTER_NUMBER;
    }

    @Override
    public CharSequence[] getFilterNames() {
        return new CharSequence[]{mContext.getString(R.string.airpremium_filter_title)};
    }

    @Override
    public CharSequence[] getFilterDescriptions() {
        return new CharSequence[]{mContext.getString(R.string.airpremium_filter_des)};
    }

    @Override
    public String[] getFilterPurchaseUrls() {
//        return new String[]{AppConfig.shareInstance().getBasePurchaseUrl() +
//                "version=3&model=CMF75M4010&product=KJ700G-PAC2127W&country=China"};

        return new String[]{AppConfig.shareInstance().getDevicePurchaseUrl("3","CMF75M4010","KJ700G-PAC2127W")};
    }


    @Override
    public int[] getFilterImages() {
        return new int[]{R.drawable.premium_polytesh};
    }

}
