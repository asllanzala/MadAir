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
 * Created by honeywell on 23/12/2016.
 */

public abstract class AllDeviceUIBaseManager extends DeviceInfoBaseUIManager  {

    public void resetData(int locationId) {
        init(locationId);
    }

    public void resetUserLocationData(UserLocationData userLocationData){
        mUserLocationData = userLocationData;
        if (mUserLocationData != null){
            mLocationId = mUserLocationData.getLocationID();
        }
    }


    public abstract boolean getGroupInfoFromServer(boolean isRefreshOpr) ;

    public abstract int createNewGroup(String groupName);


    public abstract void addDeviceToGroup(int groupId);


    /**
     * 删除设备
     *
     * @return
     */
    public abstract boolean deleteDevice();

    /**
     * the rule is that: use the first selected master device id as master id
     *
     * @return
     */

    public int getGroupMasterDeviceId() {
        ArrayList<SelectStatusDeviceItem> selectStatusDeviceItems = getSelectDeviceListItem();
        if (selectStatusDeviceItems != null && selectStatusDeviceItems.size() > 0) {
            for (SelectStatusDeviceItem deviceItem : selectStatusDeviceItems) {
                if (deviceItem.isSelected() && (DeviceType.isAirTouchX(deviceItem.getDeviceItem().getDeviceType())
                        || DeviceType.isAirTouchXCompactSeries(deviceItem.getDeviceItem().getDeviceType()))
                        && deviceItem.getDeviceItem().getDeviceInfo() != null) {
                    return deviceItem.getDeviceItem().getDeviceInfo().getDeviceID();
                }
            }
        }

        return NO_MASTER_ID;
    }

    /*
    set adapter data
     */
    public ArrayList<Category> getCategoryData() {
        ArrayList<Category> listData = new ArrayList<Category>();
        if (mUserLocationData != null && mUserLocationData.getDeviceGroupList().size() != EMPTY) {
            Category categoryOne = new Category(AppManager.getInstance().getApplication().
                    getString(R.string.group));
            categoryOne.addGoupItem(mUserLocationData.getDeviceGroupList());
            categoryOne.setType(GROUP_TYPE);
            listData.add(categoryOne);
        }
        if (mUserLocationData != null && mUserLocationData.getSelectDeviceList().size() != EMPTY) {
            Category categoryTwo;
            if (mUserLocationData.getDeviceGroupList().size() != EMPTY) {
                categoryTwo = new Category(AppManager.getInstance().getApplication().
                        getString(R.string.all_device_other));
            } else {
                categoryTwo = new Category(null);
            }
            categoryTwo.addSelectItem(mUserLocationData.getSelectDeviceList());
            categoryTwo.setType(DEVICE_TYPE);
            listData.add(categoryTwo);
        }

        mListData = listData;
        return listData;
    }

    /**
     * all device activity bottom show depend on weather if there is a group or not
     *
     * @return
     */
    public boolean isHasGroup() {
        if (mUserLocationData != null && mUserLocationData.getDeviceGroupList().size() > 0) {
            return true;
        }
        return false;
    }


    public boolean isLoadedDeviceListInLocation() {
        if (mUserLocationData != null && mUserLocationData.getSelectDeviceList().size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取allDevice界面中所有选中的设备列表
     *
     * @return
     */
    public Integer[] getAllDeviceSelectedIds() {

        List<Integer> idList = new ArrayList<>();
        ArrayList<SelectStatusDeviceItem> selectStatusDeviceItems = getSelectDeviceListItem();
        if (selectStatusDeviceItems != null && selectStatusDeviceItems.size() > 0) {
            for (SelectStatusDeviceItem deviceItem : selectStatusDeviceItems) {
                if (deviceItem.isSelected()) {
                    idList.add(deviceItem.getDeviceItem().getDeviceId());
                }
            }
        }
        Integer[] integerArray = new Integer[idList.size()];
        return idList.toArray(integerArray);
    }



    /**
     * AllDevice界面中是否所有的设备已被选中
     *
     * @return
     */
    public boolean isAllDeviceItemSelected() {
        ArrayList<SelectStatusDeviceItem> selectStatusDeviceItems = getSelectDeviceListItem();
        if (selectStatusDeviceItems != null && selectStatusDeviceItems.size() > 0) {
            for (SelectStatusDeviceItem selectStatusDeviceItem : selectStatusDeviceItems) {
                if (!selectStatusDeviceItem.isSelected()) {
                    return false;
                }
            }
        }
        return true;
    }

    protected ArrayList<SelectStatusDeviceItem> getSelectDeviceListItem() {
        if (mListData != null && mListData.size() > 0) {
            for (Category category : mListData) {
                if (category.getType() == DEVICE_TYPE) {
                    return category.getmSelectDeviceListItem();
                }
            }
        }
        return null;
    }
}
