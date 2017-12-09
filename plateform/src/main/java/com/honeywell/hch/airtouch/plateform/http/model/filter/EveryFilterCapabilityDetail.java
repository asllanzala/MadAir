package com.honeywell.hch.airtouch.plateform.http.model.filter;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;


public class EveryFilterCapabilityDetail implements IRequestParams, Serializable {

    /**
     * filterCategory:
     0 = None.
     1 = Pre-Filter(
     2 = RO Filter
     3 = Carbon Filter
     4~63 = Reserved
     */
    @SerializedName("filterCategory")
    private int mFilterCategory;

    @SerializedName("filterType")
    private String mFilterType;

    @SerializedName("minTimeSetting")
    private int mMinTimeSetting;

    @SerializedName("maxTimeSetting")
    private int mMaxTimeSetting;

    @SerializedName("minVolumeSetting")
    private int mMinVolumeSetting;

    @SerializedName("maxVolumeSetting")
    private int mMaxVolumeSetting;


    public String getmFilterType() {
        return mFilterType;
    }

    public void setmFilterType(String mFilterType) {
        this.mFilterType = mFilterType;
    }

    public int getmMinTimeSetting() {
        return mMinTimeSetting;
    }

    public void setmMinTimeSetting(int mMinTimeSetting) {
        this.mMinTimeSetting = mMinTimeSetting;
    }

    public int getmMaxTimeSetting() {
        return mMaxTimeSetting;
    }

    public void setmMaxTimeSetting(int mMaxTimeSetting) {
        this.mMaxTimeSetting = mMaxTimeSetting;
    }

    public int getmMinVolumeSetting() {
        return mMinVolumeSetting;
    }

    public void setmMinVolumeSetting(int mMinVolumeSetting) {
        this.mMinVolumeSetting = mMinVolumeSetting;
    }

    public int getmMaxVolumeSetting() {
        return mMaxVolumeSetting;
    }

    public void setmMaxVolumeSetting(int mMaxVolumeSetting) {
        this.mMaxVolumeSetting = mMaxVolumeSetting;
    }

    public int getFilterCategory() {
        return mFilterCategory;
    }

    public void setFilterCategory(int mFilterCategory) {
        this.mFilterCategory = mFilterCategory;
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
