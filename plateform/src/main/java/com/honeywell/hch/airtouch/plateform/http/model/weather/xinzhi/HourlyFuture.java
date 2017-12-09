package com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lynnliu on 10/17/15.
 */
public class HourlyFuture implements Serializable {

    @SerializedName("status")
    private String mStatus;

    @SerializedName("hourly")
    private FutureHour[] mHours;

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public FutureHour[] getHours() {
        return mHours;
    }

    public void setHours(FutureHour[] hours) {
        mHours = hours;
    }
}
