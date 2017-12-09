package com.honeywell.hch.airtouch.ui.trydemo.manager;

import android.content.Intent;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.common.Filter;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.manager.model.RealUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.device.AirtouchCapability;
import com.honeywell.hch.airtouch.plateform.http.model.device.SmartROCapability;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.control.manager.device.ControlUIBaseManager;
import com.honeywell.hch.airtouch.ui.control.manager.model.DeviceMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UI login management for Device control and filter page
 * Created by Qian Jin on 4/15/16.
 */
public class TryDemoControlUIManager extends ControlUIBaseManager {


    public TryDemoControlUIManager() {
        mControlManager = new TryDemoControlServer();
    }


    @Override
    public String getControlModePre(int deviceId) {
        return HPlusConstants.DEVICE_CONTROL_NORMAL;
    }

    @Override
    public boolean getIsFlashing(int deviceId) {
        return DeviceMode.IS_REFLASHING;
    }

    @Override
    public void getConfigFromServer() {
        mControlManager.getConfigFromServer();
    }

    @Override
    public void controlDevice(int deviceId, String scenarioOrSpeed, String productName) {
        mControlManager.controlDevice(deviceId, scenarioOrSpeed, productName);
    }

    @Override
    public void controlWaterDevice(int deviceId, int scenario, String productName) {
        mControlManager.controlWaterDevice(deviceId, scenario, productName);
    }

    @Override
    public UserLocationData getLocationWithId(int mLocationId) {
        return new RealUserLocationData();
    }

    @Override
    public HomeDevice getHomeDeviceByDeviceId(int deviceId) {
        return TryDemoHomeListContructor.getInstance().getHomeDeviceWithDeviceId(deviceId);
    }

    @Override
    public Map<Integer, AirtouchCapability> getDeviceCapabilityMap() {
        Map<Integer, AirtouchCapability> airtouchCapabilityMap = new HashMap<>();
        AirtouchCapability airtouchCapability = new AirtouchCapability();
        airtouchCapability.setFilter1ExpiredTime(1000);
        airtouchCapability.setFilter2ExpiredTime(1000);
        airtouchCapabilityMap.put(HPlusConstants.AIRTOUCH_450_TYPE, airtouchCapability);
        return airtouchCapabilityMap;
    }

    @Override
    //update filter runtime from server
    public void setmWaterRunTimeLife(int i, HomeDevice mCurrentDevice, List<Filter> filters, SmartROCapability smartROCapability) {
        filters.get(i).setmWaterRunTimeLife(80);
    }

    @Override
    public Map<Integer, SmartROCapability> getSmartRoCapabilityMap() {
        SmartROCapability smartROCapability = new SmartROCapability();
        Map<Integer, SmartROCapability> smartROCapabilityMap = new HashMap<>();
        smartROCapabilityMap.put(HPlusConstants.WATER_SMART_RO_600_TYPE, smartROCapability);
        return smartROCapabilityMap;
    }

    @Override
    public int getMadAirDeviceImg(MadAirDeviceModel madAirDeviceModel) {
        return R.drawable.mad_air_detail_white;
    }

    @Override
    public boolean isDuplicateMadAirAddress(String deviceName) {
        return false;
    }


    @Override
    public void saveDeviceName(String macAddress, String name) {
        TryDemoHomeListContructor.getInstance().getVirtualUserLocationDataList().get(0).getMadAirDeviceModel().setDeviceName(name);
        Intent intent = new Intent(HPlusConstants.TRY_DEMO_REFRESH_MADAIR_DATA);
        AppManager.getInstance().getApplication().getApplicationContext().sendBroadcast(intent);
    }

}
