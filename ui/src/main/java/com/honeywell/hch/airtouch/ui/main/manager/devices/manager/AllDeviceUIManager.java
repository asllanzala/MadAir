package com.honeywell.hch.airtouch.ui.main.manager.devices.manager;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.plateform.http.manager.model.SelectStatusDeviceItem;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.device.DeviceListRequest;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.manager.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by h127856 on 6/3/16.
 */
public class AllDeviceUIManager extends AllDeviceUIBaseManager {

    public AllDeviceUIManager(int locationId) {
        init(locationId);
    }

    public AllDeviceUIManager(UserLocationData userLocationData) {
        init(userLocationData);
    }

    public AllDeviceUIManager() {
    }

    @Override
    public boolean getGroupInfoFromServer(boolean isRefreshOpr) {
        if (mUserLocationData == null) {
            return false;
        }
        isRefreshOpr = mUserLocationData.isDeviceListLoadingData() ? false : isRefreshOpr;
        mGroupManager.getGroupByLocationId(isRefreshOpr, mLocationId);
        return true;
    }

    @Override
    public int createNewGroup(String groupName) {
        if (isHaveSameGroupName(groupName)) {
            return DUNPLICATION_GROUP_NAME;
        }

        int masterId = getGroupMasterDeviceId();
        if (masterId == NO_MASTER_ID) {
            return NO_MASTER_ID;
        }
        DeviceListRequest request = new DeviceListRequest(getSelectedIdsExceptMasterId(masterId));


        mGroupManager.createGroup(groupName, masterId, mLocationId, request);
        return CALL_SUCCESS;
    }


    @Override
    public void addDeviceToGroup(int groupId) {
        DeviceListRequest deviceListRequest = new DeviceListRequest(getAllDeviceSelectedIds());
        mGroupManager.addDeviceToGroup(groupId, deviceListRequest);
    }


    /**
     * 删除设备
     *
     * @return
     */
    @Override
    public boolean deleteDevice() {
        mGroupManager.deleteDeviceList(getAllDeviceSelectedIds(), mUserLocationData);
        return true;
    }

}
