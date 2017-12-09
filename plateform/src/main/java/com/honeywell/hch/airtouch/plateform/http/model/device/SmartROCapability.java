package com.honeywell.hch.airtouch.plateform.http.model.device;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.plateform.http.model.filter.EveryFilterCapabilityDetail;

import java.io.Serializable;
import java.util.List;


public class SmartROCapability implements IRequestParams, Serializable {

    @SerializedName("filters")
    private List<EveryFilterCapabilityDetail> mEveryFilterCapabilityDetailList;


    public List<EveryFilterCapabilityDetail> getmEveryFilterCapabilityDetailList() {
        return mEveryFilterCapabilityDetailList;
    }

    public void setmEveryFilterCapabilityDetailList(List<EveryFilterCapabilityDetail> mEveryFilterCapabilityDetailList) {
        this.mEveryFilterCapabilityDetailList = mEveryFilterCapabilityDetailList;
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
