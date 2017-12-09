package com.honeywell.hch.airtouch.plateform.http.manager.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;

/**
 * Created by Jin Qian on 1/19/2015.
 *
 * 2015-7-31 changed by Stephen(H127856)
 * data model reconstruction
 */
public class DeviceInfo implements IRequestParams, Serializable {
    @SerializedName("deviceID")
    private int mDeviceID;

    @SerializedName("name")
    private String mName;

    @SerializedName("isUpgrading")
    private boolean mIsUpgrading;

    @SerializedName("isAlive")
    private boolean mIsAlive;

    @SerializedName("firmwareVersion")
    private String mFirmwareVersion;

    @SerializedName("macID")
    private String mMacID;

    @SerializedName("deviceType")
    private int mDeviceType;

    @SerializedName("permission")
    private int mPermission;

    @SerializedName("ownerName")
    private String mOwnerName;

    public int getDeviceID() {
        return mDeviceID;
    }

    public void setDeviceID(int deviceID) {
        mDeviceID = deviceID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean getIsAlive() {
        return mIsAlive;
    }

    public void setIsAlive(boolean isAlive) {
        mIsAlive = isAlive;
    }

    public String getFirmwareVersion() {
        return mFirmwareVersion;
    }

    public String getMacID() {
        return mMacID;
    }

    @Override
    public String getRequest(Gson gson) {
        return gson.toJson(this);
    }

    @Override
    public String getPrintableRequest(Gson gson) {
        return getRequest(gson);
    }

    public int getDeviceType() {
        return mDeviceType;
    }

    public void setDeviceType(int mDeviceType) {
        this.mDeviceType = mDeviceType;
    }

    public int getPermission() {
        return mPermission;
    }

    public void setPermission(int permission) {
        mPermission = permission;
    }

    public String getOwnerName() {
        return mOwnerName;
    }

    public void setOwnerName(String mOwnerName) {
        this.mOwnerName = mOwnerName;
    }

}


