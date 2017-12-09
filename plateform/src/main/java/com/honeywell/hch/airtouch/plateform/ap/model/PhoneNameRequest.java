package com.honeywell.hch.airtouch.plateform.ap.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

public class PhoneNameRequest implements IRequestParams {

    @SerializedName("PhoneName")
    private String mPhoneName;

    public PhoneNameRequest(String phoneName) {
        mPhoneName = phoneName;
    }

    public String getPhoneName() {
        return mPhoneName;
    }

    public void setPhoneName(String phoneName) {
        mPhoneName = phoneName;
    }

    @Override
    public String getRequest(Gson gson) {
        return gson.toJson(this);
    }

    @Override
    public String getPrintableRequest(Gson gson) {
        return getRequest(gson);
    }
}
