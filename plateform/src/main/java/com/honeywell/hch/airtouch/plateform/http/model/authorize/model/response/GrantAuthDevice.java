package com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Qian Jin on 2/14/16.
 */
public class GrantAuthDevice implements  Serializable {

    @SerializedName("id")
    private int mId;

    @SerializedName("telephone")
    private String mTelephone;

    @SerializedName("message")
    private String mMessage;

    @SerializedName("code")
    private int mCode;

    public int getId() {
        return mId;
    }

    public String getTelephone() {
        return mTelephone;
    }

    public String getMessage() {
        return mMessage;
    }

    public int getCode() {
        return mCode;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setTelephone(String telephone) {
        mTelephone = telephone;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public void setCode(int code) {
        mCode = code;
    }
}
