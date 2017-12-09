package com.honeywell.hch.airtouch.plateform.devices.feature.filter;


import android.content.Context;

import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;

/**
 *
 * Airtouch-X3 KJ620
 */
public class X3UpgradeCompactFilterFeatureImpl extends X3CompactFilterFeatureImpl {

    @Override
    public String[] getFilterPurchaseUrls() {
//        return new String[]{AppConfig.shareInstance().getBasePurchaseUrl() +
//                "version=3&model=PRF62M0013&product=KJ620F-PAC2159R&country=China", AppConfig.shareInstance().getBasePurchaseUrl() +
//                "version=3&model=CMF62M4013&product=KJ620F-PAC2159R&country=China"};

        return new String[]{AppConfig.shareInstance().getDevicePurchaseUrl("3","PRF62M0013","KJ620F-PAC2159R"),
                AppConfig.shareInstance().getDevicePurchaseUrl("3","CMF62M4013","KJ620F-PAC2159R")};
    }

}
