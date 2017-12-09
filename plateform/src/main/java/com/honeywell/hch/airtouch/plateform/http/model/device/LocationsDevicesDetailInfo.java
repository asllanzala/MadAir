package com.honeywell.hch.airtouch.plateform.http.model.device;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wuyuan on 4/27/16.
 */
public class LocationsDevicesDetailInfo implements IRequestParams, Serializable {

    @SerializedName("locationId")
    private int mLocationId;

    @SerializedName("deviceRunStatus")
    private List<DeviceRunStatus> mDeviceRunStatusList;


    public int getLocationId() {
        return mLocationId;
    }

    public void setLocationId(int mLocationId) {
        this.mLocationId = mLocationId;
    }

    public List<DeviceRunStatus> getDeviceRunStatusList() {
        return mDeviceRunStatusList;
    }

    public void setDeviceRunStatusList(List<DeviceRunStatus> mDeviceRunStatusList) {
        this.mDeviceRunStatusList = mDeviceRunStatusList;
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
