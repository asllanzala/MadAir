package com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lynnliu on 10/15/15.
 */
public class Future implements Serializable {

    @SerializedName("date")
    private String mDate;

    @SerializedName("day")
    private String mDay;

    @SerializedName("text")
    private String mText;

    @SerializedName("code1")
    private int mCode1;

    @SerializedName("code2")
    private int mCode2;

    @SerializedName("high")
    private int mHigh;

    @SerializedName("low")
    private int mLow;

    @SerializedName("cop")
    private String mCop;

    @SerializedName("wind")
    private String mWind;

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getDay() {
        return mDay;
    }

    public void setDay(String day) {
        mDay = day;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public int getCode1() {
        return mCode1;
    }

    public void setCode1(int code1) {
        mCode1 = code1;
    }

    public int getCode2() {
        return mCode2;
    }

    public void setCode2(int code2) {
        mCode2 = code2;
    }

    public int getHigh() {
        return mHigh;
    }

    public void setHigh(int high) {
        mHigh = high;
    }

    public int getLow() {
        return mLow;
    }

    public void setLow(int low) {
        mLow = low;
    }

    public String getCop() {
        return mCop;
    }

    public void setCop(String cop) {
        mCop = cop;
    }

    public String getWind() {
        return mWind;
    }

    public void setWind(String wind) {
        mWind = wind;
    }

    public boolean equals(Future other){
        if (other == null){
            return false;
        }
        if (this.mDate.equals(other.mDate)
                && this.mDay.equals(other.mDay) && this.mText.equals(other.mText)
                && this.mCode1 == other.mCode1 && this.mCode2 == other.mCode2
                && this.mHigh == other.mHigh && this.mLow == other.mLow
                && this.mCop.equals(other.mCop) && this.mWind.equals(other.mWind)){
            return true;
        }
        return false;
    }
}
