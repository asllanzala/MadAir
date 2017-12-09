package com.honeywell.hch.airtouch.plateform.http.model.enroll;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Vincent on 2/6/16.
 */
public class EnrollDeviceTypeModel implements  Serializable {
    public static final String ENROLL_DEVICE_TYPE_DATA = "enroll_device_type";
    @SerializedName("type")
    private int mType;

    @SerializedName("name")
    private String mName;

    @SerializedName("models")
    private List<String> mModels;

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public List<String> getmModels() {
        return mModels;
    }

    public void setmModels(List<String> mModels) {
        this.mModels = mModels;
    }
}
