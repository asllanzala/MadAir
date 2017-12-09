package com.honeywell.hch.airtouch.ui.control.manager.device;

import com.honeywell.hch.airtouch.library.util.SharePreferenceUtil;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModelSharedPreference;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.device.AirtouchCapability;
import com.honeywell.hch.airtouch.plateform.http.model.device.SmartROCapability;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.control.manager.model.DeviceMode;

import java.util.List;
import java.util.Map;

/**
 * UI login management for Device control and filter page
 * Created by Qian Jin on 4/15/16.
 */
public class ControlUIManager extends  ControlUIBaseManager {


    public ControlUIManager(){
        mControlManager = new ControlManager();
    }



    public void getConfigFromServer() {
        mControlManager.getConfigFromServer();
    }

    public void controlDevice(int deviceId, String scenarioOrSpeed, String productName) {
        mControlManager.controlDevice(deviceId, scenarioOrSpeed, productName);
    }

    public String getControlModePre(int deviceId) {
        return SharePreferenceUtil.getPrefString(HPlusConstants.PREFERENCE_DEVICE_CONTROL_MODE,
                Integer.toString(deviceId), HPlusConstants.DEVICE_CONTROL_NORMAL);
    }

    public boolean getIsFlashing(int deviceId) {
        return SharePreferenceUtil.getPrefBoolean(HPlusConstants.PREFERENCE_DEVICE_CONTROL_FLASH,
                Integer.toString(deviceId), DeviceMode.IS_REFLASHING);
    }

    public void controlWaterDevice(int deviceId, int scenario, String productName) {
        mControlManager.controlWaterDevice(deviceId, scenario, productName);
    }

    public UserLocationData getLocationWithId(int mLocationId){
       return UserDataOperator.getLocationWithId(mLocationId,UserAllDataContainer.shareInstance().getUserLocationDataList(),UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList());
    }

    public HomeDevice getHomeDeviceByDeviceId(int deviceId){
        return UserDataOperator.getDeviceWithDeviceId(deviceId,UserAllDataContainer.shareInstance().getUserLocationDataList(),UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList());
    }

    public Map<Integer, AirtouchCapability> getDeviceCapabilityMap(){
       return UserAllDataContainer.shareInstance().getAirtouchCapabilityMap();
    }

    @Override
    public Map<Integer, SmartROCapability> getSmartRoCapabilityMap() {
       return UserAllDataContainer.shareInstance().getSmartROCapabilityMap();
    }

    public int getMadAirDeviceImg(MadAirDeviceModel madAirDeviceModel){
        switch (madAirDeviceModel.getModelName()){
            case HPlusConstants.MAD_AIR_MODEL_WHITE:
                return R.drawable.mad_air_detail_white;
            case HPlusConstants.MAD_AIR_MODEL_BLACK:
                return R.drawable.mad_air_detail_black;
            case HPlusConstants.MAD_AIR_MODEL_PINK:
                return R.drawable.mad_air_detail_pink;
            case HPlusConstants.MAD_AIR_MODEL_SKULL:
                return R.drawable.mad_air_detail_skull;
            default:
                return R.drawable.mad_air_detail_white;
        }
    }

    public boolean isDuplicateMadAirAddress(String deviceName) {
        List<MadAirDeviceModel> madAirDeviceModels = MadAirDeviceModelSharedPreference.getDeviceList();
        if (madAirDeviceModels.size() != 0) {
            for (MadAirDeviceModel madAirDeviceModel : madAirDeviceModels) {
                if (deviceName.equals(madAirDeviceModel.getDeviceName())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public  void saveDeviceName(String macAddress, String name){
        MadAirDeviceModelSharedPreference.saveDeviceName(macAddress, name);

    }
}
