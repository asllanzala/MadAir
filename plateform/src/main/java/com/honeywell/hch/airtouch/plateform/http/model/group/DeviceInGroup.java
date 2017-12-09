package com.honeywell.hch.airtouch.plateform.http.model.group;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by h127856 on 6/14/16.
 */
public class DeviceInGroup implements Serializable {

    @SerializedName("groupId")
    private int mGroupId;

    @SerializedName("locationId")
    private int mLocationId;

    @SerializedName("groupName")
    private String mGroupName;

    @SerializedName("permission")
    private int mPermission;

    @SerializedName("devices")
    private List<DevicesForGroupResponse> mGroupDeviceList;

    public int getGroupId() {
        return mGroupId;
    }

    public void setGroupId(int mGroupId) {
        this.mGroupId = mGroupId;
    }

    public int getLocationId() {
        return mLocationId;
    }

    public void setLocationId(int mLocationId) {
        this.mLocationId = mLocationId;
    }

    public String getGroupName() {
        return mGroupName;
    }

    public void setGroupName(String mGroupName) {
        this.mGroupName = mGroupName;
    }

    public int getPermission() {
        return mPermission;
    }

    public void setPermission(int mPermission) {
        this.mPermission = mPermission;
    }

    public List<DevicesForGroupResponse> getGroupDeviceList() {
        return mGroupDeviceList;
    }

    public void setGroupDeviceList(List<DevicesForGroupResponse> mGroupDeviceList) {
        this.mGroupDeviceList = mGroupDeviceList;
    }
}
