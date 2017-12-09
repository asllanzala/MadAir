package com.honeywell.hch.airtouch.plateform.http.model.device;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;

/**
 * Created by wuyuan on 4/27/16.
 */
public class SmartROFilterInfo implements IRequestParams, Serializable {

    @SerializedName("filterType")
    private int mFilterType;

    @SerializedName("usageTime")
    private int mUserTime;

    @SerializedName("usageVolume")
    private int mUsageVolume;

    @SerializedName("timeCapabilitySetByUser")
    private int mTimeCapabilitySetByUser;

    @SerializedName("volumeCapabilitySetByUser")
    private int mVolumeCapabilitySetByUser;

    @SerializedName("resetTime")
    private String mRestTime;


    public int getFilterType() {
        return mFilterType;
    }

    public void setFilterType(int mFilterType) {
        this.mFilterType = mFilterType;
    }

    public int getUserTime() {
        return mUserTime;
    }

    public void setUserTime(int mUserTime) {
        this.mUserTime = mUserTime;
    }

    public int getUsageVolume() {
        return mUsageVolume;
    }

    public void setUsageVolume(int mUsageVolume) {
        this.mUsageVolume = mUsageVolume;
    }

    public int getTimeCapabilitySetByUser() {
        return mTimeCapabilitySetByUser;
    }

    public void setTimeCapabilitySetByUser(int mTimeCapabilitySetByUser) {
        this.mTimeCapabilitySetByUser = mTimeCapabilitySetByUser;
    }

    public int getVolumeCapabilitySetByUser() {
        return mVolumeCapabilitySetByUser;
    }

    public void setVolumeCapabilitySetByUser(int mVolumeCapabilitySetByUser) {
        this.mVolumeCapabilitySetByUser = mVolumeCapabilitySetByUser;
    }

    public String getRestTime() {
        return mRestTime;
    }

    public void setRestTime(String mRestTime) {
        this.mRestTime = mRestTime;
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
