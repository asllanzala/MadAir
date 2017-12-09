package com.honeywell.hch.airtouch.plateform.http.model.multi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Qian Jin on 10/17/15.
 */
public class ScenarioMultiCommTaskResponse  {
    @SerializedName("deviceID")
    private int mDeviceId;

    @SerializedName("commonTaskID")
    private int mCommTaskId;

    @SerializedName("message")
    private String mMessage;

    public int getDeviceId() {
        return mDeviceId;
    }

    public String getMessage() {
        return mMessage;
    }

    public int getCommTaskId() {
        return mCommTaskId;
    }

}
