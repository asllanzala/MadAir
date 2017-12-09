package com.honeywell.hch.airtouch.plateform.http.manager;

import com.google.gson.Gson;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.database.model.City;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.CategoryHomeCity;
import com.honeywell.hch.airtouch.plateform.http.manager.model.CategoryHomeCityDashBoard;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeAndCity;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.manager.model.RealUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.VirtualUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.device.AirtouchCapability;
import com.honeywell.hch.airtouch.plateform.http.model.device.DeviceTypeConfigInfo;
import com.honeywell.hch.airtouch.plateform.http.model.device.SmartROCapability;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by honeywell on 22/12/2016.
 */

public class UserDataOperator {

    public static UserLocationData getUserLocationByID(int locationID,List<UserLocationData> mUserLocationDataList) {
        UserLocationData userLocation = null;

        if (mUserLocationDataList != null) {
            for (int i = 0; i < mUserLocationDataList.size(); i++) {
                if (mUserLocationDataList.get(i).getLocationID() == locationID) {
                    userLocation = mUserLocationDataList.get(i);
                    break;
                }
            }
        }
        return userLocation;
    }

    /**
     * get locaiton object using locaiton id
     *
     * @param locationId
     * @return
     */
    public static UserLocationData getLocationWithId(int locationId,List<UserLocationData> mUserLocationDataList, List<VirtualUserLocationData> mVirtualUserLocationDataList) {
        for (UserLocationData locationItem : mUserLocationDataList) {
            if (locationItem.getLocationID() == locationId) {
                return locationItem;
            }
        }
        for (UserLocationData locationItem : mVirtualUserLocationDataList) {
            if (locationItem.getLocationID() == locationId) {
                return locationItem;
            }
        }
        LogUtil.log(LogUtil.LogLevel.ERROR, "CurrentUserAccountInfo", "getLocationWithId location id = " + locationId + " ,is return null");
        return null;
    }


    /**
     * 获取需要打开的设备
     * @return
     */
    public static ArrayList<Integer> getNeedOpenDevice(List<UserLocationData> mUserLocationDataList) {
        ArrayList<Integer> deviceIdList = new ArrayList<>();
        if (mUserLocationDataList != null && mUserLocationDataList.size() > 0) {
            for (UserLocationData userLocationData : mUserLocationDataList) {
                if (userLocationData != null) {
                    deviceIdList.addAll(userLocationData.getOffDeviceIdList());
                }
            }
        }
        return deviceIdList;
    }


    public static HomeDevice getDeviceWithDeviceId(int deviceId,List<UserLocationData> mUserLocationDataList, List<VirtualUserLocationData> mVirtualUserLocationDataList) {
        List<UserLocationData> userLocationDataList = getAllDevicePageUserLocationDataList(mUserLocationDataList,mVirtualUserLocationDataList);
        if (userLocationDataList.size() > 0) {
            for (UserLocationData userLocationData : userLocationDataList) {
                if (userLocationData != null) {
                    for (HomeDevice device : userLocationData.getHomeDevicesList()) {
                        if (deviceId == device.getDeviceId())
                            return device;
                    }
                }
            }
        }
        return null;
    }


    public static List<UserLocationData> getAllDevicePageUserLocationDataList(List<UserLocationData> mUserLocationDataList, List<VirtualUserLocationData> mVirtualUserLocationDataList) {
        List<UserLocationData> userLocationDataList = new ArrayList<>();
        userLocationDataList.addAll(mUserLocationDataList);
        VirtualUserLocationData virtualUserLocationData = new VirtualUserLocationData();
        for (VirtualUserLocationData virtualUserLocationData1 : mVirtualUserLocationDataList) {
            virtualUserLocationData.setHomeDevicesList(virtualUserLocationData1.getMadAirDeviceModel());
        }
        if (virtualUserLocationData.getHomeDevicesList().size() != 0) {
            userLocationDataList.add(virtualUserLocationData);
        }
        return userLocationDataList;
    }


    /**
     * 根据获取到的device config信息，解析得到当前版本里所有支持设备类型的config信息
     */
    public static void setDeviceConfig(List<DeviceTypeConfigInfo> deviceTypeConfigInfoList, Map<Integer, AirtouchCapability> mAirtouchCapabilityMap,
                                       Map<Integer,SmartROCapability> smartROCapabilityMap) {

        if (deviceTypeConfigInfoList != null) {
            for (DeviceTypeConfigInfo deviceTypeConfigInfo : deviceTypeConfigInfoList) {
                int deviceType = deviceTypeConfigInfo.getmDeviceType();
                if (DeviceType.isAirTouchSeries(deviceType)) {
                    AirtouchCapability airtouchCapability = new Gson().fromJson(deviceTypeConfigInfo.getmDeviceCapability(), AirtouchCapability.class);
                    mAirtouchCapabilityMap.put(deviceType, airtouchCapability);
                } else if (DeviceType.isWaterSeries(deviceType)) {
                    SmartROCapability smartROCapability = new Gson().fromJson(deviceTypeConfigInfo.getmDeviceCapability(), SmartROCapability.class);
                    smartROCapabilityMap.put(deviceType,smartROCapability);
                }
            }
        }

    }


    /**
     * 从数据库中获取城市名称，如果数据库中没有，就用city作为城市名显示
     *
     * @param userLocationData
     * @return
     */
    public static String getCityName(UserLocationData userLocationData) {
        return getCityName(userLocationData.getCity());
    }

    /**
     * 从数据库中获取城市名称，如果数据库中没有，就用city作为城市名显示
     *
     * @param cityCode
     * @return
     */
    public static String getCityName(String cityCode) {
        String cityNameCnOrEn = "";
        City city = AppConfig.shareInstance().getCityFromDatabase(cityCode);
        if (city.getNameEn() != null || city.getNameZh() != null) {
            if (city.getNameEn() == null) {
                cityNameCnOrEn = city.getNameZh();
            } else if (city.getNameZh() == null) {
                cityNameCnOrEn = city.getNameEn();
            } else {
                cityNameCnOrEn = AppConfig.shareInstance().getLanguage().equals(AppConfig
                        .LANGUAGE_ZH) ? city.getNameZh() : city.getNameEn();
            }
        } else if (!StringUtil.isEmpty(cityCode)) {
            cityNameCnOrEn = cityCode;
        }
        return cityNameCnOrEn;
    }


    /**
     * 获取所有家的名字列表，封装成CategoryHomeCity
     *
     * @return
     */
    public static List<CategoryHomeCity> getHomeList() {
        ArrayList<HomeAndCity> mHomeAndCityList = new ArrayList<>();
        List<CategoryHomeCity> mHomeNameList = new ArrayList<>();
        Set<String> homeNameSet = new TreeSet<>();
        List<UserLocationData> userLocations
                = UserAllDataContainer.shareInstance().getUserLocationDataList();

        //涉及到线程安全问题，所以暂时用try catch来避免，下个版本调整架构。
        try {
            //遍历家
            if (userLocations != null) {
                int locationSize = userLocations.size();

                for (int i = 0; i < locationSize; i++) {
                    // India version
                    String cityNameCnOrEn = getCityName(userLocations.get(i));
                    if (cityNameCnOrEn == null) {
                        cityNameCnOrEn = "";
                    }
                    HomeAndCity homeAndCity = new HomeAndCity(userLocations.get(i).getName(),
                            userLocations.get(i).getLocationID(), cityNameCnOrEn, true);

                    homeAndCity.setIsOwnerHome(userLocations.get(i).isIsLocationOwner());

                    homeAndCity.setmLocationOwnerName(userLocations.get(i).getLocationOwnerName());
                    homeAndCity.setHasUnnormalDevice(isHasUnnormalStatusInHome(userLocations.get(i)));
                    homeNameSet.add(homeAndCity.getmLocationOwnerName());
                    if (isDefaultHome(userLocations.get(i), i)) {
                        homeAndCity.setIsDefaultHome(true);
                    }

                    mHomeAndCityList.add(homeAndCity);

                }
            }
        } catch (Exception e) {
        }

        //封装成category
        for (Iterator it = homeNameSet.iterator(); it.hasNext(); ) {
            CategoryHomeCity categoryHomeCity = new CategoryHomeCity();
            List<HomeAndCity> homeAndCityList = new ArrayList<>();
            String homeName = (String) it.next();
            for (HomeAndCity homeAndCity : mHomeAndCityList) {

                if (homeAndCity.getmLocationOwnerName().equals(homeName)) {
                    categoryHomeCity.setmCategoryName(homeAndCity.getmLocationOwnerName());
                    homeAndCityList.add(homeAndCity);
                }
            }
            categoryHomeCity.setmCategoryName(homeName);
            categoryHomeCity.setmHomeAndCityList(homeAndCityList);
            mHomeNameList.add(categoryHomeCity);
        }

        return mHomeNameList;
    }



    public static boolean isDefaultHome(UserLocationData userLocationData, int index) {
        int defaultLocationId = UserInfoSharePreference.getDefaultHomeId();
        if (defaultLocationId == UserInfoSharePreference.DEFAULT_INT_VALUE && index == 0) {
            return true;
        } else if (defaultLocationId != UserInfoSharePreference.DEFAULT_INT_VALUE) {
            return UserInfoSharePreference.getDefaultHomeId() == userLocationData.getLocationID();
        }
        return false;
    }


    /**
     * 获取所有家的名字列表，封装成CategoryHomeCity
     *
     * @return
     */
    public static List<CategoryHomeCityDashBoard> getHomeListDashBoard(List<UserLocationData> mUserLocationDataList, List<VirtualUserLocationData> mVirtualUserLocationDataList) {
        List<UserLocationData> userLocations
                = getHomePageUserLocationDataList(mUserLocationDataList,mVirtualUserLocationDataList);

        List<CategoryHomeCityDashBoard> mHomeNameInUIList = new ArrayList<>();
        List<String> homeNameList = new ArrayList<>();
        ArrayList<HomeAndCity> mHomeAndCityList = new ArrayList<>();
        //涉及到线程安全问题，所以暂时用try catch来避免，下个版本调整架构。
        try {
            //遍历家
            if (userLocations != null) {
                parseRealHomeList(homeNameList, userLocations, mHomeAndCityList);

                for (int i = 0; i < userLocations.size(); i++) {
                    if (userLocations.get(i) instanceof VirtualUserLocationData) {
                        homeNameList.add(AppManager.getInstance().getApplication().getString(R.string.dash_borad_personal_menu));
                        HomeAndCity homeAndCity = new HomeAndCity(userLocations.get(i).getName(),
                                userLocations.get(i).getLocationID(), userLocations.get(i).getCity(), false);
                        homeAndCity.setIsOwnerHome(true);
                        homeAndCity.setmLocationOwnerName("");
                        homeAndCity.setIsDefaultHome(false);
                        mHomeAndCityList.add(homeAndCity);

                    }
                }
            }
        } catch (Exception e) {
        }
        homeNameList = removeDuplicate(homeNameList);
        encloseCategory(homeNameList, mHomeAndCityList,mHomeNameInUIList);
        return mHomeNameInUIList;

    }

    /**
     * 获取所有家的名字列表，封装成CategoryHomeCity,AllDevice 用
     *
     * @return
     */
    public static List<CategoryHomeCityDashBoard> getHomeListAllDevice(List<UserLocationData> mUserLocationDataList, List<VirtualUserLocationData> mVirtualUserLocationDataList) {
        List<UserLocationData> userLocations
                = getAllDevicePageUserLocationDataList(mUserLocationDataList,mVirtualUserLocationDataList);


        List<CategoryHomeCityDashBoard> mHomeNameInUIList = new ArrayList<>();
        List<String> homeNameList = new ArrayList<>();
        ArrayList<HomeAndCity> mHomeAndCityList = new ArrayList<>();
        //涉及到线程安全问题，所以暂时用try catch来避免，下个版本调整架构。
        try {
            //遍历家
            if (userLocations != null) {
                parseRealHomeList(homeNameList, userLocations, mHomeAndCityList);

                for (int i = 0; i < userLocations.size(); i++) {
                    if (userLocations.get(i) instanceof VirtualUserLocationData) {
                        homeNameList.add(AppManager.getInstance().getApplication().getString(R.string.dash_borad_personal_menu));
                        HomeAndCity homeAndCity = new HomeAndCity(AppManager.getInstance().getApplication().getString(R.string.mad_air_portable_devices),
                                userLocations.get(i).getLocationID(), userLocations.get(i).getCity(), false);
                        homeAndCity.setIsOwnerHome(true);
                        homeAndCity.setmLocationOwnerName("");
                        homeAndCity.setIsDefaultHome(false);
                        mHomeAndCityList.add(homeAndCity);
                        break;

                    }
                }
            }
        } catch (Exception e) {
        }
        homeNameList = removeDuplicate(homeNameList);
        encloseCategory(homeNameList, mHomeAndCityList,mHomeNameInUIList);
        return mHomeNameInUIList;
    }


    private static List removeDuplicate(List list) {
        List listTemp = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            if (!listTemp.contains(list.get(i))) {
                listTemp.add(list.get(i));
            }
        }
        return listTemp;
    }

    private static void parseRealHomeList(List<String> homeNameList, List<UserLocationData> userLocations, ArrayList<HomeAndCity> mHomeAndCityList) {
        //涉及到线程安全问题，所以暂时用try catch来避免，下个版本调整架构。
        //遍历家
        for (int i = 0; i < userLocations.size(); i++) {
            if (userLocations.get(i) instanceof RealUserLocationData) {
                // India version
                String cityNameCnOrEn = getCityName(userLocations.get(i));
                if (cityNameCnOrEn == null) {
                    cityNameCnOrEn = "";
                }
                HomeAndCity homeAndCity = new HomeAndCity(userLocations.get(i).getName(),
                        userLocations.get(i).getLocationID(), cityNameCnOrEn, true);

                homeAndCity.setIsOwnerHome(userLocations.get(i).isIsLocationOwner());

                homeAndCity.setmLocationOwnerName(userLocations.get(i).getLocationOwnerName());
                homeAndCity.setHasUnnormalDevice(isHasUnnormalStatusInHome(userLocations.get(i)));
                homeNameList.add(AppManager.getInstance().getApplication().getString(R.string.dash_borad_home_menu));
                if (isDefaultHome(userLocations.get(i), i)) {
                    homeAndCity.setIsDefaultHome(true);
                }

                mHomeAndCityList.add(homeAndCity);
            }
        }
    }

    private static void encloseCategory(List<String> homeNameList, ArrayList<HomeAndCity> mHomeAndCityList,List<CategoryHomeCityDashBoard> mHomeNameListDashBoardList) {
        //封装成category
        for (Iterator it = homeNameList.iterator(); it.hasNext(); ) {
            CategoryHomeCityDashBoard categoryHomeCity = new CategoryHomeCityDashBoard();
            List<HomeAndCity> homeAndCityList = new ArrayList<>();
            String homeName = (String) it.next();
            for (HomeAndCity homeAndCity : mHomeAndCityList) {
                if (AppManager.getInstance().getApplication().getString(R.string.dash_borad_home_menu).equals(homeName) && homeAndCity.isRealHome()) {
                    homeAndCityList.add(homeAndCity);
                } else if (AppManager.getInstance().getApplication().getString(R.string.dash_borad_personal_menu).equals(homeName) && !homeAndCity.isRealHome()) {
                    homeAndCityList.add(homeAndCity);
                }
            }
            categoryHomeCity.setmCategoryName(homeName);
            categoryHomeCity.setmHomeAndCityList(homeAndCityList);
            mHomeNameListDashBoardList.add(categoryHomeCity);
        }

    }


    public static List<UserLocationData> getHomePageUserLocationDataList(List<UserLocationData> mUserLocationDataList, List<VirtualUserLocationData> mVirtualUserLocationDataList) {
        List<UserLocationData> userLocationDataList = new ArrayList<>();
        userLocationDataList.addAll(mUserLocationDataList);
        userLocationDataList.addAll(mVirtualUserLocationDataList);
        return userLocationDataList;
    }


    public static boolean isHasUnnormalStatusInHome(UserLocationData mUserLocationData) {
        return isAirtouchWorse(mUserLocationData) || isWaterWorse(mUserLocationData)
                || mUserLocationData.isHasErrorInThisHome() || isAirtouchTVOCWorse(mUserLocationData)
                || mUserLocationData.isHasUnsupportDeviceInHome();
    }


    public static boolean isHasUnnormalStatusExceptUnSupport(UserLocationData mUserLocationData) {
        return isAirtouchWorse(mUserLocationData) || isWaterWorse(mUserLocationData)
                || mUserLocationData.isHasErrorInThisHome() || isAirtouchTVOCWorse(mUserLocationData);
    }

    public static boolean isAirtouchWorse(UserLocationData mUserLocationData) {
        int mPmValue = HPlusConstants.DEFAULT_PM25_VALUE;
        if (mUserLocationData.getDefaultPMDevice() != null) {
            mPmValue = ((AirTouchDeviceObject) (mUserLocationData.getDefaultPMDevice())).getAirtouchDeviceRunStatus().getmPM25Value();
        }
        if (mPmValue >= HPlusConstants.PM25_MEDIUM_LIMIT && mPmValue < HPlusConstants.ERROR_SENSOR) {
            return true;
        }
        return false;
    }

    public static boolean isAirtouchTVOCWorse(UserLocationData mUserLocationData) {
        int tvocLevel = HPlusConstants.TVOC_ERROR_LEVEL;
        if (mUserLocationData.getDefaultTVOCDevice() != null) {
            tvocLevel = ((AirTouchDeviceObject) (mUserLocationData.getDefaultTVOCDevice())).getTvocFeature().getTvocLevel();
        }
        if (tvocLevel > HPlusConstants.TVOC_GOOD_LEVEL) {
            return true;
        }
        return false;
    }

    public static boolean isWaterWorse(UserLocationData mUserLocationData) {
        if (mUserLocationData.getDefaultWaterDevice() != null &&
                mUserLocationData.getDefaultWaterDevice().getiDeviceStatusFeature().isDeviceStatusWorse()) {
            return true;
        }
        return false;
    }


    public static boolean isHasErrorOrUnSupportDevice(List<UserLocationData > mUserLocationDataList) {
        for (UserLocationData userLocationData : mUserLocationDataList) {
            if (userLocationData != null && (userLocationData.isHasErrorInThisHome() || userLocationData.isHasUnsupportDeviceInHome())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 是否有相同城市名字
     * @param reName
     * @param homeAndCity
     * @return
     */
    public static boolean isHaveSameHomeName(String reName, HomeAndCity homeAndCity) {
        for (int i = 0; i < UserAllDataContainer.shareInstance().getUserLocationDataList().size(); i++) {
            UserLocationData userLocation = UserAllDataContainer.shareInstance().getUserLocationDataList().get(i);
            String cityNameCnOrEn = getCityName(userLocation);
            if (reName.equals(userLocation.getName())
                    && (cityNameCnOrEn.equals(homeAndCity.getHomeCity()))) {

                return true;
            }
        }
        return false;
    }


    /**
     *   是否有相同城市名字
     * @param reName
     * @param cityCode
     * @return
     */
    public static boolean isHaveSameHomeName(String reName, String cityCode) {
        for (int i = 0; i < UserAllDataContainer.shareInstance().getUserLocationDataList().size(); i++) {
            UserLocationData userLocation = UserAllDataContainer.shareInstance().getUserLocationDataList().get(i);
            String cityNameCnOrEn = userLocation.getCity();
            if (reName.equals(userLocation.getName())
                    && (cityNameCnOrEn.equals(cityCode))) {
                return true;
            }
        }
        return false;
    }
}
