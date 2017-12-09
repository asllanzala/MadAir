package com.honeywell.hch.airtouch.plateform.http.model.notification;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Vincent on 23/3/16.
 */
public class PushMessageModel implements  Serializable {
    public static final String PUSHPARAMETER = "pushmessageparameter";
    public static final String PUSHPARAMETERUPDATE = "pushmessageparameterupdate";


    @SerializedName("aps")
    private PushAlertModel mBaiduPushAler;

    @SerializedName("messageType")
    private int mMessageType;

    @SerializedName("messageId")
    private int mMessageId;

    @SerializedName("locationId")
    private int mLocationId;

    @SerializedName("deviceId")
    private int mDeviceId;

    public PushAlertModel getmBaiduPushAlert() {
        return mBaiduPushAler;
    }

    public void setmBaiduPushAlert(PushAlertModel mBaiduPushAlertList) {
        this.mBaiduPushAler = mBaiduPushAlertList;
    }

    public int getmMessageType() {
        return mMessageType;
    }

    public void setmMessageType(int mMessageType) {
        this.mMessageType = mMessageType;
    }

    public int getmMessageId() {
        return mMessageId;
    }

    public void setmMessageId(int mMessageId) {
        this.mMessageId = mMessageId;
    }

    public int getmLocationId() {
        return mLocationId;
    }

    public void setmLocationId(int mLocationId) {
        this.mLocationId = mLocationId;
    }

    public int getmDeviceId() {
        return mDeviceId;
    }

    public void setmDeviceId(int mDeviceId) {
        this.mDeviceId = mDeviceId;
    }
}

