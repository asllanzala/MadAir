package com.honeywell.hch.airtouch.ui.trydemo.presenter;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.RealUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.weather.WeatherPageData;
import com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi.Now;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.DashBoadConstant;
import com.honeywell.hch.airtouch.ui.main.ui.title.presenter.IHomeIndicatorPresenter;
import com.honeywell.hch.airtouch.ui.main.ui.title.view.IHomeIndactorView;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoHomeListContructor;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by honeywell on 23/12/2016.
 */

public class TryDemoDashboardTitleIndicatorPresenter implements IHomeIndicatorPresenter {

    private IHomeIndactorView mHomeIndicatorView;
    private int mCurrentIndex;


    public TryDemoDashboardTitleIndicatorPresenter(IHomeIndactorView indactorView, int index) {
        mHomeIndicatorView = indactorView;
        mCurrentIndex = index;
    }

    @Override
    public int initHomeIndicator(int index) {
        List<UserLocationData> userLocationDataList = UserDataOperator.getHomePageUserLocationDataList(TryDemoHomeListContructor.getInstance().getUserLocationDataList(),
                TryDemoHomeListContructor.getInstance().getVirtualUserLocationDataList());
        if (index < 0) {
            mCurrentIndex = 0;
        } else if (index >= userLocationDataList.size()
                && userLocationDataList.size() - 1 >= 0) {
            mCurrentIndex = userLocationDataList.size() - 1;
        } else {
            mCurrentIndex = index;
        }

        mHomeIndicatorView.setHomeName(getLocationHomeName());
        mHomeIndicatorView.setCityName(UserDataOperator.getCityName(getUserLocationFromList()));
        mHomeIndicatorView.setDefaultHomeIcon(isDefaultHome(), isSelfHome(), isRealHome());

        mHomeIndicatorView.setCacheLoadingGone();
        //TODO: indiaaccount in try demo model should not display weather.
        if (!AppConfig.shareInstance().isIndiaAccount()) {
            setWeatherIcon();
            setWeatherTemereture();
        }
        return mCurrentIndex;
    }

    /**
     * 获取家的个数
     * @return
     */
    @Override
    public int getHomeSize() {
        return UserDataOperator.getHomePageUserLocationDataList(TryDemoHomeListContructor.getInstance().getUserLocationDataList(),
                TryDemoHomeListContructor.getInstance().getVirtualUserLocationDataList()).size();
    }

    /**
     * 是否是默认家
     *
     * @return
     */
    private boolean isDefaultHome() {
        try{
            return UserDataOperator.isDefaultHome(getUserLocationFromList(), mCurrentIndex);
        }catch (Exception e){

        }
        return false;

    }

    /**
     * 是否是自己的家，如果不是就是授权过来的
     *
     * @return
     */
    private boolean isSelfHome() {
        try{
            return getUserLocationFromList().isIsLocationOwner();
        }catch (Exception e){

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
            Now thisNowWeather = weatherDataHashMap.get(TryDemoHomeListContructor.SHANGHAI_CODE).getWeather().getNow();
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
            Now thisNowWeather = weatherDataHashMap.get(TryDemoHomeListContructor.SHANGHAI_CODE).getWeather().getNow();
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
        try{
            return getUserLocationFromList().getName();
        }catch (Exception e){

        }
        return "";
    }


    private UserLocationData getUserLocationFromList() {

        try{
            return UserDataOperator.getHomePageUserLocationDataList(TryDemoHomeListContructor.getInstance().getUserLocationDataList(),
                    TryDemoHomeListContructor.getInstance().getVirtualUserLocationDataList()).get(mCurrentIndex);
        }catch (Exception e){

        }
        return null;


    }

}
