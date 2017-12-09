package com.honeywell.hch.airtouch.plateform.devices.feature.controlable.madair;

import android.content.Context;

import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.devices.feature.controlable.IControlFeature;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;

/**
 * Created by h127856 on 16/10/27.
 */
public class MadAirCanControlableFeatureImpl implements IControlFeature {

    private Context mContext = AppManager.getInstance().getApplication();
    private MadAirDeviceModel mMadAirDeviceModel;

    public MadAirCanControlableFeatureImpl(MadAirDeviceModel madAirDeviceModel) {
        mMadAirDeviceModel = madAirDeviceModel;
    }

    @Override
    public boolean canRemoteControl() {
        return false;
    }


    @Override
    public int getAllDeviceStatusImage() {
        if (mMadAirDeviceModel != null) {
            switch (mMadAirDeviceModel.getDeviceStatus()) {
                case BLE_DISABLE:
                case DISCONNECT:
                    return R.drawable.disconnected_icon;
                case USING:
                    return R.drawable.using_icon;
                default:
                    return R.drawable.connected_icon;
            }
        }
        return R.drawable.disconnected_icon;
    }

    @Override
    public String getScenerioModeAction() {
        if (mMadAirDeviceModel != null) {
            switch (mMadAirDeviceModel.getDeviceStatus()) {
                case BLE_DISABLE:
                case DISCONNECT:
                    return mContext.getString(R.string.mad_air_disconnected);
                case USING:
                    return mContext.getString(R.string.mad_air_wearing);
                default:
                    return mContext.getString(R.string.mad_air_connected);
            }
        }
        return mContext.getString(R.string.mad_air_disconnected);
    }

    @Override
    public boolean isCanControlable() {
        return true;
    }

    @Override
    public int getAllDeviceBigImg() {
        if (mMadAirDeviceModel != null) {
            switch (mMadAirDeviceModel.getDeviceStatus()) {
                case DISCONNECT:
                    return R.drawable.mad_air_disconnected_big;
                default:
                    return R.drawable.mad_air_connected_big;
            }
        }
        return R.drawable.mad_air_disconnected_big;
    }
}
