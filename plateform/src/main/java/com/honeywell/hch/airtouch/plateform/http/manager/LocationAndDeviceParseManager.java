package com.honeywell.hch.airtouch.plateform.http.manager;

import com.google.gson.Gson;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.devices.airtouch.model.AirtouchRunStatus;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.plateform.devices.common.HPlusDeviceFactory;
import com.honeywell.hch.airtouch.plateform.devices.water.model.WaterDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceGroupItem;
import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceInfo;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.manager.model.RealUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.SelectStatusDeviceItem;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.device.AquaTouchRunstatus;
import com.honeywell.hch.airtouch.plateform.http.model.device.DeviceRunStatus;
import com.honeywell.hch.airtouch.plateform.http.model.device.DeviceTypeConfigInfo;
import com.honeywell.hch.airtouch.plateform.http.model.device.LocationsDevicesDetailInfo;
import com.honeywell.hch.airtouch.plateform.http.model.group.DevicesForGroupResponse;
import com.honeywell.hch.airtouch.plateform.http.model.group.GroupData;
import com.honeywell.hch.airtouch.plateform.http.model.group.GroupDataResponse;
import com.honeywell.hch.airtouch.plateform.http.model.message.MessageModel;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.UserLocation;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by h127856 on 16/9/18.
 */
public class LocationAndDeviceParseManager {

    private final int COMMOM_DEVICE = 0;

    private List<UserLocationData> mUserLocationDataList = UserAllDataContainer.shareInstance().getUserLocationDataList();
    //是否需要重新排序，只有有个数增加的时候才进行一次排序
    private boolean isNeedSort = false;

    private static LocationAndDeviceParseManager mUserLocationRelativeManager;

    public static LocationAndDeviceParseManager getInstance() {
        if (mUserLocationRelativeManager == null) {
            mUserLocationRelativeManager = new LocationAndDeviceParseManager();
        }
        return mUserLocationRelativeManager;
    }


    public void parseJsonDataToUserLocationObject(String responseData) throws JSONException {

        if (StringUtil.isEmpty(responseData)) {
            return;
        }
        JSONArray responseArray = new JSONArray(responseData);

        List<UserLocationData> tempList = new ArrayList<>();
        for (int i = 0; i < responseArray.length(); i++) {
            JSONObject responseJSON = responseArray.getJSONObject(i);
            UserLocation userLocation = new Gson().fromJson(responseJSON.toString(), UserLocation.class);
            addLocationDataFromGetLocationAPI(userLocation, tempList);
        }

        updateUsrdataList(tempList);

    }

    public void parseJsonDataToRunstatusObject(String responseData) throws JSONException {
        if (StringUtil.isEmpty(responseData)) {
            return;
        }
        JSONArray responseArray = new JSONArray(responseData);
        List<LocationsDevicesDetailInfo> tempList = new ArrayList<>();
        for (int i = 0; i < responseArray.length(); i++) {
            JSONObject responseJSON = responseArray.getJSONObject(i);

            //解析最外层
            LocationsDevicesDetailInfo locationsDevicesDetailInfo
                    = new Gson().fromJson(responseJSON.toString(), LocationsDevicesDetailInfo.class);
            tempList.add(locationsDevicesDetailInfo);
        }
        setDeviceRunstatus(tempList);
    }

    public List<MessageModel> parseJsonDataToMessageSObject(String responseData) throws JSONException {
        JSONArray responseArray = new JSONArray(responseData);
        List<MessageModel> tempList = new ArrayList<>();
        for (int i = 0; i < responseArray.length(); i++) {
            JSONObject responseJSON = responseArray.getJSONObject(i);

            //解析最外层
            MessageModel messageModel
                    = new Gson().fromJson(responseJSON.toString(), MessageModel.class);
            tempList.add(messageModel);
        }
        UserAllDataContainer.shareInstance().setLoadMessageResponseList((ArrayList<MessageModel>) tempList);
        return tempList;
    }

    public void parseJsonDataToGroupData(int locationId, String responseData, boolean isFromCache) {
        UserLocationData userLocationData = UserDataOperator.getUserLocationByID(locationId,UserAllDataContainer.shareInstance().getUserLocationDataList());
        if ((userLocationData == null) || StringUtil.isEmpty(responseData)) {
            return;
        }
        final List<HomeDevice> mSortHomeDevices = sortDevices(userLocationData.getHomeDevicesList());
        GroupData groupData = new Gson().fromJson(responseData, GroupData.class);
        List<DevicesForGroupResponse> unGroupDeviceList = groupData.getUnGroupDeviceList();
        List<GroupDataResponse> mGroupLists = groupData.getGroupList();

        if (mGroupLists != null) {
            if (isFromCache) {
                userLocationData.setDeviceCacheDataLoadSuccess();
            } else {
                userLocationData.setDeviceNetworkDataLoadSuccess();
            }
            ArrayList<DeviceGroupItem> deviceGroupTempList = new ArrayList<>();
            for (GroupDataResponse oneGroup : mGroupLists) {

                //data from GroupDataResponse to deviceGroupItem
                DeviceGroupItem deviceGroupItem = changeGroupDataResponseToDeviceGroupItem(oneGroup);

                List<DevicesForGroupResponse> groupDeviceList = oneGroup.getGroupDeviceList();
                for (HomeDevice homeDevice : mSortHomeDevices) {
                    for (DevicesForGroupResponse groupDevice : groupDeviceList) {
                        if (groupDevice.getDeviceId() == homeDevice.getDeviceInfo().getDeviceID()) {
                            // store group Air Premium and other devices isMaster or not
                            homeDevice.setIsMasterDevice(
                                    groupDevice.getIsMasterDevice());
                            SelectStatusDeviceItem deviceMode = new SelectStatusDeviceItem();
                            deviceMode.setDeviceItem(homeDevice);
                            deviceGroupItem.addDeviceToHomeList(deviceMode);
                        }
                    }
                }
                deviceGroupTempList.add(deviceGroupItem);

            }
            userLocationData.setDeviceGroupList(deviceGroupTempList);

            ArrayList<SelectStatusDeviceItem> deviceTempList = new ArrayList<>();
            // bind unGroupList into mUnGroupHomeDevices data
            for (HomeDevice homeDevice : mSortHomeDevices) {
                for (DevicesForGroupResponse unGroupDevice : unGroupDeviceList) {
                    if (homeDevice.getDeviceInfo().getDeviceID() == unGroupDevice.getDeviceId()) {
                        homeDevice.setIsMasterDevice(COMMOM_DEVICE);
                        SelectStatusDeviceItem deviceMode = new SelectStatusDeviceItem();
                        deviceMode.setDeviceItem(homeDevice);
                        deviceTempList.add(deviceMode);
                    }
                }
            }
            userLocationData.setSelectDeviceList(deviceTempList);
            return;
        }
        if (!isFromCache) {
            userLocationData.setDeviceListLoadFailed();
        }

    }

    public void parseJsonToDeviceConfig(String responseData) throws JSONException {
        JSONArray responseArray = new JSONArray(responseData);
        List<DeviceTypeConfigInfo> tempList = new ArrayList<>();
        for (int i = 0; i < responseArray.length(); i++) {
            JSONObject responseJSON = responseArray.getJSONObject(i);

            //解析最外层
            DeviceTypeConfigInfo deviceTypeConfigInfo
                    = new Gson().fromJson(responseJSON.toString(), DeviceTypeConfigInfo.class);
            tempList.add(deviceTypeConfigInfo);
        }
        UserDataOperator.setDeviceConfig(tempList,UserAllDataContainer.shareInstance().getAirtouchCapabilityMap()
        ,UserAllDataContainer.shareInstance().getSmartROCapabilityMap());
    }

    /**
     * sort devices int userLocalData for that the airtouchX should be in the first
     * 优先显示 错误的，之后是不支持，再是可以成组的设备
     *
     * @param rawList
     * @return
     */
    private List<HomeDevice> sortDevices(List<HomeDevice> rawList) {
        List<HomeDevice> targetList = new ArrayList<>();

        for (HomeDevice homeDevice : rawList) {
            if (!homeDevice.getiDeviceStatusFeature().isNormal())
                targetList.add(homeDevice);
        }

        for (HomeDevice homeDevice : rawList) {
            if (!DeviceType.isHplusSeries(homeDevice.getDeviceType()) && !targetList.contains(homeDevice))
                targetList.add(homeDevice);
        }


        for (HomeDevice homeDevice : rawList) {
            if (DeviceType.isAirTouchX(homeDevice.getDeviceType()) && !targetList.contains(homeDevice))
                targetList.add(homeDevice);
        }
        for (HomeDevice homeDevice : rawList) {
            if (!targetList.contains(homeDevice))
                targetList.add(homeDevice);
        }
        return targetList;
    }


    private DeviceGroupItem changeGroupDataResponseToDeviceGroupItem(GroupDataResponse oneGroup) {

        DeviceGroupItem deviceGroupItem = new DeviceGroupItem();
        deviceGroupItem.initFromGroupDataResponseExcHomeDevice(oneGroup);
        return deviceGroupItem;
    }


    /**
     * @param tempList
     */
    public void updateUsrdataList(List<UserLocationData> tempList) {
        mUserLocationDataList = UserAllDataContainer.shareInstance().getUserLocationDataList();
        //check the location will delete
        List<UserLocationData> deleteList = new ArrayList<>();
        for (UserLocationData userLocationData : mUserLocationDataList) {
            int locationId = userLocationData.getLocationID();
            boolean isHasSame = false;
            for (UserLocationData tempItem : tempList) {
                if (tempItem.getLocationID() == locationId) {
                    updateUserLocationDataItem(userLocationData, tempItem);
                    isHasSame = true;
                    break;
                }
            }
            if (!isHasSame) {
                deleteList.add(userLocationData);
            }
        }

        if (deleteList.size() > 0) {
            mUserLocationDataList.removeAll(deleteList);
            isNeedSort = true;
        }

        //add new
        List<UserLocationData> addList = new ArrayList<>();
        for (UserLocationData tempItem : tempList) {
            int locationId = tempItem.getLocationID();
            tempItem.setCityWeatherData(UserAllDataContainer.shareInstance().getWeatherRefreshManager().getWeatherPageDataHashMap().get(tempItem.getCity()));
            boolean isHasSame = false;
            for (UserLocationData userLocationData : mUserLocationDataList) {
                if (userLocationData.getLocationID() == locationId) {
                    isHasSame = true;
                    break;
                }
            }
            if (!isHasSame) {
                //get the default device
                addList.add(tempItem);
            }
        }

        if (addList.size() > 0) {
            mUserLocationDataList.addAll(addList);
            isNeedSort = true;
        }


        copyOnWriteArrayListSort();
        UserAllDataContainer.shareInstance().getWeatherRefreshManager().addCityListRefresh(getCityList(mUserLocationDataList), false);

        //刷新MadAir的城市天气
        List<String> madAirCityList = new ArrayList<>();
        if (UserInfoSharePreference.getIsUsingGpsCityCode() && !StringUtil.isEmpty(UserInfoSharePreference.getGpsCityCode())){
            madAirCityList.add(UserInfoSharePreference.getGpsCityCode());
        }else if (!UserInfoSharePreference.getIsUsingGpsCityCode() && !StringUtil.isEmpty(UserInfoSharePreference.getManualCityCode())){
            madAirCityList.add(UserInfoSharePreference.getManualCityCode());
        }
        if (madAirCityList.size() > 0){
            UserAllDataContainer.shareInstance().getWeatherRefreshManager().addCityListRefresh(madAirCityList, false);
        }

    }

    private void copyOnWriteArrayListSort() {
        if (isNeedSort) {
            Object[] a = mUserLocationDataList.toArray();
            Arrays.sort(a);
            for (int i = 0; i < a.length; i++) {
                mUserLocationDataList.set(i, (UserLocationData) a[i]);
            }
            isNeedSort = false;
        }

    }

    private void updateUserLocationDataItem(UserLocationData srcData, UserLocationData destData) {
        srcData.setName(destData.getName());
        srcData.setOperationModel(destData.getOperationModel());
        srcData.setHomeDevicesList(destData.getHomeDevicesList());
        srcData.setIsLocationOwner(destData.isIsLocationOwner());
    }

    /**
     * @param userLocation
     */
    public void addLocationDataFromGetLocationAPI(UserLocation userLocation, List<UserLocationData> tempList) {
        UserLocationData userLocationDataItem = new RealUserLocationData();
        if (userLocation == null || userLocation.getDeviceInfo() == null) {
            return;
        }

        for (DeviceInfo deviceInfoItem : userLocation.getDeviceInfo()) {

            HomeDevice homeDevice = HPlusDeviceFactory.createHPlusDeviceObject(deviceInfoItem);

            initDefaultDevice(userLocationDataItem, homeDevice);
            userLocationDataItem.addHomeDeviceItemToList(homeDevice);

            if (!userLocation.getIsLocationOwner() && StringUtil.isEmpty(userLocationDataItem.getLocationOwnerName())) {
                userLocationDataItem.setIsLocationOwner(userLocation.getIsLocationOwner());
                userLocationDataItem.setLocationOwnerName(deviceInfoItem.getOwnerName());
            }
        }
        initUnsupportDevice(userLocationDataItem);
        userLocationDataItem.setOperationModel(userLocation.getScenarioOperation());
        userLocationDataItem.addHomeListFromDeviceInfoList(userLocation.getDeviceInfo());
        userLocationDataItem.setName(userLocation.getName());
        userLocationDataItem.setCity(userLocation.getCity());
        userLocationDataItem.setLocationID(userLocation.getLocationID());

        userLocationDataItem.setCityWeatherData(UserAllDataContainer.shareInstance().getWeatherRefreshManager().getWeatherPageDataHashMap().get(userLocation.getCity()));
        tempList.add(userLocationDataItem);
    }

    /**
     * 获取default设备
     *
     * @param userLocationDataItem
     * @param homeDevice
     */
    private void initDefaultDevice(UserLocationData userLocationDataItem, HomeDevice homeDevice) {
        if (userLocationDataItem.getDefaultPMDevice() == null && homeDevice instanceof AirTouchDeviceObject) {
            userLocationDataItem.setDefaultPMDevice((AirTouchDeviceObject) homeDevice);
        }
        if (userLocationDataItem.getDefaultTVOCDevice() == null && homeDevice instanceof AirTouchDeviceObject) {
            if (((AirTouchDeviceObject) homeDevice).canShowTvoc()) {
                userLocationDataItem.setDefaultTVOCDevice((AirTouchDeviceObject) homeDevice);
            }
        }
        if (userLocationDataItem.getDefaultWaterDevice() == null && homeDevice instanceof WaterDeviceObject) {
            userLocationDataItem.setDefaultWaterDevice((WaterDeviceObject) homeDevice);
        }
    }

    private void initUnsupportDevice(UserLocationData userLocationDataItem) {
        if (userLocationDataItem.getDefaultWaterDevice() == null && userLocationDataItem.getDefaultPMDevice() == null
                && userLocationDataItem.getDefaultTVOCDevice() == null && userLocationDataItem.getHomeDevicesList().size() > 0) {
            userLocationDataItem.setUnSupportDevice(userLocationDataItem.getHomeDevicesList().get(0));
        }
    }

    private List<String> getCityList(List<UserLocationData> userLocations) {
        List<String> cityList = new ArrayList<>();
        for (UserLocationData userLocationData : userLocations) {
            cityList.add(userLocationData.getCity());
        }
        return cityList;
    }


    /**
     * 根据新的smart ro api，传入location id的数组，返回对应location id下所有设备的runstatus信息。
     * 这里把返回回来的ruanstatus数组进行解析，根据device type设置不同的HomeDevice 的runStatus
     */
    public void setDeviceRunstatus(List<LocationsDevicesDetailInfo> locationsDevicesDetailInfoList) {
        mUserLocationDataList = UserAllDataContainer.shareInstance().getUserLocationDataList();
        if (locationsDevicesDetailInfoList != null) {
            for (LocationsDevicesDetailInfo locationsDevicesDetailInfo : locationsDevicesDetailInfoList) {
                int locationId = locationsDevicesDetailInfo.getLocationId();
                for (UserLocationData userLocationData : mUserLocationDataList) {
                    if (userLocationData.getLocationID() == locationId) {
                        //
                        parseCertainHomeDeviceRunstatus(locationsDevicesDetailInfo, userLocationData);
                        break;
                    }
                }
            }
        }

    }


    private void parseCertainHomeDeviceRunstatus(LocationsDevicesDetailInfo locationsDevicesDetailInfo, UserLocationData userLocationData) {

        for (HomeDevice device : userLocationData.getHomeDevicesList()) {
            int deviceId = device.getDeviceInfo().getDeviceID();

            for (DeviceRunStatus deviceRunStatus : locationsDevicesDetailInfo.getDeviceRunStatusList()) {
                if (deviceRunStatus.getDeviceID() == deviceId) {

                    if (DeviceType.isAirTouchSeries(device.getDeviceType())) {
                        AirtouchRunStatus airtouchRunstatus = new Gson().fromJson(deviceRunStatus.getDeviceRunStatus(), AirtouchRunStatus.class);

                        ((AirTouchDeviceObject) device).setAirtouchDeviceRunStatus(airtouchRunstatus);
                    } else if (DeviceType.isWaterSeries(device.getDeviceType())) {
                        AquaTouchRunstatus aquaTouchRunstatus = new Gson().fromJson(deviceRunStatus.getDeviceRunStatus(), AquaTouchRunstatus.class);
                        ((WaterDeviceObject) device).setAquaTouchRunstatus(aquaTouchRunstatus);
                    }

                }
            }
        }

    }

}
