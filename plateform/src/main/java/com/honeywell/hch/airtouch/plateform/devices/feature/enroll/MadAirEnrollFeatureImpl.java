package com.honeywell.hch.airtouch.plateform.devices.feature.enroll;


import android.content.Context;
import android.widget.ImageView;

import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;

/**
 * Created by Qian Jin on 3/28/16.
 */
public class MadAirEnrollFeatureImpl implements IEnrollFeature {

    private Context mContext = AppManager.getInstance().getApplication();
    private String mModel = HPlusConstants.MAD_AIR_MODEL_WHITE;
    public MadAirEnrollFeatureImpl(){}

    public MadAirEnrollFeatureImpl(String model){
        mModel = model;
    }

    @Override
    public void setImageDrawable(ImageView deviceImageView) {
    }

    @Override
    public String getEnrollDeviceWifiPre() {
        return HPlusConstants.AIR_TOUCH_P_SSID;
    }

    @Override
    public int getEnrollDeviceImage() {
        switch (mModel) {
            case HPlusConstants.MAD_AIR_MODEL_WHITE:
                return R.drawable.mad_air_white_big;
            case HPlusConstants.MAD_AIR_MODEL_BLACK:
                return R.drawable.mad_air_black_big;
            case HPlusConstants.MAD_AIR_MODEL_PINK:
                return R.drawable.mad_air_pink_big;
            case HPlusConstants.MAD_AIR_MODEL_SKULL:
                return R.drawable.mad_air_skull_big;
            default:
                return R.drawable.mad_air_white_big;
        }


    }

    @Override
    public int getEnrollDeviceName() {
        return R.string.mad_air_enroll_name_device;
    }

    //MadAIr 四种类型enroll picture
    @Override
    public int getEnrollChoiceDeivceImage() {
        switch (mModel) {
            case HPlusConstants.MAD_AIR_MODEL_WHITE:
                return R.drawable.madair_default;
            case HPlusConstants.MAD_AIR_MODEL_BLACK:
                return R.drawable.madair_black;
            case HPlusConstants.MAD_AIR_MODEL_PINK:
                return R.drawable.madair_pink;
            case HPlusConstants.MAD_AIR_MODEL_SKULL:
                return R.drawable.madair_skull;
            default:
                return R.drawable.madair_default;
        }
    }



}
