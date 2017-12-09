package com.honeywell.hch.airtouch.ui.control.manager.group;

import android.content.Context;

import com.honeywell.hch.airtouch.library.util.SharePreferenceUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceGroupItem;
import com.honeywell.hch.airtouch.plateform.http.manager.model.SelectStatusDeviceItem;
import com.honeywell.hch.airtouch.plateform.http.model.device.DeviceListRequest;
import com.honeywell.hch.airtouch.plateform.http.model.group.ScenarioGroupRequest;
import com.honeywell.hch.airtouch.ui.common.manager.model.Category;
import com.honeywell.hch.airtouch.ui.control.manager.model.DeviceMode;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.DeviceInfoBaseUIManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by h127856 on 6/3/16.
 */
public class GroupControlUIManager extends DeviceInfoBaseUIManager {

    private final int INTERVAL = 1000;
    private long mClickTime = 0;
    private final int GROUPCACHEID = 100;

    public GroupControlUIManager(int locationId) {
        init(locationId);
    }


    /**
     * 获取组里的设备，用于列表展示
     *
     * @param groupId
     * @return
     */
    public ArrayList<Category> getGroupDeviceCategoryData(int groupId) {
        ArrayList<Category> listData = new ArrayList<Category>();
        for (DeviceGroupItem deviceGroupItem : mUserLocationData.getDeviceGroupList()) {
            if (deviceGroupItem != null && deviceGroupItem.getGroupId() == groupId) {
                List<SelectStatusDeviceItem> groupDevices = deviceGroupItem.getGroupDeviceList();
                if (groupDevices != null && groupDevices.size() > 0) {
                    Category category = new Category("");
                    if (AppManager.isEnterPriseVersion()) {
                        category.setmCategoryName(null);
                    }
                    ArrayList<SelectStatusDeviceItem> deviceTempList = new ArrayList<>();
                    for (SelectStatusDeviceItem homeDevice : groupDevices) {
                        SelectStatusDeviceItem deviceMode = new SelectStatusDeviceItem();
                        deviceMode.setDeviceItem(homeDevice.getDeviceItem());
                        deviceTempList.add(deviceMode);
                    }
                    category.addSelectItem(deviceTempList);
                    category.setType(DEVICE_TYPE);
                    listData.add(category);
                }
            }
        }
        mListData = listData;

        return listData;
    }

    /**
     * 获取组名字
     *
     * @param groupId
     * @return
     */
    public String getGroupName(int groupId) {
        ArrayList<Category> listData = new ArrayList<Category>();
        for (DeviceGroupItem deviceGroupItem : mUserLocationData.getDeviceGroupList()) {
            if (deviceGroupItem != null && deviceGroupItem.getGroupId() == groupId) {
                return deviceGroupItem.getGroupName();
            }
        }

        return "";
    }

    public DeviceGroupItem getDeviceGroupItemByGroupId(int groupId) {
        if (mUserLocationData.getDeviceGroupList() != null) {
            for (DeviceGroupItem deviceGroupItem : mUserLocationData.getDeviceGroupList()) {
                if (deviceGroupItem.getGroupId() == groupId) {
                    return deviceGroupItem;
                }
            }
        }
        return null;
    }

    /**
     * 选中的设备中是否有主设备
     *
     * @return
     */
    public boolean isHasMasterDeviceInSelectedIds() {
        ArrayList<SelectStatusDeviceItem> groupDevicesItems = getDeviceGroupItem();
        if (groupDevicesItems != null && groupDevicesItems.size() > 0) {
            for (SelectStatusDeviceItem deviceItem : groupDevicesItems) {
                if (deviceItem.isSelected() && deviceItem.getDeviceItem().getDeviceInfo() != null
                        && deviceItem.getDeviceItem().getIsMasterDevice() == MASTER_DEVICE) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断组内是否有设备
     *
     * @param groupId
     * @return
     */
    public boolean isHasDeviceInThisGroup(int groupId) {
        for (DeviceGroupItem deviceGroupItem : mUserLocationData.getDeviceGroupList()) {
            if (deviceGroupItem != null && deviceGroupItem.getGroupId() == groupId) {
                if (deviceGroupItem.getGroupDeviceList() != null &&
                        deviceGroupItem.getGroupDeviceList().size() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 把设备移除组
     *
     * @param groupId
     */
    public void moveDeviceOutFromGroup(int groupId) {
        DeviceListRequest deviceListRequest = new DeviceListRequest(getGroupDeviceSelectedIds(groupId));
        mGroupManager.moveOutDeviceFromGroup(groupId, deviceListRequest);
    }

    /**
     * 删除组
     *
     * @param groupId
     */
    public void deleteGroup(int groupId) {
        mGroupManager.deleteGroup(groupId);
    }

    /*
     更新组名
     */
    public void updateGroupName(String groupName, int groupId) {
        mGroupManager.updateGroupName(groupName, groupId);
    }

    /*
    组控制
     */
    public void sendScenarioToGroup(int groupId, int scenarioMode) {
        ScenarioGroupRequest request = new ScenarioGroupRequest(scenarioMode);
        mGroupManager.sendScenarioToGroup(groupId, request);
    }

    public boolean canClcikable() {
        if (System.currentTimeMillis() - mClickTime > INTERVAL) {
            mClickTime = System.currentTimeMillis();
            return true;
        }
        mClickTime = System.currentTimeMillis();
        return false;
    }

    public void groupControlSelectStatusDeviceItem(int deviceId, ArrayList<Category> mlistData) {
        ArrayList<SelectStatusDeviceItem> selectStatusDeviceItemList = mlistData.get(0).getmSelectDeviceListItem();
        for (SelectStatusDeviceItem selectStatusDeviceItem : selectStatusDeviceItemList) {
            if (selectStatusDeviceItem.getDeviceItem().getDeviceInfo().getDeviceID() == deviceId) {
                selectStatusDeviceItem.setDeviceItem(UserDataOperator.getDeviceWithDeviceId(deviceId,UserAllDataContainer.shareInstance().getUserLocationDataList(),UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList()));
            }
        }
    }


    public int getSendGroupMode(int groupID) {
        // TO-DO: need to delete data in SharePreference
        return SharePreferenceUtil.getPrefInt(HPlusConstants.PREFERENCE_SEND_GROUP_CONTROL_MODE,
                Integer.toString(groupID), DeviceMode.MODE_UNDEFINE);
    }

    public int getSuccessGroupMode(int groupID) {
        // TO-DO: need to delete data in SharePreference
        return SharePreferenceUtil.getPrefInt(HPlusConstants.PREFERENCE_SUCCESS_GROUP_CONTROL_MODE,
                Integer.toString(groupID), DeviceMode.MODE_UNDEFINE);
    }

    public boolean getIsFlashing(int groupID) {
        return SharePreferenceUtil.getPrefBoolean(HPlusConstants.PREFERENCE_GROUP_CONTROL_FLASH,
                Integer.toString(groupID + GROUPCACHEID), DeviceMode.IS_REFLASHING);
    }

    public void clearFlashPreference(Context mContext) {
        SharePreferenceUtil.clearPreference(mContext, SharePreferenceUtil
                .getSharedPreferenceInstanceByName(HPlusConstants.PREFERENCE_GROUP_CONTROL_FLASH));
    }


    /**
     * 获取某个组下所有选中的设备列表
     *
     * @return
     */
    public Integer[] getGroupDeviceSelectedIds(int groupId) {

        List<Integer> idList = new ArrayList<>();

        ArrayList<SelectStatusDeviceItem> groupDevicesItems = getDeviceGroupItem();
        if (groupDevicesItems != null && groupDevicesItems.size() > 0) {
            for (SelectStatusDeviceItem deviceItem : groupDevicesItems) {
                if (deviceItem.isSelected() && deviceItem.getDeviceItem().getDeviceInfo() != null) {
                    idList.add(deviceItem.getDeviceItem().getDeviceInfo().getDeviceID());
                }
            }
        }

        Integer[] integerArray = new Integer[idList.size()];
        return idList.toArray(integerArray);
    }


    /**
     * 删除组里的设备
     *
     * @return
     */
    public boolean deleteDeviceFromGroup(int groupId) {
        mGroupManager.deleteDeviceList(getGroupDeviceSelectedIds(groupId), mUserLocationData);
        return true;
    }

    /**
     * GroupControl界面是否所有的设备已被选中
     *
     * @return
     */
    public boolean isAllDeviceInGroupItemSelected(int groupId) {

        ArrayList<SelectStatusDeviceItem> groupDevicesItems = getDeviceGroupItem();
        if (groupDevicesItems != null && groupDevicesItems.size() > 0) {
            for (SelectStatusDeviceItem deviceItem : groupDevicesItems) {
                if (!deviceItem.isSelected()) {
                    return false;
                }
            }

        }

        return true;
    }

    /**
     * @param listModelDevieList
     */
    public void setGroupModelDevieList(ArrayList<SelectStatusDeviceItem> listModelDevieList, int groupId) {
        DeviceGroupItem deviceGroupItem = getDeviceGroupItemByGroupId(groupId);
        if (deviceGroupItem != null) {
            deviceGroupItem.setGroupDeviceList(listModelDevieList);
        }
    }


    /**
     * 这里获取到的mList是特定group下的组设备列表
     * @return
     */
    private ArrayList<SelectStatusDeviceItem> getDeviceGroupItem() {
        if (mListData != null && mListData.size() > 0) {
           return mListData.get(0).getmSelectDeviceListItem();

        }
        return null;
    }


}
