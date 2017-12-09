package com.honeywell.hch.airtouch.plateform.devices.feature.filter;

/**
 * Created by h127856 on 16/10/24.
 */
public interface IFilterFeature {
    int getFilterNumber();
    CharSequence[] getFilterNames();
    CharSequence[] getFilterDescriptions();
    String[] getFilterPurchaseUrls();
    int[] getFilterImages();
}
