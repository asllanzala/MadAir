package com.honeywell.hch.airtouch.plateform.http.model.device;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;

/**
 * Created by Vincent on 6/5/16.
 */
public class WaterDeviceControlRequest implements IRequestParams, Serializable {
    @SerializedName("senario")
    private int mSenario;  //1--home,2--away

    public WaterDeviceControlRequest(int senario) {
        this.mSenario = senario;
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
