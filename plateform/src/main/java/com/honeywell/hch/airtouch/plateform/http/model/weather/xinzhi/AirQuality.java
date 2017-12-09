package com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;

/**
 * Created by Jin Qian on 2/2/2015.
 */
public class AirQuality implements IRequestParams, Serializable {
    @SerializedName("city")
    private AirQualityIndex mAirQualityIndex;

    public AirQualityIndex getAirQualityIndex() {
        return mAirQualityIndex;
    }

    public void setAirQualityIndex(AirQualityIndex airQualityIndex) {
        mAirQualityIndex = airQualityIndex;
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
