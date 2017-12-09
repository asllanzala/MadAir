package com.honeywell.hch.airtouch.ui.main.ui.title.presenter;

import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.RealUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.weather.WeatherPageData;
import com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi.Now;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.DashBoadConstant;
import com.honeywell.hch.airtouch.ui.main.ui.title.view.IHomeIndactorView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by h127856 on 7/26/16.
 */
public class DashboardTitleIndicatorPresenter implements IHomeIndicatorPresenter {

    private IHomeIndactorView mHomeIndicatorView;
    private int mCurrentIndex;


    public DashboardTitleIndicatorPresenter(IHomeIndactorView indactorView, int index) {
        mHomeIndicatorView = indactorView;
        mCurrentIndex = index;
    }

    @Override
    public int initHomeIndicator(int index) {
        List<UserLocationData> userLocationDataList = UserDataOperator.getHomePageUserLocationDataList(UserAllDataContainer.shareInstance().getUserLocationDataList(),
                UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList());
        if (index < 0) {
            mCurrentIndex = 0;
        } else if (index >= userLocationDataList.size()
                && userLocationDataList.size() - 1 >= 0) {
            mCurrentIndex = userLocationDataList.size() - 1;
        } else {
            mCurrentIndex = index;
        }

        mHomeIndicatorView.setHomeName(getLocationHomeName());
        if (getUserLocationFromList() != null) {
            mHomeIndicatorView.setCityName(UserDataOperator.getCityName(getUserLocationFromList()));
        }
        mHomeIndicatorView.setDefaultHomeIcon(isDefaultHome(), isSelfHome(), isRealHome());

        if (NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication())
                && !UserAllDataContainer.shareInstance().isHasNetWorkError() && UserAllDataContainer.shareInstance().isLoadingCacheSuccess()) {
            mHomeIndicatorView.setCacheLoadingVisible(false);
        } else if (NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication())
                && !UserAllDataContainer.shareInstance().isHasNetWorkError() &&
                (UserAllDataContainer.shareInstance().isLoadCacheSuccessButRefreshFailed() || UserAllDataContainer.shareInstance().isLoadDataFailed())) {
            mHomeIndicatorView.setCacheLoadingVisible(true);
        } else {
            mHomeIndicatorView.setCacheLoadingGone();
            setWeatherIcon();
            setWeatherTemereture();
        }


        return mCurrentIndex;
    }

    /**
     * 获取家的个数
     *
     * @return
     */
    @Override
    public int getHomeSize() {
        return UserDataOperator.getHomePageUserLocationDataList(UserAllDataContainer.shareInstance().getUserLocationDataList(),
                UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList()).size();
    }

    /**
     * 是否是默认家
     *
     * @return
     */
    private boolean isDefaultHome() {
        if (getUserLocationFromList() != null) {
            return UserDataOperator.isDefaultHome(getUserLocationFromList(), mCurrentIndex);
        }
        return false;
    }

    /**
     * 是否是自己的家，如果不是就是授权过来的
     *
     * @return
     */
    private boolean isSelfHome() {
        if (getUserLocationFromList() != null) {
            return getUserLocationFromList().isIsLocationOwner();
        }
        return false;

    }

    /**
     * 是否是自己的家，如果不是就是授权过来的
     *
     * @return
     */
    private boolean isRealHome() {
        return getUserLocationFromList() instanceof RealUserLocationData;
    }


    private void setWeatherIcon() {
        int weatherIcon = getWeatherIcon();
        if (weatherIcon != DashBoadConstant.DEFAULT_WEATHER_CODE) {
            mHomeIndicatorView.setWeatherIcon(weatherIcon);
        } else {
            mHomeIndicatorView.setGetWeatherFailed();
        }
    }

    private void setWeatherTemereture() {
        int temperature = getWeatherTemperature();
        if (temperature != DashBoadConstant.DEFAULT_TEMPERATURE) {
            mHomeIndicatorView.setWeatherTemperature(temperature + AppManager.getInstance().getApplication().getString(R.string.temp_unit_c));
        }
    }


    /**
     * 获取天气图标
     *
     * @return
     */
    private int getWeatherIcon() {
        int weatherCode = DashBoadConstant.DEFAULT_WEATHER_CODE;
        HashMap<String, WeatherPageData> weatherDataHashMap = UserAllDataContainer.shareInstance().getWeatherRefreshManager().getWeatherPageDataHashMap();
        if (getUserLocationFromList() != null && weatherDataHashMap.get(getUserLocationFromList().getCity()) != null) {
            Now thisNowWeather = weatherDataHashMap.get(getUserLocationFromList().getCity()).getWeather().getNow();
            if (thisNowWeather != null) {
                weatherCode = thisNowWeather.getCode();
                if (weatherCode == 99) {
                    weatherCode = DashBoadConstant.WEATHER_ICON.length - 1;
                }
                return DashBoadConstant.WEATHER_ICON[weatherCode];
            }
        }
        return weatherCode;
    }


    /**
     * 获取天气
     *
     * @return
     */
    private int getWeatherTemperature() {
        int temperature = DashBoadConstant.DEFAULT_TEMPERATURE;

        HashMap<String, WeatherPageData> weatherDataHashMap = UserAllDataContainer.shareInstance().getWeatherRefreshManager().getWeatherPageDataHashMap();
        if (getUserLocationFromList() != null && weatherDataHashMap.get(getUserLocationFromList().getCity()) != null) {
            Now thisNowWeather = weatherDataHashMap.get(getUserLocationFromList().getCity()).getWeather().getNow();
            if (thisNowWeather != null) {
                return thisNowWeather.getTemperature();

            }
        }
        return temperature;
    }

    /**
     * 获取家的名字
     *
     * @return
     */
    private String getLocationHomeName() {
        if (getUserLocationFromList() != null) {
            return getUserLocationFromList().getName();
        }
        return "";
    }


    private UserLocationData getUserLocationFromList() {
        List<UserLocationData> userLocationDataList = UserDataOperator.getHomePageUserLocationDataList(UserAllDataContainer.shareInstance().getUserLocationDataList(),
                UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList());
        if (userLocationDataList.size() != 0) {
            return userLocationDataList.get(mCurrentIndex);
        }
        return null;

    }

}
