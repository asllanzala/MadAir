package com.honeywell.hch.airtouch.plateform.config;


import com.honeywell.hch.airtouch.plateform.ap.WAPIRouter;
import com.honeywell.hch.airtouch.plateform.ap.model.WAPIDeviceResponse;
import com.honeywell.hch.airtouch.plateform.ap.model.WAPIKeyResponse;

import java.io.Serializable;

public class DIYInstallationState implements Serializable {

    private static final long serialVersionUID = -2083291746396398400L;

    private static WAPIKeyResponse mWAPIKeyResponse;
    private static WAPIDeviceResponse mWAPIDeviceResponse;
    private static WAPIRouter mWAPIRouter;
    private static String mDeviceName;
    private static String mHomeName;
    private static String mCityCode;
    private static int errorCode;
    private static String mHomeConnectedSsid;
    private static boolean isDeviceAlreadyEnrolled = false;
    private static int mDeviceType;

    private static String mSsid;

    private static int mLocationId;

    public static String getmHomeConnectedSsid() {
        return mHomeConnectedSsid;
    }

    public static void setmHomeConnectedSsid(String mHomeConnectedSsid) {
        DIYInstallationState.mHomeConnectedSsid = mHomeConnectedSsid;
    }

    public static String getCityCode() {
        return mCityCode;
    }

    public static void setCityCode(String cityCode) {
        DIYInstallationState.mCityCode = cityCode;
    }

    public static String getDeviceName() {
        return mDeviceName;
    }

    public static void setDeviceName(String deviceName) {
        DIYInstallationState.mDeviceName = deviceName;
    }

    public static String getHomeName() {
        return mHomeName;
    }

    public static void setHomeName(String mHomeName) {
        DIYInstallationState.mHomeName = mHomeName;
    }

    public static int getErrorCode() {
        return errorCode;
    }

    public static void setErrorCode(int errorCode) {
        DIYInstallationState.errorCode = errorCode;
    }

    public static WAPIKeyResponse getWAPIKeyResponse() {
        return mWAPIKeyResponse;
    }

    public static void setWAPIKeyResponse(WAPIKeyResponse wapiKeyResponse) {
        mWAPIKeyResponse = wapiKeyResponse;
    }

    public static WAPIDeviceResponse getWAPIDeviceResponse() {
        return mWAPIDeviceResponse;
    }

    public static void setWAPIDeviceResponse(WAPIDeviceResponse wapiDeviceResponse) {
        mWAPIDeviceResponse = wapiDeviceResponse;
    }

    public static WAPIRouter getWAPIRouter() {
        return mWAPIRouter;
    }

    public static void setWAPIRouter(WAPIRouter wapiRouter) {
        mWAPIRouter = wapiRouter;
    }

    public static boolean getIsDeviceAlreadyEnrolled() {
        return isDeviceAlreadyEnrolled;
    }

    public static void setIsDeviceAlreadyEnrolled(Boolean isDeviceAlreadyEnrolled) {
        DIYInstallationState.isDeviceAlreadyEnrolled = isDeviceAlreadyEnrolled;
    }

    public static int getDeviceType() {
        return mDeviceType;
    }

    public static void setDeviceType(int mDeviceType) {
        DIYInstallationState.mDeviceType = mDeviceType;
    }

    public static String getSsid() {
        return mSsid;
    }

    public static void setSsid(String mSsid) {
        DIYInstallationState.mSsid = mSsid;
    }

    public static int getLocationId() {
        return mLocationId;
    }

    public static void setLocationId(int locationId) {
        DIYInstallationState.mLocationId = locationId;
    }

    /**
     * 在每次进入enroll的开始界面，都进行reset操作，保证这个对象里的所有状态为初始状态
     */
    public static void reset(){
        mWAPIKeyResponse = null;
        mWAPIDeviceResponse = null;
        mWAPIRouter = null;
        mDeviceName = null;
        mHomeName = null;
        mCityCode = null;
        errorCode = 0;
        mHomeConnectedSsid = null;
        isDeviceAlreadyEnrolled = false;
        mDeviceType = 0;
        mSsid = null;
        mLocationId = 0;
    }
}