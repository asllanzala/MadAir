package com.honeywell.hch.airtouch.plateform.devices.feature.enroll;


import android.content.Context;
import android.widget.ImageView;

import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;

/**
 * Created by Qian Jin on 3/28/16.
 */
public class AirTouchFFACEnrollFeatureImpl implements IEnrollFeature {

    private Context mContext = AppManager.getInstance().getApplication();

    @Override
    public void setImageDrawable(ImageView deviceImageView) {
        deviceImageView.setImageDrawable(mContext.getResources()
                .getDrawable(R.drawable.all_device_airtouch_ffac));
    }

    @Override
    public String getEnrollDeviceWifiPre(){
        return HPlusConstants.AIR_TOUCH_FFAC_SSID;
    }

    @Override
    public int getEnrollDeviceImage() {
        return R.drawable.enroll_air_touch_ffac;
    }

    @Override
    public int getEnrollDeviceName() {
        return R.string.airtouch_ffac_str;
    }

    @Override
    public int getEnrollChoiceDeivceImage() {
        return  R.drawable.ffac_click;
    }


}
