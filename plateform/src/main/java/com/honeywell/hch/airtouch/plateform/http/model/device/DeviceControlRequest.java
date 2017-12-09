package com.honeywell.hch.airtouch.plateform.http.model.device;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;

/**
 * Created by Jin Qian on 1/16/2015.
 */
public class DeviceControlRequest implements IRequestParams, Serializable {
    @SerializedName("airCleanerFanModeSwitch")
    private String mFanModeSwitch;

    public DeviceControlRequest(String fanModeSwitch) {
        this.mFanModeSwitch = fanModeSwitch;
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
