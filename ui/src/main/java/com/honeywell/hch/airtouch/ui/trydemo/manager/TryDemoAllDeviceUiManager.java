package com.honeywell.hch.airtouch.ui.trydemo.manager;

import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.device.DeviceListRequest;
import com.honeywell.hch.airtouch.ui.common.manager.model.Category;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.AllDeviceUIBaseManager;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.DeviceInfoBaseUIManager;
import com.honeywell.hch.airtouch.ui.splash.StartActivity;
import com.honeywell.hch.airtouch.ui.trydemo.ui.IRefreshOpr;

import java.util.ArrayList;

/**
 * Created by Vincent on 2/11/16.
 */
public class TryDemoAllDeviceUiManager extends AllDeviceUIBaseManager {


    public TryDemoAllDeviceUiManager(UserLocationData locationData) {
        init(locationData);
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


    /**
     * 删除设备
     *
     * @return
     */
    @Override
    public boolean deleteDevice() {
        return true;
    }


}
