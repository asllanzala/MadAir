package com.honeywell.hch.airtouch.plateform.http.model.user.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jin Qian on 2/26/2015.
 */
public class SmsSendResponse  {
    @SerializedName("isSend")
    private Boolean mIsSend;

    public Boolean isSend() {
        return mIsSend;
    }

    public void setIsSend(Boolean isSend) {
        mIsSend = isSend;
    }
}