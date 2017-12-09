package com.honeywell.hch.airtouch.plateform.devices.common.protocol;

import android.widget.ImageView;

/**
 * Created by Vincent on 20/4/16.
 */
public interface IHPlusDeviceFeature {
    /**
     * set device image icon
     */
    void setImageDrawable(ImageView deviceImageView);

    /**
     * get device big image and name in enrollment pages
     */
    int getEnrollDeviceImage();
    int getEnrollDeviceName();

    String getEnrollDeviceWifiPre();

    /**
     * AllDeviceAirTouchView whether show Tvoc or not
     */
    boolean canShowTvoc();

    CharSequence[] getFilterNames();
    CharSequence[] getFilterDescriptions();
    String[] getFilterPurchaseUrls();

    int getEnrollChoiceDeivceImage();

    int[] getFilterImages();


}
