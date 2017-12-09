package com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Qian Jin on 2/14/16.
 */
public class RemoveAuthResponse implements Serializable {
    public static final String REMOVE_AUTH_DATA = "auth_remove_data";

    @SerializedName("message")
    private String mMessage;

    @SerializedName("code")
    private String mCode;

    public String getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public void setCode(String code) {
        mCode = code;
    }
}
