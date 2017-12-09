package com.honeywell.hch.airtouch.plateform.http.model.user.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;

/**
 * Created by Jin Qian on 7/2/2015.
 */
public class EmotionBottleResponse implements IRequestParams, Serializable {
    public static final String EMOTION_RESP_DATA = "emotion_resp_data";

    @SerializedName("emotionalType")
    private int mEmtoionType;

    @SerializedName("cleanDust")
    private float mCleanDust;

    @SerializedName("paHs")
    private float mPahs;

    @SerializedName("heavyMetal")
    private float mHeavyMetal;

    @SerializedName("outFlowVolume")
    private float mOutflowVolume;

    @SerializedName("makeWaterTime")
    private int mMakeWaterTime;

    public float getCleanDust() {
        return mCleanDust;
    }

    public float getPahs() {
        return mPahs;
    }

    public float getHeavyMetal() {
        return mHeavyMetal;
    }

    public void setCleanDust(float mCleanDust) {
        this.mCleanDust = mCleanDust;
    }

    public void setPahs(float mPahs) {
        this.mPahs = mPahs;
    }

    public void setHeavyMetal(float mHeavyMetal) {
        this.mHeavyMetal = mHeavyMetal;
    }

    public float getOutflowVolume() {
        return mOutflowVolume;
    }

    public void setOutflowVolume(float mOutflowVolume) {
        this.mOutflowVolume = mOutflowVolume;
    }

    public int getMakeWaterTime() {
        return mMakeWaterTime;
    }

    public void setMakeWaterTime(int mMakeWaterTime) {
        this.mMakeWaterTime = mMakeWaterTime;
    }

    public int getEmtoionType() {
        return mEmtoionType;
    }

    public void setEmtoionType(int mEmtoionType) {
        this.mEmtoionType = mEmtoionType;
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
