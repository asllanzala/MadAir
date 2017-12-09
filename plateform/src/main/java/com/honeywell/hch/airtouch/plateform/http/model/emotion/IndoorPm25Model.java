package com.honeywell.hch.airtouch.plateform.http.model.emotion;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Vincent on 9/2/17.
 */

public class IndoorPm25Model implements Serializable {

    @SerializedName("ymd")
    private String mDate;

    @SerializedName("cleanDust")
    private float mCleanDust = -1;

    @SerializedName("maxInPM25")
    private float mMaxInPM25;

    @SerializedName("minInPM25")
    private float mMinInPM25 = -1;

    @SerializedName("avgInPM25")
    private float mAvgInPM25;

    public String getmDate() {
        return mDate;
    }

    public float getmCleanDust() {
        return mCleanDust;
    }

    public float getmAvgInPM25() {
        return mAvgInPM25;
    }
}
