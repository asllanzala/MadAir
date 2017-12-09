package com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Qian Jin on 2/14/16.
 */
public class HandleAuthMessageResponse implements  Serializable {
    public static final String AUTH_HANDLE_MESSAGE_DATA = "auth_handle_message_data";

    @SerializedName("id")
    private int mId;

    @SerializedName("message")
    private String mMessage;

    @SerializedName("code")
    private String mCode;

    public int getId() {
        return mId;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getCode() {
        return mCode;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public void setCode(String code) {
        mCode = code;
    }
}
