package com.honeywell.hch.airtouch.plateform.http.model.authorize.model;

import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.protocol.AuthorizeProtocol;

import java.io.Serializable;

/**
 * Created by Qian Jin on 2/1/16.
 */
public class AuthDeviceModel extends AuthBaseModel implements Serializable, AuthorizeProtocol {

    @SerializedName("deviceName")
    private String mDeviceName;

    @SerializedName("deviceId")
    private int mDeviceId;

    @SerializedName("deviceType")
    private int mDeviceType;

    public String getmDeviceName() {
        return mDeviceName;
    }

    public void setmDeviceName(String mDeviceName) {
        this.mDeviceName = mDeviceName;
    }

    public int getmDeviceId() {
        return mDeviceId;
    }

    public void setmDeviceId(int mDeviceId) {
        this.mDeviceId = mDeviceId;
    }

    public int getmDeviceType() {
        return mDeviceType;
    }

    public void setmDeviceType(int mDeviceType) {
        this.mDeviceType = mDeviceType;
    }

    @Override
    public String getModelName() {
        if (!DeviceType.isHplusSeries(mDeviceType)){
            setmDeviceName(AppManager.getInstance().getApplication().getString(com.honeywell.hch.airtouch.plateform.R.string.all_device_unsupport_device));
        }
        return this.mDeviceName;
    }

    @Override
    public int getModelId() {
        return this.mDeviceId;
    }

    @Override
    public int getModelIcon() {
        if (DeviceType.isAirTouchSeries(mDeviceType)) {
            return com.honeywell.hch.airtouch.plateform.R.drawable.all_device_air_icon;
        } else if (DeviceType.isWaterSeries(mDeviceType)) {
            return com.honeywell.hch.airtouch.plateform.R.drawable.all_device_ro_icon;
        }
        return com.honeywell.hch.airtouch.plateform.R.drawable.unknow_device;
    }

    public void setModleName(String name) {
        mDeviceName = name;
    }

    public void setModelId(int modelId) {
        mDeviceId = modelId;
    }


}