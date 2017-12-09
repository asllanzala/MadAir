package com.honeywell.hch.airtouch.plateform.devices.feature.filter;


import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;

import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;

/**
 * Airtouch -P KJ450
 */
public class AirTouch450FilterFeatureImpl implements IFilterFeature {

    protected Context mContext = AppManager.getInstance().getApplication();

    @Override
    public int getFilterNumber() {
        return HPlusConstants.AIR_TOUCH_450_FILTER_NUMBER;
    }


    @Override
    public CharSequence[] getFilterNames() {
        return new CharSequence[]{mContext.getString(R.string.pre_filter),
                mContext.getString(R.string.hisiv_composite_filter)};
    }

    @Override
    public CharSequence[] getFilterDescriptions() {
        return new CharSequence[]{mContext.getString(R.string.pre_filter_instruction),
                mContext.getString(R.string.hisiv_composite_filter_instruction)};
    }

    @Override
    public String[] getFilterPurchaseUrls() {

        return new String[]{AppConfig.shareInstance().getDevicePurchaseUrl("3","PRF35M0011","KJ450F-PAC2022S"),
                AppConfig.shareInstance().getDevicePurchaseUrl("3","CMF45M3520","KJ450F-PAC2022S")};
    }

//    private SpannableString hisiv() {
//        SpannableString ssTitle = new SpannableString(mContext.getString(R.string.hisiv_450_filter));
//        ssTitle.setSpan(new SuperscriptSpan(), 5, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ssTitle.setSpan(new RelativeSizeSpan(0.6f), 5, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return ssTitle;
//    }

    private SpannableString hisivDescription() {
        SpannableString ssDescription = new SpannableString(mContext.getString(R.string.hisiv_450_filter_instruction));
        ssDescription.setSpan(new SuperscriptSpan(), 5, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssDescription.setSpan(new RelativeSizeSpan(0.6f), 5, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssDescription;
    }

    @Override
    public int[] getFilterImages() {
        return new int[]{R.drawable.ic_pre_filter, R.drawable.ic_hisiv_filter};
    }
}
