package com.honeywell.hch.airtouch.plateform.http.model.group;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Qian Jin on 10/14/15.
 * get group by location id
 */
public class GroupData implements  Serializable {
    public static final String GROUP_DATA = "group_data_bundle";

    @SerializedName("groupList")
    private List<GroupDataResponse> mGroupList;

    @SerializedName("unGroupingDeviceList")
    private List<DevicesForGroupResponse> mUnGroupDeviceList;

    public List<GroupDataResponse> getGroupList() {
        return mGroupList;
    }

    public List<DevicesForGroupResponse> getUnGroupDeviceList() {
        return mUnGroupDeviceList;
    }

}
