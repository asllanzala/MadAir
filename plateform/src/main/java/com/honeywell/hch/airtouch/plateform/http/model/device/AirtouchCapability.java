package com.honeywell.hch.airtouch.plateform.http.model.device;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;


public class AirtouchCapability implements IRequestParams, Serializable {
    @SerializedName("filter1ExpiredTime")
    private int mFilter1ExpiredTime;

    @SerializedName("filter2ExpiredTime")
    private int mFilter2ExpiredTime;

    @SerializedName("filter3ExpiredTime")
    private int mFilter3ExpiredTime;

    public int getFilter1ExpiredTime() {
        return mFilter1ExpiredTime;
    }

    public void setFilter1ExpiredTime(int filter1ExpiredTime) {
        mFilter1ExpiredTime = filter1ExpiredTime;
    }

    public int getFilter2ExpiredTime() {
        return mFilter2ExpiredTime;
    }

    public void setFilter2ExpiredTime(int filter2ExpiredTime) {
        mFilter2ExpiredTime = filter2ExpiredTime;
    }

    public int getFilter3ExpiredTime() {
        return mFilter3ExpiredTime;
    }

    public void setFilter3ExpiredTime(int filter3ExpiredTime) {
        mFilter3ExpiredTime = filter3ExpiredTime;
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
