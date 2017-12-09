package com.honeywell.hch.airtouch.plateform.http.manager.model;

import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModelSharedPreference;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;

import java.util.List;

/**
 * Created by Vincent on 29/11/16.
 */

public class MadAirModelManager {

    /*
        读取本地口罩的数据，并封装数据
     */
    public void readMadAirData(List<VirtualUserLocationData> mUserLocationDataList) {
        List<MadAirDeviceModel> madAirDeviceModels = MadAirDeviceModelSharedPreference.getDeviceList();
        if (madAirDeviceModels.size() != 0) {
            for (MadAirDeviceModel madAirDeviceModel : madAirDeviceModels) {
                if (MadAirDeviceModelSharedPreference.hasDevice(madAirDeviceModel.getMacAddress())) {
                    boolean isAddNew = true;

                    for (UserLocationData userLocationData : mUserLocationDataList) {
                        if (userLocationData instanceof VirtualUserLocationData) {
                            if (((VirtualUserLocationData) userLocationData).getMacAddress().equals(madAirDeviceModel.getMacAddress())) {
                                ((VirtualUserLocationData) userLocationData).setMadAirDeviceModel(madAirDeviceModel);
                                isAddNew = false;
                            }
                        }
                    }
                    if (isAddNew) {
                        VirtualUserLocationData virtualUserLocationData = new VirtualUserLocationData();
                        virtualUserLocationData.setMadAirDeviceObject(madAirDeviceModel);
                        mUserLocationDataList.add(virtualUserLocationData);
                    }
                }
            }
        }

    }

    public void deleteMadAirDeviceForHomePage(String macAddress) {
        List<VirtualUserLocationData> mUserLocationDataList = UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList();
        for (UserLocationData userLocationData : mUserLocationDataList) {
            if (macAddress.equals(((VirtualUserLocationData) userLocationData).getMacAddress())) {
                mUserLocationDataList.remove(userLocationData);
            }
        }

    }

}
