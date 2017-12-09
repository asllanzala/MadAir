package com.honeywell.hch.airtouch.plateform.http.manager.model;

import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

import java.util.ArrayList;

/**
 * Created by Vincent on 23/11/16.
 */

public class VirtualUserLocationData extends UserLocationData {
//    public static final int VIRTUAL_LOCATION_ID = 10000;

    {
//        mLocationID = VIRTUAL_LOCATION_ID; //常量
        mAllDeviceGetStatus = UserAllDataContainer.LOADED_SUCCESS_STATUS;
    }

    MadAirDeviceObject madAirDeviceObject;

//    ArrayList<HomeDevice> madAirDeviceModelArrayList = new ArrayList();  //All device使用

    public MadAirDeviceModel getMadAirDeviceModel() {
        return madAirDeviceObject.getmMadAirDeviceModel();
    }

    public void setMadAirDeviceModel(MadAirDeviceModel madAirDeviceModel) {
        madAirDeviceObject.setmMadAirDeviceModel(madAirDeviceModel);
    }

    public String getName() {
        return madAirDeviceObject.getmMadAirDeviceModel().getDeviceName();
    }

    public void setName(String mName) {
        madAirDeviceObject.getmMadAirDeviceModel().setDeviceName(mName);
    }

    public String getMacAddress() {
        return madAirDeviceObject.getmMadAirDeviceModel().getMacAddress();
    }

    public String getCity() {
        if (UserInfoSharePreference.getIsUsingGpsCityCode()) {
            return UserInfoSharePreference.getGpsCityCode();
        } else {
            return UserInfoSharePreference.getManualCityCode();
        }
    }

    //不需要调用
    public void setCity(String mCity) {
        UserInfoSharePreference.saveGpsCityCode(mCity);
    }

    @Override
    public ArrayList<SelectStatusDeviceItem> getSelectDeviceList() {
        ArrayList<SelectStatusDeviceItem> selectStatusDeviceItemArrayList = new ArrayList<>();
        for (int i = 0; i < mHomeDevicesList.size(); i++) {
            SelectStatusDeviceItem selectStatusDeviceItem = new SelectStatusDeviceItem();
            selectStatusDeviceItem.setIsSelected(false);
            selectStatusDeviceItem.setDeviceItem(mHomeDevicesList.get(i));
            selectStatusDeviceItemArrayList.add(selectStatusDeviceItem);
        }

        return selectStatusDeviceItemArrayList;
    }

    @Override
    public void setSelectDeviceList(ArrayList<SelectStatusDeviceItem> mSelectDeviceList) {

    }

    public void setHomeDevicesList(ArrayList<HomeDevice> homeDevicesList) {
        this.mHomeDevicesList = homeDevicesList;
    }

    public void setHomeDevicesList(MadAirDeviceModel madAirDeviceModel) {
        mHomeDevicesList.add(new MadAirDeviceObject(madAirDeviceModel));
    }

    @Override
    public String getAllDeviceTitleName() {
        return AppManager.getInstance().getApplication().getString(R.string.mad_air_portable_devices);
    }

    public ArrayList<HomeDevice> getHomeDevicesList() {
        return mHomeDevicesList;
    }

    public MadAirDeviceObject getMadAirDeviceObject() {
        return madAirDeviceObject;
    }

    public void setMadAirDeviceObject(MadAirDeviceModel madAirDeviceModel) {
        this.madAirDeviceObject = new MadAirDeviceObject(madAirDeviceModel);
    }

    public void setMadAirDeviceObject(MadAirDeviceObject madAirDeviceObject) {
        this.madAirDeviceObject = madAirDeviceObject;
    }

    public boolean isIsLocationOwner() {
        return true;
    }

    public void setIsLocationOwner(boolean mIsLocationOwner) {
        this.mIsLocationOwner = mIsLocationOwner;
    }

    public int getLocationID() {
        if (madAirDeviceObject != null) {
            return madAirDeviceObject.getDeviceId();
        } else {
            return mHomeDevicesList.get(0).getDeviceId();
        }
    }

}
