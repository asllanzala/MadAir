package com.honeywell.hch.airtouch.plateform.devices.feature.enroll;


import android.content.Context;
import android.widget.ImageView;

import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;

/**
 * Created by Vincent on 17/8/16.
 */
public class AirTouchXCompactEnrollFeatureImpl implements IEnrollFeature {

    protected Context mContext = AppManager.getInstance().getApplication();

    @Override
    public void setImageDrawable(ImageView deviceImageView) {
        deviceImageView.setImageDrawable(mContext.getResources()
                .getDrawable(R.drawable.all_device_airtouch_x));
    }

    @Override
    public String getEnrollDeviceWifiPre() {
        return HPlusConstants.AIR_TOUCH_XCOMPACT_SSID;
    }

    @Override
    public int getEnrollDeviceImage() {
        return R.drawable.enroll_air_touch_x_compact;
    }

    @Override
    public int getEnrollDeviceName() {
        return R.string.airtouch_xcompact_str;
    }



    @Override
    public int getEnrollChoiceDeivceImage() {
        return R.drawable.air_touch_x_compact_click;
    }


}
