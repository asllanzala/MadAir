package com.honeywell.hch.airtouch.plateform.ap.model;

import com.google.gson.annotations.SerializedName;

public class WAPIConnectResponse {

    @SerializedName("Success")
    private boolean mSuccess;

    @SerializedName("Message")
    private String mMessage;

    public boolean isSuccess() {
        return mSuccess;
    }

    public void setSuccess(boolean success) {
        mSuccess = success;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }
}
