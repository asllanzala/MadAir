package com.honeywell.hch.airtouch.plateform.http.model.group;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Qian Jin on 10/14/15.
 * get group by group id
 */
public class GroupDataResponse implements Serializable {
    public static final String GROUP_LIST = "group_list_bundle";

    @SerializedName("groupId")
    private int mGroupId;

    @SerializedName("locationId")
    private int mLocationId;

    @SerializedName("groupName")
    private String mGroupName;

    @SerializedName("permission")
    private int mPermission;

    @SerializedName("scenarioOperation")
    private int scenarioOperation;
    //Away(Off):0    Home(Auto):1;     Sleep(Sleep):2;    Awake(Awake):3;    No record:-1

    @SerializedName("devices")
    private List<DevicesForGroupResponse> mGroupDeviceList;

    public int getGroupId() {
        return mGroupId;
    }

    public int getLocationId() {
        return mLocationId;
    }

    public String getGroupName() {
        return mGroupName;
    }

    public void setGroupId(int groupId) {
        mGroupId = groupId;
    }

    public void setLocationId(int locationId) {
        mLocationId = locationId;
    }

    public void setGroupName(String groupName) {
        mGroupName = groupName;
    }

    public List<DevicesForGroupResponse> getGroupDeviceList() {
        return mGroupDeviceList;
    }

    public int getPermission() {
        return mPermission;
    }

    public void setPermission(int permission) {
        mPermission = permission;
    }

    public int getScenarioOperation() {
        return scenarioOperation;
    }

    public void setScenarioOperation(int scenarioOperation) {
        this.scenarioOperation = scenarioOperation;
    }
}
