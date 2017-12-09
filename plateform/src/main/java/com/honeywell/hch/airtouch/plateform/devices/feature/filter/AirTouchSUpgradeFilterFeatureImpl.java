package com.honeywell.hch.airtouch.plateform.devices.feature.filter;


import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;

/**
 * Air Touch-S Upgrade KJ305
 */
public class AirTouchSUpgradeFilterFeatureImpl extends AirTouch450FilterFeatureImpl {

    @Override
    public CharSequence[] getFilterNames() {
        return new CharSequence[]{mContext.getString(R.string.pre_filter),
                mContext.getString(R.string.hisiv_composite_filter)};
    }

    @Override
    public String[] getFilterPurchaseUrls() {
//        return new String[]{AppConfig.shareInstance().getBasePurchaseUrl() +
//                "version=3&model=PRF35M0011&product=KJ300F-JAC2801W&country=China",
//                AppConfig.shareInstance().getBasePurchaseUrl() + "version=3&model=CMF30M3200&product=KJ300F-JAC2801W&country=China"
//        };

        return new String[]{AppConfig.shareInstance().getDevicePurchaseUrl("3","PRF35M0011","KJ300F-JAC2801W"),
                AppConfig.shareInstance().getDevicePurchaseUrl("3","CMF30M3200","KJ300F-JAC2801W")};
    }
}
