package com.honeywell.hch.airtouch.plateform.http.task;


import android.content.Intent;

import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.ThinkPageClient;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.WeatherRefreshManager;
import com.honeywell.hch.airtouch.plateform.http.model.weather.WeatherPageData;
import com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi.HourlyFuture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by wuyuan on 13/10/2015.
 * this task is used for refresh the datas that need to refresh not frequently
 * like weather data,emotional data.
 */
public class LongTimerRefreshTask extends BaseRequestTask {


    private List<String> mCityList = new ArrayList<>();

    public LongTimerRefreshTask(List<String> cityList) {
        mCityList.addAll(cityList);
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {
        //get weather data and send broad cast to refresh weather data
        try {
            mCityList = new ArrayList<>(new HashSet<>(mCityList));
            getCityWeatherData();
        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.ERROR, "LongTimerRefresh", "doInBackground exception =" + e.toString());
        }

        mCityList.clear();
        return null;
    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {

        Intent boradIntent = new Intent(HPlusConstants.LONG_REFRESH_END_ACTION);
        AppManager.getInstance().getApplication().getApplicationContext().sendBroadcast(boradIntent);

        super.onPostExecute(responseResult);
    }


    private void getCityWeatherData() {

        if (mCityList != null && mCityList.size() > 0) {
            for (String cityName : mCityList) {

                ResponseResult responseResult = ThinkPageClient.sharedInstance().getWeatherDataNew
                        (cityName, AppConfig.getLanguageXinzhi(), 'c', RequestID.ALL_DATA);

                if (responseResult != null && responseResult.isResult()) {
                    HashMap<String, WeatherPageData> mWeatherDataHashMap = (HashMap<String, WeatherPageData>) responseResult
                            .getResponseData().getSerializable(HPlusConstants.WEATHER_DATA_KEY);

                    //update this to CityWeather model
                    updateWeatherDataHashMapToCityWeatherModel(mWeatherDataHashMap);

                    HourlyFuture hourlyFutureData = ThinkPageClient.sharedInstance()
                            .getFutureWeather(cityName, AppConfig.getLanguageXinzhi());

                    UserAllDataContainer.shareInstance().getWeatherRefreshManager()
                            .setWeatherHourlyData(cityName,
                                    hourlyFutureData.getHours());
                }


            }

        }

    }

    private void updateWeatherDataHashMapToCityWeatherModel(HashMap<String, WeatherPageData> weatherDataHashMap) {
        WeatherRefreshManager weatherRefreshManager = UserAllDataContainer.shareInstance().getWeatherRefreshManager();
        weatherRefreshManager.updateFromWeatherDataHashMap(weatherDataHashMap);
    }
}
