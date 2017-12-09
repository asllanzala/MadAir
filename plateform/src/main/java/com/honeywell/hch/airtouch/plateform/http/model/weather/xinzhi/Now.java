package com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by e573227 on 2/2/2015.
 */
public class Now implements Serializable {
    @SerializedName("text")
    private String mText;

    @SerializedName("code")
    private int mCode;

    @SerializedName("temperature")
    private int mTemperature;

    @SerializedName("air_quality")
    private AirQuality mAirQuality;

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        mCode = code;
    }

    public int getTemperature() {
        return mTemperature;
    }

    public void setTemperature(int temperature) {
        mTemperature = temperature;
    }

    public AirQuality getAirQuality() {
        return mAirQuality;
    }

    public void setAirQuality(AirQuality airQuality) {
        mAirQuality = airQuality;
    }

}
