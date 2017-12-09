package com.honeywell.hch.airtouch.ui.trydemo.manager;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.devices.water.model.WaterDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.manager.model.MadAirDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.RealUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.VirtualUserLocationData;
import com.honeywell.hch.airtouch.ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by honeywell on 21/12/2016.
 */

public class TryDemoHomeListContructor {

    public static final String SHANGHAI_CODE = "CHSH000000";
    public static final int REAL_LOCATION_ID = 1;
    public static final int VIRTUAL_LOCATION_ID = 2;
    private static final int AT_HOME_SCENARIO_MODEL = 1;
    //
    private List<UserLocationData> mUserLocationDataList = new ArrayList<>(1);

    private List<VirtualUserLocationData> mVirtualUserLocationDataList = new ArrayList<>(1);  //virtualLocationData

    private static TryDemoHomeListContructor mTtryDemoHomeListContructor;

    public static TryDemoHomeListContructor getInstance() {
        if (mTtryDemoHomeListContructor == null) {
            mTtryDemoHomeListContructor = new TryDemoHomeListContructor();
        }
        return mTtryDemoHomeListContructor;
    }

    public void exitTryDemoModel() {
        mUserLocationDataList.clear();
        mVirtualUserLocationDataList.clear();
        TryDemoDataConstructor.exitTryDemo();
    }

    public List<UserLocationData> getUserLocationDataList() {
        return mUserLocationDataList;
    }

    public List<VirtualUserLocationData> getVirtualUserLocationDataList() {
        return mVirtualUserLocationDataList;
    }

    public void constructorDataList() {
        TryDemoDataConstructor.initDeviceList();
        UserLocationData userLocationData = new RealUserLocationData();
        userLocationData.setLocationID(REAL_LOCATION_ID);
        userLocationData.setName(AppManager.getInstance().getApplication().getString(R.string.my_home));
        userLocationData.setCity("CHSH000000");
        userLocationData.setOperationModel(AT_HOME_SCENARIO_MODEL);
        userLocationData.setHomeDevicesList(TryDemoDataConstructor.getTryDemoHomeDeviceList());
        userLocationData.setDefaultPMDevice((AirTouchDeviceObject) TryDemoDataConstructor.getTryDemoHomeDeviceList().get(0));
        userLocationData.setDefaultTVOCDevice((AirTouchDeviceObject) TryDemoDataConstructor.getTryDemoHomeDeviceList().get(0));
        userLocationData.setSelectDeviceList(TryDemoDataConstructor.getSelectStutasHomeDevicesList());
        userLocationData.setDeviceNetworkDataLoadSuccess();
        if (AppManager.getInstance().getLocalProtocol().isTryDemoShowWaterDevice()) {
            userLocationData.setDefaultWaterDevice((WaterDeviceObject) TryDemoDataConstructor.getTryDemoHomeDeviceList().get(1));
        }
        mUserLocationDataList.add(userLocationData);

        if (AppManager.getInstance().getLocalProtocol().isTryDemoShowWaterDevice()) {
            VirtualUserLocationData virtualUserLocationData = new VirtualUserLocationData();
            userLocationData.setCity("CHSH000000");
            virtualUserLocationData.setMadAirDeviceObject((MadAirDeviceObject) TryDemoDataConstructor.getTryDemoVirtualDevicesList().get(0));
            virtualUserLocationData.setHomeDevicesList(TryDemoDataConstructor.getTryDemoVirtualDevicesList());
            mVirtualUserLocationDataList.add(virtualUserLocationData);
        }

    }


    public HomeDevice getHomeDeviceWithDeviceId(int deviceId) {

        for (UserLocationData userLocationData : mUserLocationDataList) {
            HomeDevice homeDevice = getHomeDevice(deviceId, userLocationData);
            if (homeDevice != null) {
                return homeDevice;
            }
        }

        for (UserLocationData userLocationData : mVirtualUserLocationDataList) {
            HomeDevice homeDevice = getHomeDevice(deviceId, userLocationData);
            if (homeDevice != null) {
                return homeDevice;
            }
        }
        return null;

    }

    private HomeDevice getHomeDevice(int deviceId, UserLocationData userLocationData) {
        List<HomeDevice> devices = userLocationData.getHomeDevicesList();
        for (HomeDevice homeDevice : devices) {
            if (homeDevice.getDeviceId() == deviceId) {
                return homeDevice;
            }
        }
        return null;
    }
}
