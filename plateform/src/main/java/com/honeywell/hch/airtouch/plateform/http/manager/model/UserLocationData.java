package com.honeywell.hch.airtouch.plateform.http.manager.model;


import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.plateform.devices.common.HPlusDeviceFactory;
import com.honeywell.hch.airtouch.plateform.devices.water.model.UnSupportDeviceObject;
import com.honeywell.hch.airtouch.plateform.devices.water.model.WaterDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.model.weather.WeatherPageData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuyuan on 10/10/15.
 * by Stephen(H127856)
 */
public abstract class UserLocationData implements Comparable {
    private final String TAG = "UserLocationData";
    protected int mLocationID;

    protected String mName = "";

    // city code (i.e. CHSH00000)
    protected String mCity;

    protected ArrayList<HomeDevice> mHomeDevicesList = new ArrayList<>();

    private BackgroundData mCityBackgroundDta;

    private WeatherPageData mCityWeatherData;


    /**
     * the location is owner or not
     */
    protected boolean mIsLocationOwner = true;

    private String mLocationOwnerName = "";

    private AirTouchDeviceObject mDefaultPMDevice;

    private WaterDeviceObject mDefaultWaterDevice;

    private AirTouchDeviceObject mDefaultTVOCDevice;

    private HomeDevice mUnSupportDevice;

    private int mOperationModel;

    private List<DeviceGroupItem> mDeviceGroupList = new ArrayList<>();

    protected ArrayList<SelectStatusDeviceItem> mSelectDeviceList = new ArrayList<>();

    /**
     * 用于界面显示加载中，加载失败等状态
     */
    protected int mAllDeviceGetStatus = UserAllDataContainer.LOADING_STATUS;


    public UserLocationData() {
        mCityBackgroundDta = new BackgroundData();
    }


    public ArrayList<AirTouchDeviceObject> getAirTouchSeriesList() {
        ArrayList<AirTouchDeviceObject> airTouchSDevicesList = new ArrayList<>();
        for (HomeDevice homeDevice : mHomeDevicesList) {
            if (DeviceType.isAirTouchSeries(homeDevice.getDeviceType())) {
                airTouchSDevicesList.add((AirTouchDeviceObject) homeDevice);
            }
        }
        return airTouchSDevicesList;
    }

    public ArrayList<WaterDeviceObject> getWaterDeviceList() {
        ArrayList<WaterDeviceObject> waterDeviceObjects = new ArrayList<>();
        for (HomeDevice homeDevice : mHomeDevicesList) {
            if (DeviceType.isWaterSeries(homeDevice.getDeviceType())) {
                waterDeviceObjects.add((WaterDeviceObject) homeDevice);
            }
        }
        return waterDeviceObjects;
    }

    public boolean isOwnAirTouchSeries() {
        for (HomeDevice homeDevice : mHomeDevicesList) {
            if (DeviceType.isAirTouchSeries(homeDevice.getDeviceType())) {
                return true;
            }
        }
        return false;
    }

    public boolean isAllAirTouch100GSeries() {
        for (HomeDevice homeDevice : mHomeDevicesList) {
            if (homeDevice.getControlFeature().isCanControlable()) {
                return false;
            }
        }
        return true;
    }

    public boolean isOwnWaterSeries() {
        for (HomeDevice homeDevice : mHomeDevicesList) {
            if (DeviceType.isWaterSeries(homeDevice.getDeviceType())) {
                return true;
            }
        }
        return false;
    }

    public boolean isAllDeviceOffline() {
        for (HomeDevice homeDevice : mHomeDevicesList) {
            if (DeviceType.isAirTouchSeries(homeDevice.getDeviceType()) &&
                    !homeDevice.getiDeviceStatusFeature().isOffline()) {
                return false;
            }
        }
        return true;
    }

    /**
     * get device with device id
     *
     * @param deviceID
     * @return
     */
    public HomeDevice getHomeDeviceWithDeviceId(int deviceID) {
        for (HomeDevice homeDevice : mHomeDevicesList) {
            if (homeDevice.getDeviceInfo().getDeviceID() == deviceID) {
                return homeDevice;
            }
        }
        return null;
    }


    /**
     * get the child class of base class: HomeDevice according deviceId
     *
     * @param deviceId
     * @return
     */
    public HomeDevice getHomeDeviceWithId(int deviceId) {
        for (HomeDevice homeDevice : mHomeDevicesList) {
            if (homeDevice.getDeviceInfo().getDeviceID() == deviceId) {
                return homeDevice;
            }
        }
        return null;
    }


    /**
     * if the location contain the airtouchs device,return true,else return false;
     *
     * @return
     */
    public boolean isHaveDeviceInThisLocation() {
        if (mHomeDevicesList != null && mHomeDevicesList.size() > 0) {
            return true;
        }
        return false;
    }

    public boolean isHaveSelectedDeviceThisLocation() {
        if (mSelectDeviceList.size() > 0 || mDeviceGroupList.size() > 0) {
            return true;
        }
        return false;
    }


    public int getLocationID() {
        return mLocationID;
    }

    public void setLocationID(int mLocationID) {
        this.mLocationID = mLocationID;
    }

    public abstract String getName();

    public abstract void setName(String mName);

    public abstract String getCity();

    public abstract void setCity(String mCity);

    public ArrayList<HomeDevice> getHomeDevicesList() {
        return mHomeDevicesList;
    }

    public abstract void setHomeDevicesList(ArrayList<HomeDevice> homeDevicesList);

    public void updateDeviceInfo(HomeDevice src, HomeDevice des) {
        src.setDeviceInfo(des.getDeviceInfo());
        //AIRQA-1573, the value of isMasterDevice set here is always 0, caused value changed 1->0 after refresh.
//        src.setIsMasterDevice(des.getIsMasterDevice());

        if (src instanceof AirTouchDeviceObject) {
            if (((AirTouchDeviceObject) src).getAirtouchDeviceRunStatus() == null) {
                ((AirTouchDeviceObject) src).setAirtouchDeviceRunStatus(((AirTouchDeviceObject) des).getAirtouchDeviceRunStatus());
            }
        } else if (src instanceof WaterDeviceObject) {
            if (((WaterDeviceObject) src).getAquaTouchRunstatus() == null) {
                ((WaterDeviceObject) src).setAquaTouchRunstatus(((WaterDeviceObject) des).getAquaTouchRunstatus());
            }
        }

    }

    public void addHomeDeviceItemToList(HomeDevice mHomeDevicesItem) {
        this.mHomeDevicesList.add(mHomeDevicesItem);
    }

    public BackgroundData getCityBackgroundDta() {
        return mCityBackgroundDta;
    }


    public int getOperationModel() {
        return mOperationModel;
    }

    public void setOperationModel(int mOperationModel) {
        this.mOperationModel = mOperationModel;
    }

    public void setCityWeatherData(WeatherPageData mCityWeatherData) {
        this.mCityWeatherData = mCityWeatherData;
    }


    public List<DeviceGroupItem> getDeviceGroupList() {
        return mDeviceGroupList;
    }

    public void setDeviceGroupList(ArrayList<DeviceGroupItem> mDeviceGroupList) {
        this.mDeviceGroupList = (ArrayList<DeviceGroupItem>) mDeviceGroupList.clone();
    }

    public abstract ArrayList<SelectStatusDeviceItem> getSelectDeviceList();

    public abstract void setSelectDeviceList(ArrayList<SelectStatusDeviceItem> mSelectDeviceList);


    public AirTouchDeviceObject getDefaultPMDevice() {

        return mDefaultPMDevice;
    }


    public boolean isOutDoorPmWorse() {
        if (mCityWeatherData != null && mCityWeatherData.getWeather() != null && mCityWeatherData.getWeather().getNow() != null) {
            return Integer.valueOf(mCityWeatherData.getWeather().getNow().getAirQuality()
                    .getAirQualityIndex().getPm25()) > HPlusConstants.OUTDOOR_PM25_MAX;
        }
        return false;
    }

    public List<Integer> getOffDeviceIdList() {
        // out door pm25 value is not considered from time being.
        boolean isOutdoorOpen = isOutDoorPmWorse();
//        boolean isOutdoorOpen = false;

        List<Integer> deviceIdList = new ArrayList<>();
        if (mHomeDevicesList != null && mHomeDevicesList.size() > 0) {
            for (HomeDevice homeDevice : mHomeDevicesList) {
                if (homeDevice instanceof AirTouchDeviceObject) {
                    if (!AppManager.getLocalProtocol().getRole().canControlDevice(homeDevice.getDeviceInfo())) {
                        continue;
                    }

                    boolean isIndoorOpen = (homeDevice != null &&
                            ((AirTouchDeviceObject) homeDevice).getAirtouchDeviceRunStatus() != null
                            && ((AirTouchDeviceObject) homeDevice).getAirtouchDeviceRunStatus().getmPM25Value() > HPlusConstants.MAX_PMVALUE_LOW)
                            && (((AirTouchDeviceObject) homeDevice).getAirtouchDeviceRunStatus().getmPM25Value() < 9999);

                    boolean isAlive = false;
                    if (homeDevice.getDeviceInfo() != null) {
                        isAlive = homeDevice.getDeviceInfo().getIsAlive();
                    }

                    if (((AirTouchDeviceObject) homeDevice).getAirtouchDeviceRunStatus() != null
                            && ((AirTouchDeviceObject) homeDevice).getAirtouchDeviceRunStatus().getScenarioMode() == HPlusConstants.MODE_OFF_INT
                            && isAlive) {

                        if (isOutdoorOpen || isIndoorOpen) {
                            deviceIdList.add(homeDevice.getDeviceInfo().getDeviceID());
                        }
                    } else if (((AirTouchDeviceObject) homeDevice).getAirtouchDeviceRunStatus() == null && isAlive) {
                        if (isOutdoorOpen || isIndoorOpen) {
                            deviceIdList.add(homeDevice.getDeviceInfo().getDeviceID());
                        }
                    }
                }

            }
        }
        return deviceIdList;
    }


    public boolean isIsLocationOwner() {
        return mIsLocationOwner;
    }

    public void setIsLocationOwner(boolean mIsLocationOwner) {
        this.mIsLocationOwner = mIsLocationOwner;
    }

    public String getLocationOwnerName() {
        return mLocationOwnerName;
    }

    public void setLocationOwnerName(String mLoationOwnerName) {
        this.mLocationOwnerName = mLoationOwnerName;
    }


    public void addHomeListFromDeviceInfoList(ArrayList<DeviceInfo> deviceInfoArrayList) {
        boolean isHasSame = false;
        List<HomeDevice> tempAddList = new ArrayList<>();

        for (DeviceInfo deviceInfo : deviceInfoArrayList) {
            int deviceId = deviceInfo.getDeviceID();

            for (HomeDevice homeDevice : mHomeDevicesList) {
                if (deviceId == homeDevice.getDeviceInfo().getDeviceID()) {
                    isHasSame = true;
                    break;
                }
            }

            if (!isHasSame) {
                HomeDevice newHomeDevice = HPlusDeviceFactory.createHPlusDeviceObject(deviceInfo);
                tempAddList.add(newHomeDevice);
            }
        }

        if (tempAddList.size() > 0) {
            mHomeDevicesList.addAll(tempAddList);
        }

    }

    @Override
    public int compareTo(Object another) {
        if (isIsLocationOwner()) {
            if (another != null && !((UserLocationData) another).isIsLocationOwner()) {
                return -1;
            }
        } else {
            if (another != null && ((UserLocationData) another).isIsLocationOwner()) {
                return 1;
            } else if (another != null && !((UserLocationData) another).isIsLocationOwner()) {
                return this.getLocationOwnerName().compareTo(((UserLocationData) another).getLocationOwnerName());
            }
        }

        return 0;
    }


    public void setDefaultPMDevice(AirTouchDeviceObject defaultDevice) {
        this.mDefaultPMDevice = defaultDevice;
    }

    public WaterDeviceObject getDefaultWaterDevice() {
        return mDefaultWaterDevice;
    }

    public void setDefaultWaterDevice(WaterDeviceObject mDefaultWaterDevice) {
        this.mDefaultWaterDevice = mDefaultWaterDevice;
    }


    public AirTouchDeviceObject getDefaultTVOCDevice() {
        return mDefaultTVOCDevice;
    }

    public void setDefaultTVOCDevice(AirTouchDeviceObject mDefaultTVOCDevice) {
        this.mDefaultTVOCDevice = mDefaultTVOCDevice;
    }

    public HomeDevice getUnSupportDevice() {
        return mUnSupportDevice;
    }

    public void setUnSupportDevice(HomeDevice mUnSupportDevice) {
        this.mUnSupportDevice = mUnSupportDevice;
    }

    /**
     * 判断这个家里是否有异常
     *
     * @return
     */
    public boolean isHasErrorInThisHome() {
        List<WaterDeviceObject> waterDeviceObjects = getWaterDeviceList();
        if (waterDeviceObjects != null) {
            for (WaterDeviceObject waterDeviceObject : waterDeviceObjects) {
                if (!waterDeviceObject.getiDeviceStatusFeature().isNormal()) {
                    return true;
                }
            }
        }

        return false;

    }


    /**
     * 判断家里是否有不支持的设备
     *
     * @return
     */
    public boolean isHasUnsupportDeviceInHome() {
        for (HomeDevice homeDevice : mHomeDevicesList) {
            if (homeDevice instanceof UnSupportDeviceObject) {
                return true;
            }
        }
        return false;
    }


    /**
     * all device界面中设备列表的获取情况
     */
    public void setDeviceListLoadFailed() {
        if (mAllDeviceGetStatus == UserAllDataContainer.LOADING_STATUS) {
            mAllDeviceGetStatus = UserAllDataContainer.LOADED_FAILED_STATUS;
        } else if (mAllDeviceGetStatus == UserAllDataContainer.LOADING_CACHE_DATA_SUCCESS) {
            mAllDeviceGetStatus = UserAllDataContainer.CACHE_SUCCESS_BUT_REFRESH_FAILED_STATUS;
        }
    }

    public void setDeviceListLoadSuccess() {
        mAllDeviceGetStatus = UserAllDataContainer.LOADED_SUCCESS_STATUS;
    }

    public boolean isDeviceListLoadingData() {
        return mAllDeviceGetStatus == UserAllDataContainer.LOADING_STATUS;
    }

    public boolean isDeviceListLoadDataFailed() {
        return mAllDeviceGetStatus == UserAllDataContainer.LOADED_FAILED_STATUS;
    }

    public void setDeviceCacheDataLoadSuccess() {

        //如果已经设置了LOADED_SUCCESS_STATUS 就不能再让状态变成 LOADING_CACHE_DATA_SUCCESS
        if (mAllDeviceGetStatus != UserAllDataContainer.LOADED_SUCCESS_STATUS) {
            mAllDeviceGetStatus = UserAllDataContainer.LOADING_CACHE_DATA_SUCCESS;
        }
    }

    public void setDeviceNetworkDataLoadSuccess() {
        mAllDeviceGetStatus = UserAllDataContainer.LOADED_SUCCESS_STATUS;
    }

    public boolean isNetworkDataLoadSuccess() {
        return mAllDeviceGetStatus == UserAllDataContainer.LOADED_SUCCESS_STATUS;
    }

    public boolean isDeviceCacheDataLoadSuccess() {
        return mAllDeviceGetStatus == UserAllDataContainer.LOADING_CACHE_DATA_SUCCESS;
    }

    public boolean isDeviceCacheDataSuccessButFreshFaile() {
        return mAllDeviceGetStatus == UserAllDataContainer.CACHE_SUCCESS_BUT_REFRESH_FAILED_STATUS;
    }

    public int getAllDeviceGetStatus() {
        return mAllDeviceGetStatus;
    }

    public void setAllDeviceGetStatus(int mAllDeviceGetStatus) {
        this.mAllDeviceGetStatus = mAllDeviceGetStatus;
    }

    public abstract String getAllDeviceTitleName();

}
