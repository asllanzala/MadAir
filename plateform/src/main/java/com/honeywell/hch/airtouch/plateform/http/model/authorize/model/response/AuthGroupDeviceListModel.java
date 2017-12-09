package com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response;

import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthDeviceModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Vincent on 12/4/16.
 */
public class AuthGroupDeviceListModel implements Serializable {
    public static final String AUTH_GROUP_DEVICE_LIST_DATA = "auth_group_device_list_data";


    @SerializedName("groupName")
    private String mGroupName;

    @SerializedName("groupId")
    private int mGroupId;

    @SerializedName("locationName")
    private String mLocationName;

    @SerializedName("locationId")
    private int mLocationId;

    @SerializedName("isLocationOwner")
    private boolean mIsLocatonOwner;

    @SerializedName("deviceList")
    private List<AuthDeviceModel> mAuthDevices;

    public static String getAuthGroupDeviceListData() {
        return AUTH_GROUP_DEVICE_LIST_DATA;
    }

    public String getmGroupName() {
        return mGroupName;
    }

    public void setmGroupName(String mGroupName) {
        this.mGroupName = mGroupName;
    }

    public int getmGroupId() {
        return mGroupId;
    }

    public void setmGroupId(int mGroupId) {
        this.mGroupId = mGroupId;
    }

    public String getmLocationName() {
        return mLocationName;
    }

    public void setmLocationName(String mLocationName) {
        this.mLocationName = mLocationName;
    }

    public int getmLocationId() {
        return mLocationId;
    }

    public void setmLocationId(int mLocationId) {
        this.mLocationId = mLocationId;
    }

    public boolean ismIsLocatonOwner() {
        return mIsLocatonOwner;
    }

    public void setmIsLocatonOwner(boolean mIsLocatonOwner) {
        this.mIsLocatonOwner = mIsLocatonOwner;
    }

    public List<AuthDeviceModel> getmAuthDevices() {
        return mAuthDevices;
    }

    public void setmAuthDevices(List<AuthDeviceModel> mAuthDevices) {
        this.mAuthDevices = mAuthDevices;
    }
}
