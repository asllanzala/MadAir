package com.honeywell.hch.airtouch.plateform.http.model.group;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Qian Jin on 10/14/15.
 * get group by group id
 */
public class DevicesForGroupResponse implements Serializable {
    @SerializedName("deviceId")
    private int mDeviceId;

    @SerializedName("deviceName")
    private String mDeviceName;

    @SerializedName("deviceType")
    private double mDeviceType;

    @SerializedName("isAlive")
    private Boolean mIsAlive;

    @SerializedName("macId")
    private String mMacId;

    @SerializedName("isUpgrading")
    private boolean mIsUpgrading;

    @SerializedName("isMasterDevice")
    private int mIsMasterDevice;

    public int getDeviceId() {
        return mDeviceId;
    }

    public String getDeviceName() {
        return mDeviceName;
    }

    public double getDeviceType() {
        return mDeviceType;
    }

    public boolean getIsAlive() {
        return mIsAlive;
    }

    public String getMacId() {
        return mMacId;
    }

    public boolean getIsUpgrading() {
        return mIsUpgrading;
    }

    public int getIsMasterDevice() {
        return mIsMasterDevice;
    }

    public void setDeviceId(int deviceId) {
        mDeviceId = deviceId;
    }

    public void setDeviceName(String deviceName) {
        mDeviceName = deviceName;
    }

    public void setDeviceType(double deviceType) {
        mDeviceType = deviceType;
    }

    public void setIsAlive(Boolean isAlive) {
        mIsAlive = isAlive;
    }

    public void setMacId(String macId) {
        mMacId = macId;
    }

    public void setIsUpgrading(Boolean isUpgrading) {
        mIsUpgrading = isUpgrading;
    }

    public void setIsMasterDevice(int isMasterDevice) {
        mIsMasterDevice = isMasterDevice;
    }
}
