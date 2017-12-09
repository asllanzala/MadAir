package com.honeywell.hch.airtouch.plateform.devices.common;


import com.honeywell.hch.airtouch.library.util.LogUtil;

/**
 * Created by Qian Jin on 4/28/16.
 */
public class Filter {
    private CharSequence mName;
    private CharSequence mDescription;
    private String mPurchaseUrl;
    private float mMaxLife;
    private float mRuntimeLife;
    private int mWaterRunTimeLife;
    private int mDeviceType;
    private boolean UnAuthenticFilter;
    private int mFilterImage;

    public int getmFilterImage() {
        return mFilterImage;
    }

    public void setmFilterImage(int mFilterImage) {
        this.mFilterImage = mFilterImage;
    }

    public int getmDeviceType() {
        return mDeviceType;
    }

    public void setmDeviceType(int mDeviceType) {
        this.mDeviceType = mDeviceType;
    }

    public CharSequence getName() {
        return mName;
    }

    public void setName(CharSequence name) {
        mName = name;
    }

    public CharSequence getDescription() {
        return mDescription;
    }

    public void setDescription(CharSequence description) {
        mDescription = description;
    }

    public String getPurchaseUrl() {
        return mPurchaseUrl;
    }

    public void setPurchaseUrl(String purchaseUrl) {
        mPurchaseUrl = purchaseUrl;
    }

    public float getMaxLife() {
        return mMaxLife;
    }

    public void setMaxLife(int maxLife) {
        mMaxLife = maxLife;
    }

    public float getRuntimeLife() {
        return mRuntimeLife;
    }

    public void setRuntimeLife(int runtimeLife) {
        mRuntimeLife = runtimeLife;
    }

    public int getmWaterRunTimeLife() {
        return mWaterRunTimeLife;
    }

    public void setmWaterRunTimeLife(int mWaterRunTimeLife) {
        this.mWaterRunTimeLife = mWaterRunTimeLife;
    }

    public boolean isUnAuthenticFilter() {
        return UnAuthenticFilter;
    }

    public void setUnAuthenticFilter(boolean unAuthenticFilter) {
        UnAuthenticFilter = unAuthenticFilter;
    }

    public int getLifePercentage() {
        if (mRuntimeLife >= 0 && mMaxLife > 0) {
            int percentage = 100 - Math.round(mRuntimeLife * 100 / mMaxLife);
            return percentage >= 0 ? percentage : 0;
        }

        return 100;
    }

}
