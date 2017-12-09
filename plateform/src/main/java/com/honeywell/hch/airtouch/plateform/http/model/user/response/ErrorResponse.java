package com.honeywell.hch.airtouch.plateform.http.model.user.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Jin Qian on 1/23/2015.
 */
public class ErrorResponse implements Serializable {
    @SerializedName("code")
    private String mCode;

    @SerializedName("message")
    private String mMessage;

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }
}