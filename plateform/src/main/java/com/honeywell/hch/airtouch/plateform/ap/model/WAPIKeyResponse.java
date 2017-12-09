package com.honeywell.hch.airtouch.plateform.ap.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WAPIKeyResponse implements Serializable {

    private static final long serialVersionUID = 2360989918177959925L;

    @SerializedName("Modulus")
    private String mModulus;

    @SerializedName("Exponent")
    private String mExponent;

    public String getModulus() {
        return mModulus;
    }

    public void setModulus(String modulus) {
        mModulus = modulus;
    }

    public String getExponent() {
        return mExponent;
    }

    public void setExponent(String exponent) {
        mExponent = exponent;
    }
}
