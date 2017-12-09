package com.honeywell.hch.airtouch.plateform.http.model.multicommtask;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Qian Jin on 10/14/15.
 * get group by group id
 */
public class MultiCommTaskResponse implements  Serializable {
    @SerializedName("state")
    private String mState;

    @SerializedName("faultReason")
    private String mFaultReason;

    @SerializedName("commTaskId")
    private int mCommTaskId;

    public String getState() {
        return mState;
    }

    public String getFaultReason() {
        return mFaultReason;
    }

    public int getCommTaskId() {
        return mCommTaskId;
    }

    public MultiCommTaskResponse(String mState, String mFaultReason, int mCommTaskId) {
        this.mState = mState;
        this.mFaultReason = mFaultReason;
        this.mCommTaskId = mCommTaskId;
    }
}
