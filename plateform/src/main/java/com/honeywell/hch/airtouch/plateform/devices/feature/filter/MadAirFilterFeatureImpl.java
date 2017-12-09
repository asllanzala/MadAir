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
 * Created by Qian Jin on 3/28/16.
 */
public class MadAirFilterFeatureImpl implements IFilterFeature {

    private Context mContext = AppManager.getInstance().getApplication();

    @Override
    public int getFilterNumber() {
        return HPlusConstants.MAD_AIR_FILTER_NUMBER;
    }


    @Override
    public CharSequence[] getFilterNames() {
        return new CharSequence[]{mContext.getString(R.string.mad_air_filter),
                hisiv()};
    }

    @Override
    public CharSequence[] getFilterDescriptions() {
        return new String[]{""};
    }

    @Override
    public String[] getFilterPurchaseUrls() {
//        return new String[]{AppConfig.shareInstance().getBasePurchaseUrl() +
//                "version=2&model=madairfilter&product=MA50100PW&country=China"};

        return new String[]{AppConfig.shareInstance().getDevicePurchaseUrl("2","madairfilter","MA50100PW")};
    }

    private SpannableString hisiv() {
        SpannableString ssTitle = new SpannableString(mContext.getString(R.string.mad_air_filter));
        return ssTitle;
    }

    private SpannableString hisivDescription() {
        SpannableString ssDescription = new SpannableString(mContext.getString(R.string.hisiv_450_filter_instruction));
        ssDescription.setSpan(new SuperscriptSpan(), 5, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssDescription.setSpan(new RelativeSizeSpan(0.6f), 5, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssDescription;
    }

    @Override
    public int[] getFilterImages() {
        return new int[]{R.drawable.ic_pre_filter};
    }
}
