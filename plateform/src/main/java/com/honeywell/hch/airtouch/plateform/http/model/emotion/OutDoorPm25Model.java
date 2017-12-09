package com.honeywell.hch.airtouch.plateform.http.model.emotion;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Vincent on 9/2/17.
 */

public class OutDoorPm25Model implements Serializable {

    @SerializedName("ymd")
    private String mDate;

    @SerializedName("maxOutPM25")
    private float mMaxOutPM25 = -1;

    @SerializedName("minOutPM25")
    private float mMinOutPM25;

    @SerializedName("avgOutPM25")
    private float mAvgOutPM25;

    public String getmDate() {
        return mDate;
    }

    public float getmAvgOutPM25() {
        return mAvgOutPM25;
    }

}
