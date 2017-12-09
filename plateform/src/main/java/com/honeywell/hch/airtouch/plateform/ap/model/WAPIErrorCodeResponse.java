package com.honeywell.hch.airtouch.plateform.ap.model;

import com.google.gson.annotations.SerializedName;

public class WAPIErrorCodeResponse {

    @SerializedName("error")
    private int mError;

    public int getError() {
        return mError;
    }

    public void setError(int error) {
        mError = error;
    }
}
