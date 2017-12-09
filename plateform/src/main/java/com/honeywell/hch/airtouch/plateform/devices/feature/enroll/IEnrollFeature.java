package com.honeywell.hch.airtouch.plateform.devices.feature.enroll;

import android.widget.ImageView;

/**
 * Created by h127856 on 16/10/25.
 */
public interface IEnrollFeature {

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

    int getEnrollChoiceDeivceImage();

}
