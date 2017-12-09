package com.honeywell.hch.airtouch.plateform.location.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Qian Jin on 12/3/15.
 */
public class AddressComponents implements  Serializable {

    @SerializedName("long_name")
    private String mLongName;

    @SerializedName("short_name")
    private String mShortName;

    @SerializedName("types")
    private List<String> mTypes;

    public String getLongName() {
        return mLongName;
    }

    public String getShortName() {
        return mShortName;
    }

    public List<String> getTypes() {
        return mTypes;
    }
}
