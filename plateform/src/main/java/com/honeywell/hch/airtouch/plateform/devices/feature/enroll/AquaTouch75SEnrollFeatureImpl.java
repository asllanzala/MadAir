package com.honeywell.hch.airtouch.plateform.devices.feature.enroll;


import android.content.Context;
import android.widget.ImageView;

import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;

/**
 * Created by Qian Jin on 3/28/16.
 */
public class AquaTouch75SEnrollFeatureImpl implements IEnrollFeature {

    protected Context mContext = AppManager.getInstance().getApplication();

    @Override
    public void setImageDrawable(ImageView deviceImageView) {
        deviceImageView.setImageDrawable(AppManager.getInstance().getApplication()
                .getResources().getDrawable(R.drawable.smart_ro_image));
    }

    @Override
    public int getEnrollDeviceImage() {
        return R.drawable.enroll_ro_100series;
    }

    @Override
    public int getEnrollDeviceName() {
        return R.string.smart_ro_75_str;
    }

    @Override
    public String getEnrollDeviceWifiPre() {
        return HPlusConstants.AQUA_TOUCH_75_SSID;
    }

    @Override
    public int getEnrollChoiceDeivceImage() {
        return R.drawable.smart_ro_click_100series;
    }


}
