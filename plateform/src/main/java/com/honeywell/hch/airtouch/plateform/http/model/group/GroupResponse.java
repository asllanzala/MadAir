package com.honeywell.hch.airtouch.plateform.http.model.group;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Qian Jin on 10/14/15.
 * get group by group id
 */
public class GroupResponse {
    public static final String CODE_ID = "code_id";

    @SerializedName("messgage")
    private String mMessage;

    @SerializedName("code")
    private int mCode;

    public String getMessage() {
        return mMessage;
    }

    public int getCode() {
        return mCode;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public void setCode(int code) {
        mCode = code;
    }
}
