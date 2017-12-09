package com.honeywell.hch.airtouch.plateform.http.model.device;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;

/**
 * Created by wuyuan on 4/27/16.
 */
public class DeviceTypeConfigInfo implements IRequestParams, Serializable {

    @SerializedName("deviceType")
    private int mDeviceType;

    @SerializedName("capability")
    private JsonObject mDeviceCapability;

    public int getmDeviceType() {
        return mDeviceType;
    }

    public void setmDeviceType(int mDeviceType) {
        this.mDeviceType = mDeviceType;
    }

    public JsonObject getmDeviceCapability() {
        return mDeviceCapability;
    }

    public void setmDeviceCapability(JsonObject mDeviceCapability) {
        this.mDeviceCapability = mDeviceCapability;
    }

    @Override
    public String getRequest(Gson gson) {
        return gson.toJson(this);
    }

    @Override
    public String getPrintableRequest(Gson gson) {
        return getRequest(gson);
    }
}
