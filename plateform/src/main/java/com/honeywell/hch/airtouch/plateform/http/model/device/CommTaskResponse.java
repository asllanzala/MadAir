package com.honeywell.hch.airtouch.plateform.http.model.device;

import com.google.gson.annotations.SerializedName;

/**
 * Created by E573227 on 2/8/2015.
 */
public class CommTaskResponse{
    @SerializedName("state")
    private String mState;

    @SerializedName("faultReason")
    private String mFaultReason;

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public String getFaultReason() {
        return mFaultReason;
    }

    public void setFaultReason(String faultReason) {
        mFaultReason = faultReason;
    }
}
