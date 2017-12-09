package com.honeywell.hch.airtouch.plateform.http.model.user.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceInfo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jin Qian on 1/19/2015.
 * GET api/locations?userId={userId}
 * <p/>
 * 2015-7-31 changed by Stephen(H127856)
 * data model reconstruction
 */
public class UserLocation implements IRequestParams, Serializable {

    @SerializedName("devices")
    private ArrayList<DeviceInfo> mDeviceInfo;

    @SerializedName("locationID")
    private int mLocationID;

    @SerializedName("name")
    private String mName;

    // city code (i.e. CHSH00000)
    @SerializedName("city")
    private String mCity;

    @SerializedName("isLocationOwner")
    private Boolean mIsLocationOwner;

    @SerializedName("locationOwnerName")
    private Boolean mLocationOwnerName;

    @SerializedName("scenarioOperation")
    private int scenarioOperation;
    //Away(Off):0    Home(Auto):1;     Sleep(Sleep):2;    Awake(Awake):3;    No record:-1

    public int getLocationID() {
        return mLocationID;
    }

    public void setLocationID(int locationID) {
        mLocationID = locationID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Boolean getIsLocationOwner() {
        return mIsLocationOwner;
    }

    public void setIsLocationOwner(Boolean isLocationOwner) {
        mIsLocationOwner = isLocationOwner;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public ArrayList<DeviceInfo> getDeviceInfo() {
        return mDeviceInfo;
    }

    public void setDeviceInfo(ArrayList<DeviceInfo> deviceInfo) {
        mDeviceInfo = deviceInfo;
    }

    @Override
    public String getRequest(Gson gson) {
        return gson.toJson(this);
    }

    @Override
    public String getPrintableRequest(Gson gson) {
        return getRequest(gson);
    }

    public int getScenarioOperation() {
        return scenarioOperation;
    }

    public void setScenarioOperation(int scenarioOperation) {
        this.scenarioOperation = scenarioOperation;
    }
}


