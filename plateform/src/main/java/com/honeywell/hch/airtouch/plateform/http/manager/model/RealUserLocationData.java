package com.honeywell.hch.airtouch.plateform.http.manager.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 23/11/16.
 */

public class RealUserLocationData extends UserLocationData {

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String mCity) {
        this.mCity = mCity;
    }

    public ArrayList<SelectStatusDeviceItem> getSelectDeviceList() {
        return mSelectDeviceList;
    }

    public void setSelectDeviceList(ArrayList<SelectStatusDeviceItem> mSelectDeviceList) {
        this.mSelectDeviceList = (ArrayList<SelectStatusDeviceItem>) mSelectDeviceList.clone();
    }

    @Override
    public String getAllDeviceTitleName() {
        return mName;
    }

    public boolean isIsLocationOwner() {
        return mIsLocationOwner;
    }

    public void setIsLocationOwner(boolean mIsLocationOwner) {
        this.mIsLocationOwner = mIsLocationOwner;
    }

    public void setHomeDevicesList(ArrayList<HomeDevice> homeDevicesList) {

        List<HomeDevice> addList = new ArrayList<>();
        List<HomeDevice> deleList = new ArrayList<>();

        for (HomeDevice homeDevice : homeDevicesList) {
            int deviceId = homeDevice.getDeviceInfo().getDeviceID();
            boolean isHasSame = false;
            for (HomeDevice item : mHomeDevicesList) {
                if (item.getDeviceInfo().getDeviceID() == deviceId) {
                    isHasSame = true;

                    //update home device info
                    updateDeviceInfo(item, homeDevice);
                    continue;
                }
            }
            if (!isHasSame) {
                addList.add(homeDevice);
            }

        }

        for (HomeDevice item : mHomeDevicesList) {
            int deviceId = item.getDeviceInfo().getDeviceID();
            boolean isHasSame = false;
            for (HomeDevice homeDevice : homeDevicesList) {
                if (homeDevice.getDeviceInfo().getDeviceID() == deviceId) {
                    isHasSame = true;
                    continue;
                }
            }
            if (!isHasSame) {
                deleList.add(item);
            }
        }

        if (addList.size() > 0) {
            mHomeDevicesList.addAll(addList);
        }
        if (deleList.size() > 0) {
            mHomeDevicesList.removeAll(deleList);
        }
    }

}
