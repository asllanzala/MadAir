package com.honeywell.hch.airtouch.plateform.devices.feature.enroll;

import android.widget.ImageView;

import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;

/**
 * Created by Qian Jin on 3/28/16.
 * AirTouchSFeature from JD.
 */
public class AirTouchJDSEnrollFeatureImpl implements IEnrollFeature {

    @Override
    public void setImageDrawable(ImageView deviceImageView) {
        deviceImageView.setImageDrawable(AppManager.getInstance().getApplication().getResources()
                .getDrawable(R.drawable.all_device_airtouch_s));
    }

    @Override
    public int getEnrollDeviceName() {
        return R.string.airtouch_s_str;
    }

    @Override
    public int getEnrollDeviceImage() {
        return R.drawable.enroll_air_touch_450;
    }


    @Override
    public int getEnrollChoiceDeivceImage() {
        return R.drawable.air_touch_s_click;
    }


    @Override
    public String getEnrollDeviceWifiPre() {
        return HPlusConstants.AIR_TOUCH_P_SSID;
    }


}
