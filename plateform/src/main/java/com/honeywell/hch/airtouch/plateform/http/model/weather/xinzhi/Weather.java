package com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jin Qian on 2/2/2015.
 */
public class Weather implements Serializable {
    @SerializedName("city_name")
    private String mCityName;

    @SerializedName("city_id")
    private String mCityID;

    @SerializedName("last_update")
    private String mLastUpdate;

    @SerializedName("now")
    private Now mNow;

    @SerializedName("future")
    private List<Future> mFutureList;

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        mCityName = cityName;
    }

    public String getCityID() {
        return mCityID;
    }

    public void setCityID(String cityID) {
        mCityID = cityID;
    }

    public String getLastUpdate() {
        return mLastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        mLastUpdate = lastUpdate;
    }

    public Now getNow() {
        return mNow;
    }

    public void setNow(Now now) {
        mNow = now;
    }

    public List<Future> getFutureList() {
        return mFutureList;
    }

    public void setFutureList(List<Future> futureList) {
        mFutureList = futureList;
    }


    public boolean equalsWeatherFutureList(Weather other){
        if (other == null || other.getFutureList() == null ||  this.getFutureList() == null
                || this.getFutureList().size() != other.getFutureList().size()){
            return false;
        }

        for (int i =0 ;i < getFutureList().size();i++){
            if ((getFutureList().get(i) == null && other.getFutureList().get(i) != null) ||
                    (getFutureList().get(i) != null && other.getFutureList().get(i) == null)){
                return false;
            }
            else if (getFutureList().get(i) == null && other.getFutureList().get(i) == null){
                continue;
            }
            else if (!getFutureList().get(i).equals(other.getFutureList().get(i))){
                return false;
            }
        }
        return true;
    }
}
