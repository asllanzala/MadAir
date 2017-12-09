package com.honeywell.hch.airtouch.plateform.http.manager.model;

import com.honeywell.hch.airtouch.library.util.SharePreferenceUtil;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.model.group.GroupDataResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by h127856 on 6/3/16.
 */
public class DeviceGroupItem {

    private List<SelectStatusDeviceItem> mGroupDeviceList = new ArrayList<>();

    private int mGroupId;

    private int mLocationId;

    private String mGroupName = "";

    private int mPermission;

    //group的操作status
//    private int mGroupStatus;

    private int scenarioOperation;  //group control status


    public List<SelectStatusDeviceItem> getGroupDeviceList() {
        return mGroupDeviceList;
    }

    public void setGroupDeviceList(ArrayList<SelectStatusDeviceItem> mGroupDeviceList) {
        this.mGroupDeviceList = (ArrayList<SelectStatusDeviceItem>)mGroupDeviceList.clone();
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

//    public int getGroupStatus() {
//        mGroupStatus = SharePreferenceUtil.getPrefInt(HPlusConstants.PREFERENCE_SUCCESS_GROUP_CONTROL_MODE,
//                Integer.toString(mGroupId), DeviceMode.MODE_UNDEFINE);
//
//        return mGroupStatus;
//    }

//    public void setGroupStatus(int mGroupStatus) {
//        this.mGroupStatus = mGroupStatus;
//    }

    public int getGroupId() {
        return mGroupId;
    }

    public void setGroupId(int mGroupId) {
        this.mGroupId = mGroupId;
    }

    public int getScenarioOperation() {
        return scenarioOperation;
    }

    public void setScenarioOperation(int scenarioOperation) {
        this.scenarioOperation = scenarioOperation;
    }

    public void initFromGroupDataResponseExcHomeDevice(GroupDataResponse groupDataResponse){
        mGroupId = groupDataResponse.getGroupId();
        mLocationId = groupDataResponse.getLocationId();
        mGroupName = groupDataResponse.getGroupName();
        mPermission = groupDataResponse.getPermission();
        scenarioOperation = groupDataResponse.getScenarioOperation();
        SharePreferenceUtil.setPrefInt(HPlusConstants.PREFERENCE_SUCCESS_GROUP_CONTROL_MODE,
                Integer.toString(mGroupId), scenarioOperation);
    }

    public void addDeviceToHomeList(SelectStatusDeviceItem homeDevice){
        mGroupDeviceList.add(homeDevice);
    }

}
