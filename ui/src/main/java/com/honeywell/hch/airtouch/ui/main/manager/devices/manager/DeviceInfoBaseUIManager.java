package com.honeywell.hch.airtouch.ui.main.manager.devices.manager;

import android.os.Bundle;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.devices.airtouch.model.AirtouchRunStatus;
import com.honeywell.hch.airtouch.plateform.devices.water.model.WaterDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceGroupItem;
import com.honeywell.hch.airtouch.plateform.http.manager.model.SelectStatusDeviceItem;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.device.AquaTouchRunstatus;
import com.honeywell.hch.airtouch.ui.common.manager.model.Category;
import com.honeywell.hch.airtouch.ui.control.ui.device.controller.DeviceControlActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by h127856 on 6/3/16.
 */
public class DeviceInfoBaseUIManager {

    public static final int MASTER_DEVICE = 1;

    public static final int COMMOM_DEVICE = 0;

    public static final int CALL_SUCCESS = 0;

    public static final int DUNPLICATION_GROUP_NAME = -2;

    public static final int NO_MASTER_ID = -1;

    public static final int GROUP_TYPE = 0;
    public static final int DEVICE_TYPE = 1;

    //是否处于编辑状态,alldevice 和deviceControl公用
    private static boolean isEditStatus = false;


    protected GroupManager mGroupManager;
    protected int mLocationId;
    protected UserLocationData mUserLocationData;
    protected final int EMPTY = 0;

    protected ArrayList<Category> mListData;


    /**
     * 初始化
     *
     * @param locationId
     */
    public void init(int locationId) {
        mLocationId = locationId;
        if (mGroupManager == null) {
            mGroupManager = new GroupManager();
        }
        mUserLocationData = UserDataOperator.getUserLocationByID(mLocationId,UserAllDataContainer.shareInstance().getUserLocationDataList());
    }

    public void init(UserLocationData userLocationData) {
        mLocationId = userLocationData.getLocationID();
        if (mGroupManager == null) {
            mGroupManager = new GroupManager();
        }
        mUserLocationData = userLocationData;
    }


    public void setGroupId(int groupId) {
        mGroupManager.setmGroupId(groupId);
    }

    /**
     * 设置success callBack
     *
     * @param successCallback
     */
    public void setSuccessCallback(GroupManager.SuccessCallback successCallback) {
        mGroupManager.setSuccessCallback(successCallback);
    }

    /**
     * 设置error callBack
     *
     * @param errorCallback
     */
    public void setErrorCallback(GroupManager.ErrorCallback errorCallback) {
        mGroupManager.setErrorCallback(errorCallback);
    }


    /**
     * 获取除了选中的master device之外的其他所有选中设备id列表
     *
     * @param masterId
     * @return
     */
    public Integer[] getSelectedIdsExceptMasterId(int masterId) {

        List<Integer> idList = new ArrayList<>();

        for (SelectStatusDeviceItem deviceItem : mUserLocationData.getSelectDeviceList()) {
            if (deviceItem.isSelected() && deviceItem.getDeviceItem().getDeviceInfo() != null
                    && deviceItem.getDeviceItem().getDeviceInfo().getDeviceID() != masterId) {
                idList.add(deviceItem.getDeviceItem().getDeviceInfo().getDeviceID());
            }
        }
        Integer[] integerArray = new Integer[idList.size()];
        return idList.toArray(integerArray);
    }

    /**
     * 判断组名是否有重名的
     *
     * @param groupName
     * @return
     */
    public boolean isHaveSameGroupName(String groupName) {
        for (DeviceGroupItem deviceGroupItem : mUserLocationData.getDeviceGroupList()) {
            if (groupName.equalsIgnoreCase(deviceGroupItem.getGroupName())) {
                return true;
            }
        }
        return false;
    }

    /*
    设备控制返回更新数据，allDevice 和 groupcontrol page
     */
    public void setDeviceControlResult(int deviceId, ArrayList<Category> mlistData, Object object) {
        for (int i = 0; i < mlistData.size(); i++) {
            ArrayList<SelectStatusDeviceItem> selectStatusDeviceItemList = mlistData.get(i).getmSelectDeviceListItem();
            if (selectStatusDeviceItemList != null) {
                for (SelectStatusDeviceItem selectStatusDeviceItem : selectStatusDeviceItemList) {
                    if (selectStatusDeviceItem.getDeviceItem().getDeviceInfo().getDeviceID() == deviceId) {
                        if (selectStatusDeviceItem.getDeviceItem() instanceof AirTouchDeviceObject) {
                            LogUtil.log(LogUtil.LogLevel.INFO, "GroupControlActivity_Old", "airtouch device name: " + (selectStatusDeviceItem.getDeviceItem()).getDeviceInfo().getName());
                            ((AirTouchDeviceObject) selectStatusDeviceItem.getDeviceItem()).setAirtouchDeviceRunStatus((AirtouchRunStatus) (((Bundle)object).get(DeviceControlActivity.ARG_DEVICE_RUNSTATUS)));
                        } else if (selectStatusDeviceItem.getDeviceItem() instanceof WaterDeviceObject) {
                            LogUtil.log(LogUtil.LogLevel.INFO, "GroupControlActivity_Old", "smatro device name: " + (selectStatusDeviceItem.getDeviceItem()).getDeviceInfo().getName());
                            ((WaterDeviceObject) selectStatusDeviceItem.getDeviceItem()).setAquaTouchRunstatus((AquaTouchRunstatus) (((Bundle)object).get(DeviceControlActivity.ARG_DEVICE_RUNSTATUS)));
                        }
                    }
                }
            }
        }

    }

    public static boolean isEditStatus() {
        return isEditStatus;
    }

    public static void setIsEditStatus(boolean isEditStatus) {
        DeviceInfoBaseUIManager.isEditStatus = isEditStatus;
    }

}
