package com.honeywell.hch.airtouch.plateform.ap.model;

import com.google.gson.annotations.SerializedName;

public class WAPIDeviceResponse {

    @SerializedName("MAC")
    private String mMacID;

    @SerializedName("CRC")
    private String mCrcID;


    public String getMacID() {
        return mMacID;
    }

    public void setMacID(String macID) {
        mMacID = macID;
    }

    public String getCrcID() {
        return mCrcID;
    }

    public void setCrcID(String crcID) {
        mCrcID = crcID;
    }

}
