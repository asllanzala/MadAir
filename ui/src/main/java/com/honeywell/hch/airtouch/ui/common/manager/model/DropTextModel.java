package com.honeywell.hch.airtouch.ui.common.manager.model;

import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.database.model.City;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;

/**
 * Created by h127856 on 6/25/16.
 */
public class DropTextModel {
    private HomeDevice mHomeDevice;
    private City mCity;

    private String mTextViewString;

    private int mLeftImageId = 0;

    private String mRightTextViewString;

    /**
     * enroll时候，选择一个已有的家，需要记录当前的locationId
     */
    private int mLocationId;

    public DropTextModel(String textStr, int imageId) {
        mTextViewString = textStr;
        mLeftImageId = imageId;
    }

    public City getmCity() {
        return mCity;
    }

    public void setmCity(City mCity) {
        this.mCity = mCity;
    }

    public DropTextModel(String textStr) {
        mTextViewString = textStr;
    }

    public DropTextModel(String textStr,String cityName) {
        mTextViewString = textStr;
        mRightTextViewString = cityName;
    }

    public DropTextModel(HomeDevice homeDevice) {
        mHomeDevice = homeDevice;
    }

    public DropTextModel(City city) {
        mCity = city;
    }

    public String getTextViewString() {
        if (!StringUtil.isEmpty(mTextViewString)) {
            return mTextViewString;
        } else if (mCity != null) {
            return getCityName(mCity);
        }
        return mTextViewString;

    }

    public void setTextViewString(String textViewString) {
        this.mTextViewString = textViewString;
    }

    public int getLeftImageId() {
        return mLeftImageId;
    }

    public void setLeftImageId(int leftImageId) {
        this.mLeftImageId = leftImageId;
    }

    public String getCityName(City city) {
        return AppConfig.shareInstance().getLanguage().equals(AppConfig.LANGUAGE_ZH)
                ? city.getNameZh() : city.getNameEn();
    }

    public String getRightTextViewString() {
        return mRightTextViewString;
    }

    public void setRightTextViewString(String mRightTextViewString) {
        this.mRightTextViewString = mRightTextViewString;
    }

    public int getLocationId() {
        return mLocationId;
    }

    public void setLocationId(int mLocationId) {
        this.mLocationId = mLocationId;
    }

    public String getmTextViewString() {
        return mTextViewString;
    }

    public HomeDevice getmHomeDevice() {
        return mHomeDevice;
    }

    public void setmHomeDevice(HomeDevice mHomeDevice) {
        this.mHomeDevice = mHomeDevice;
    }
}
