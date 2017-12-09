package com.honeywell.hch.airtouch.plateform.devices.feature.filter;


import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;

/**
 * Air Touch-P Upgrade KJ455
 */
public class AirTouch450UpgradeFilterFeatureImpl extends AirTouch450FilterFeatureImpl {

    @Override
    public CharSequence[] getFilterNames() {
        return new CharSequence[]{mContext.getString(R.string.pre_filter),
                mContext.getString(R.string.hisiv_composite_filter)};
    }

    @Override
    public String[] getFilterPurchaseUrls() {
//        return new String[]{AppConfig.shareInstance().getBasePurchaseUrl() +
//                "version=3&model=PRF35M0011&product=KJ450F-JAC2522S&country=China",
//                AppConfig.shareInstance().getBasePurchaseUrl() +
//                        "version=3&model=CMF45M5500&product=KJ450F-JAC2522S&country=China"};

        return new String[]{AppConfig.shareInstance().getDevicePurchaseUrl("3","PRF35M0011","KJ450F-JAC2522S"),
                AppConfig.shareInstance().getDevicePurchaseUrl("3","CMF45M5500","KJ450F-JAC2522S")};
    }
}
