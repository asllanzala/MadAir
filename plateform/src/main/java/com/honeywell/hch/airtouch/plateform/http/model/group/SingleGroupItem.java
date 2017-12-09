package com.honeywell.hch.airtouch.plateform.http.model.group;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by h127856 on 6/14/16.
 * getGroupByGroupId
 */
public class SingleGroupItem implements  Serializable {

    @SerializedName("group")
    private DeviceInGroup mDeviceInGroup;

    public DeviceInGroup getDeviceInGroup() {
        return mDeviceInGroup;
    }

    public void setDeviceInGroup(DeviceInGroup mDeviceInGroup) {
        this.mDeviceInGroup = mDeviceInGroup;
    }
}
