package com.honeywell.hch.airtouch.plateform.http.model.emotion;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Vincent on 9/2/17.
 */

public class GetVolumeAndTdsResponse implements Serializable {
    public static final String GET_VOLUME_AND_TDS_PARAMETER = "get_volume_and_tds_parameter";

    @SerializedName("ymd")
    private String mDate = "";

    @SerializedName("outflowVolume")
    private float mOutflowVolume;

    @SerializedName("maxInflowTDS")
    private float mMaxInflowTDS;

    @SerializedName("minInflowTDS")
    private float mMinInflowTDS;

    @SerializedName("avgInflowTDS")
    private float mAvgInflowTDS;

    @SerializedName("maxOutflowTDS")
    private float mMaxOutflowTDS;

    @SerializedName("minOutflowTDS")
    private float mMinOutflowTDS;

    @SerializedName("avgOutflowTDS")
    private float mAvgOutflowTDS;


    public String getmDate() {
        return mDate;
    }

    public float getmOutflowVolume() {
        return mOutflowVolume;
    }

    public float getmAvgInflowTDS() {
        return mAvgInflowTDS;
    }

    public float getmAvgOutflowTDS() {
        return mAvgOutflowTDS;
    }
}
