package com.honeywell.hch.airtouch.ui.main.manager.devices.manager;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;
import com.honeywell.hch.airtouch.plateform.http.manager.model.MadAirDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.SelectStatusDeviceItem;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.manager.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 24/11/16.
 */

public class AllDeviceMadAirManager extends AllDeviceUIBaseManager {

    public AllDeviceMadAirManager(UserLocationData locationData) {
        mUserLocationData = locationData;
        if (mGroupManager == null) {
            mGroupManager = new GroupManager();
        }
    }

    /*
        口罩数据
     */
    public ArrayList<Category> getCategoryData() {
        ArrayList<Category> listData = new ArrayList<Category>();
        if (mUserLocationData != null && mUserLocationData.getSelectDeviceList().size() != EMPTY) {
//            LogUtil.log(LogUtil.LogLevel.DEBUG, "AllDeviceFragment", "RefreshDataReceiver--- "+((MadAirDeviceObject)mUserLocationData.getSelectDeviceList().get(0).getDeviceItem()).getmMadAirDeviceModel().getDeviceStatus());
            Category category = new Category(AppManager.getInstance().getApplication().getString(R.string.mad_air_mask));
            category.addSelectItem(mUserLocationData.getSelectDeviceList());
            category.setType(DEVICE_TYPE);
            listData.add(category);
        }

        mListData = listData;
        return listData;
    }

    /**
     * 删除设备
     *
     * @return
     */
    @Override
    public boolean deleteDevice() {
        mGroupManager.deleteMadAirDeviceList(getMadAirSelectedIds());
        return true;
    }

    public String[] getMadAirSelectedIds() {

        List<String> idList = new ArrayList<>();
        ArrayList<SelectStatusDeviceItem> selectStatusDeviceItems = getSelectDeviceListItem();
        if (selectStatusDeviceItems != null && selectStatusDeviceItems.size() > 0) {
            for (SelectStatusDeviceItem deviceItem : selectStatusDeviceItems) {
                if (deviceItem.isSelected()) {
                    idList.add(((MadAirDeviceObject) deviceItem.getDeviceItem()).getmMadAirDeviceModel().getMacAddress());
                }
            }
        }
        String[] integerArray = new String[idList.size()];
        return idList.toArray(integerArray);
    }


    @Override
    public boolean getGroupInfoFromServer(boolean isRefreshOpr) {
        return true;
    }

    @Override
    public int createNewGroup(String groupName) {
        return CALL_SUCCESS;
    }


    @Override
    public void addDeviceToGroup(int groupId) {

    }


}
