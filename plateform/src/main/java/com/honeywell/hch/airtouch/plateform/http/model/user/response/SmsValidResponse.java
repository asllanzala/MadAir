package com.honeywell.hch.airtouch.plateform.http.model.user.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jin Qian on 2/26/2015.
 */
public class SmsValidResponse {
    @SerializedName("isValid")
    private Boolean mIsValid;

    public Boolean isValid() {
        return mIsValid;
    }

    public void setIsValid(Boolean isValid) {
        mIsValid = isValid;
    }
}