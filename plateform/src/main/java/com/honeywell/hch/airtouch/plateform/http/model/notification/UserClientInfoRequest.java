package com.honeywell.hch.airtouch.plateform.http.model.notification;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;

/**
 * Created by Vincent on 28/3/16.
 */
public class UserClientInfoRequest implements IRequestParams, Serializable {

    @SerializedName("userId")
    private String mUserId;

    @SerializedName("telephone")
    private String mPhone;

    @SerializedName("vendorID")
    private String mVendorId = "";

    @SerializedName("brand")
    private String mBrand = "";

    @SerializedName("model")
    private String mModel = "";

    @SerializedName("osType")
    private int mOsType ; //0:ios,1:android

    @SerializedName("osVersion")
    private String mOsVersion = "";

    @SerializedName("osLanguage")
    private String mLanguage;

    @SerializedName("communicationOperator")
    private String mCommunicationOperator = "";

    @SerializedName("uniqueCode")
    private String mUniqueCode;

    @SerializedName("comment")
    private String mComment = "";

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getmLanguage() {
        return mLanguage;
    }

    public void setmLanguage(String mLanguage) {
        this.mLanguage = mLanguage;
    }

    public String getmUniqueCode() {
        return mUniqueCode;
    }

    public void setmUniqueCode(String mUniqueCode) {
        this.mUniqueCode = mUniqueCode;
    }

    @Override
    public String getRequest(Gson gson) {
        return gson.toJson(this);
    }

    @Override
    public String getPrintableRequest(Gson gson) {
        return getRequest(gson);
    }

    public UserClientInfoRequest() {}

    public UserClientInfoRequest(String mUserId, String mPhone, int mOsType, String mLanguage, String mUniqueCode) {
        this.mUserId = mUserId;
        this.mPhone = mPhone;
        this.mOsType = mOsType;
        this.mLanguage = mLanguage;
        this.mUniqueCode = mUniqueCode;
    }
}
