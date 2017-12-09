package com.honeywell.hch.airtouch.plateform.http.model.notification;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Vincent on 23/3/16.
 */
public class PushAlertModel implements Serializable {
    @SerializedName("alert")
    private String mAlert;

    public String getmAlert() {
        return mAlert;
    }

    public void setmAlert(String mAlert) {
        this.mAlert = mAlert;
    }
}
