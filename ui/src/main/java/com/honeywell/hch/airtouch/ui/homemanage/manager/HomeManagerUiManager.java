package com.honeywell.hch.airtouch.ui.homemanage.manager;

import android.content.Context;
import android.content.Intent;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.database.manager.CityChinaDBService;
import com.honeywell.hch.airtouch.plateform.database.manager.CityIndiaDBService;
import com.honeywell.hch.airtouch.plateform.database.model.City;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.CategoryHomeCity;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeAndCity;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;
import com.honeywell.hch.airtouch.ui.common.manager.model.DropTextModel;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoHomeListContructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 13/7/16.
 */
public class HomeManagerUiManager {
    private final String TAG = "HomeManagerUiManager";
    private final int EMPTY = 0;
    private HomeManagerManager managerManager;

    public HomeManagerUiManager() {
        managerManager = new HomeManagerManager();
    }


    public void pickUpItem(List<CategoryHomeCity> mCategoryHomeCityList) {
        for (CategoryHomeCity categoryHomeCity : mCategoryHomeCityList) {
            List<HomeAndCity> mHomeAndCityList = categoryHomeCity.getmHomeAndCityList();
            for (HomeAndCity homeAndCity : mHomeAndCityList) {
                homeAndCity.setIsExpand(false);
            }
        }
    }

//    public void clearDefaultHomeId(int homeId, List<CategoryHomeCity> mCategoryHomeCityList) {
//        for (CategoryHomeCity categoryHomeCity : mCategoryHomeCityList) {
//            List<HomeAndCity> mHomeAndCityList = categoryHomeCity.getmHomeAndCityList();
//            for (HomeAndCity homeAndCity : mHomeAndCityList) {
//                if (homeAndCity.getLocationId() == homeId) {
//                    homeAndCity.setIsDefaultHome(false);
//                }
//            }
//        }
//    }


    //是否有相同家的名字
    public boolean isSameName(String cityName, String homeName) {
        for (int i = 0; i < UserAllDataContainer.shareInstance().getUserLocationDataList().size(); i++) {
            UserLocationData userLocation = UserAllDataContainer.shareInstance().getUserLocationDataList().get(i);
            if (homeName.equals(userLocation.getName())
                    && userLocation.isIsLocationOwner()
                    && (userLocation.getCity().equals(cityName))) {
                return true;
            }
        }
        return false;
    }

    //将citylist 分装成droptext
    public DropTextModel[] getCityArrays(ArrayList<City> mCitiesList) {
        List<DropTextModel> dropTextModelsList = new ArrayList<>();
        for (City city : mCitiesList) {
            DropTextModel dropTextModel = new DropTextModel(city);
            dropTextModelsList.add(dropTextModel);
        }
        DropTextModel[] stringsArray = new DropTextModel[dropTextModelsList.size()];
        return dropTextModelsList.toArray(stringsArray);
    }

    //查询city是否正确
    public boolean isCityCanFound(String cityName, CityChinaDBService mCityChinaDBService, CityIndiaDBService mCityIndiaDBService) {
        ArrayList<City> cityArrayList;
        if (AppConfig.shareInstance().isIndiaAccount()) {
            cityArrayList = mCityIndiaDBService.getCitiesByKey(cityName);

        } else {
            cityArrayList = mCityChinaDBService.getCitiesByKey(cityName);
        }
        if (cityArrayList.size() > 0) {
            for (City city : cityArrayList) {
                return cityName.equals(getCityName(city));
            }
        }
        return false;
    }


    public ArrayList<City> getCitiesByKey(String searchCityName, CityChinaDBService mCityChinaDBService, CityIndiaDBService mCityIndiaDBService) {
        if (AppConfig.shareInstance().isIndiaAccount()) {
            return mCityIndiaDBService.getCitiesByKey(searchCityName);
        } else {
            return mCityChinaDBService.getCitiesByKey(searchCityName);
        }
    }

    private String getCityName(City city) {
        return AppConfig.shareInstance().getLanguage().equals(AppConfig.LANGUAGE_ZH)
                ? city.getNameZh() : city.getNameEn();
    }


    /**
     * 设置success callBack
     *
     * @param successCallback
     */
    public void setSuccessCallback(HomeManagerManager.SuccessCallback successCallback) {
        managerManager.setSuccessCallback(successCallback);
    }

    /**
     * 设置error callBack
     *
     * @param errorCallback
     */
    public void setErrorCallback(HomeManagerManager.ErrorCallback errorCallback) {
        managerManager.setErrorCallback(errorCallback);
    }

    public void processRenameHome(String name, int locationId) {
        managerManager.processRenameHome(name, locationId);
    }

    public void processAddHome(String cityName, String homeName) {
        managerManager.processAddHome(cityName, homeName);
    }

    public void getLocation() {
        managerManager.getLocation();
    }

    public void processRemoveHome(int locationId) {
        managerManager.processRemoveHome(locationId);
    }

    public void isOwnDefaultHome(Context mContext, List<CategoryHomeCity> mCategoryHomeCityList) {
        int defaultHomeId = getDefaultHomeId();
        for (CategoryHomeCity categoryHomeCity : mCategoryHomeCityList) {
            for (HomeAndCity homeAndCity : categoryHomeCity.getmHomeAndCityList()) {
                if (homeAndCity.getLocationId() == defaultHomeId) {
                    return;
                }
            }
        }
        processSetDefaultHome(UserInfoSharePreference.DEFAULT_INT_VALUE, mContext);
    }

    private void processSetDefaultHome(int locationId, Context mContext) {
        UserInfoSharePreference.saveDefaultHomeId(locationId);
        Intent intent = new Intent(HPlusConstants.SET_DEFALUT_HOME);
        mContext.sendBroadcast(intent);
    }

    private int getDefaultHomeId() {
        return UserInfoSharePreference.getDefaultHomeId();
    }

    public City isCityCanFoundFromLocal(String searchCityName, CityChinaDBService mCityChinaDBService, CityIndiaDBService mCityIndiaDBService) {
        City city = null;
        if (AppConfig.shareInstance().isIndiaAccount()) {
            city = mCityIndiaDBService.getCityByName(searchCityName);

        } else {
            city = mCityChinaDBService.getCityByName(searchCityName);
        }
        return city;
    }

    public List<String> getCurrentHomeCityList() {
        List<String> cityList = new ArrayList<>();
        List<UserLocationData> userLocationDatas = UserDataOperator.getHomePageUserLocationDataList(UserAllDataContainer.shareInstance().getUserLocationDataList(),
                UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList());
        List<UserLocationData> tryDemoUserLocationDatas =  UserDataOperator.getHomePageUserLocationDataList(TryDemoHomeListContructor.getInstance().getUserLocationDataList(),
                TryDemoHomeListContructor.getInstance().getVirtualUserLocationDataList());
        for (UserLocationData userLocationData : userLocationDatas) {
            LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "userLocationData.getCity(): " + userLocationData.getCity());
            cityList.add(userLocationData.getCity());
        }
        for (UserLocationData userLocationData : tryDemoUserLocationDatas) {
            LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "trydemo userLocationData.getCity(): " + userLocationData.getCity());
            cityList.add(userLocationData.getCity());
        }
        return cityList;
    }

    public String getCurrentCity(CityChinaDBService mCityChinaDBService, CityIndiaDBService mCityIndiaDBService){
        String cityCode = "";
        if (UserInfoSharePreference.getIsUsingGpsCityCode()) {
            cityCode = UserInfoSharePreference.getGpsCityCode();
        } else {
            cityCode = UserInfoSharePreference.getManualCityCode();
        }
        City city = null;
        if (AppConfig.shareInstance().isIndiaAccount()) {
            city = mCityIndiaDBService.getCityByCode(cityCode);

        } else {
            city = mCityChinaDBService.getCityByCode(cityCode);
        }
        String language = AppConfig.shareInstance().getLanguage();
        if (HPlusConstants.CHINA_LANGUAGE_CODE.equals(language)) {
            return city.getNameZh();
        } else {
            return  city.getNameEn();
        }
    }

}
