package com.honeywell.hch.airtouch.plateform.http.manager.model;

/**
 * Created by Qian Jin on 8/14/15.
 */
public class HomeAndCity {
    private String mHomeName;
    private int mLocationId;
    private String mHomeCity;
    private boolean isOwnerHome = true;
    private String mLocationOwnerName = "";
    private boolean isExpand = false;
    private boolean isDefaultHome = false;
    private boolean isHasUnnormalDevice = false;
    private boolean isRealHome = true;

    public HomeAndCity(String homeName, int locationId, String homeCity,boolean isRealHome) {
        mHomeName = homeName;
        mLocationId = locationId;
        mHomeCity = homeCity;
        this.isRealHome = isRealHome;
    }

    public boolean isDefaultHome() {
        return isDefaultHome;
    }

    public void setIsDefaultHome(boolean isDefaultHome) {
        this.isDefaultHome = isDefaultHome;
    }

    public boolean isOwnerHome() {
        return isOwnerHome;
    }

    public void setIsOwnerHome(boolean isOwnerHome) {
        this.isOwnerHome = isOwnerHome;
    }

    public String getHomeName() {
        return mHomeName;
    }

    public void setmHomeName(String mHomeName) {
        this.mHomeName = mHomeName;
    }

    public int getLocationId() {
        return mLocationId;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setIsExpand(boolean isExpand) {
        this.isExpand = isExpand;
    }

    public String getHomeCity() {
        return mHomeCity;
    }

    public String getmLocationOwnerName() {
        return mLocationOwnerName;
    }

    public void setmLocationOwnerName(String mLocationOwnerName) {
        this.mLocationOwnerName = mLocationOwnerName;
    }

    public boolean isHasUnnormalDevice() {
        return isHasUnnormalDevice;
    }

    public void setHasUnnormalDevice(boolean hasUnnormalDevice) {
        isHasUnnormalDevice = hasUnnormalDevice;
    }

    public boolean isRealHome() {
        return isRealHome;
    }

    public void setRealHome(boolean realHome) {
        isRealHome = realHome;
    }
}
