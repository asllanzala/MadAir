package com.honeywell.hch.airtouch.plateform.http.model.device;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;

/**
 * Created by wuyuan on 4/27/16.
 */
public class DeviceRunStatus  implements IRequestParams, Serializable {

    @SerializedName("deviceType")
    private int mDeviceType;

    @SerializedName("deviceID")
    private int mDeviceID;

    @SerializedName("runStatus")
    private JsonObject mDeviceRunStatus;

    public int getDeviceType() {
        return mDeviceType;
    }

    public void setDeviceType(int mDeviceType) {
        this.mDeviceType = mDeviceType;
    }

    public int getDeviceID() {
        return mDeviceID;
    }

    public void setDeviceID(int mDeviceID) {
        this.mDeviceID = mDeviceID;
    }

    public JsonObject getDeviceRunStatus() {
        return mDeviceRunStatus;
    }

    public void setDeviceRunStatus(JsonObject mDeviceRunStatus) {
        this.mDeviceRunStatus = mDeviceRunStatus;
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
