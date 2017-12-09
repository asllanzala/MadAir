package com.honeywell.hch.airtouch.plateform.http.manager;

import android.content.Context;

import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.http.model.weather.WeatherPageData;
import com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi.FutureHour;
import com.honeywell.hch.airtouch.plateform.http.task.LongTimerRefreshTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wuyuan on 3/1/16.
 */
public class WeatherRefreshManager {

    private HashMap<String, WeatherPageData> mWeatherPageDataHashMap = new HashMap<>();

    private Context mContext;

    public WeatherRefreshManager(Context context){
        mContext = context;
    }

    public void addCityRefresh(String cityName){
        if (!AppManager.getLocalProtocol().canShowWeatherView()){
            return;
        }
        if ((!mWeatherPageDataHashMap.containsKey(cityName)
                || (mWeatherPageDataHashMap.containsKey(cityName) && !cityWeatherIsGetSuccess(cityName)))){
            List<String> cityList = new ArrayList<>();
            cityList.add(cityName);

            LongTimerRefreshTask longTimerRefreshTask = new LongTimerRefreshTask(cityList);
            AsyncTaskExecutorUtil.executeAsyncTask(longTimerRefreshTask);
        }
    }

    public void addCityListRefresh(List<String> cityList,boolean isFromTimer){

        List<String> needRefreshCityList = new ArrayList<>();

        if (!isFromTimer){
            for (String cityName : cityList){
                if ((!mWeatherPageDataHashMap.containsKey(cityName)
                        || (mWeatherPageDataHashMap.containsKey(cityName) && !cityWeatherIsGetSuccess(cityName)))){
                    needRefreshCityList.add(cityName);
                }
            }
        }
        else{
            needRefreshCityList.addAll(cityList);
        }


        if (needRefreshCityList.size() > 0){
            LongTimerRefreshTask longTimerRefreshTask = new LongTimerRefreshTask(needRefreshCityList);
            AsyncTaskExecutorUtil.executeAsyncTask(longTimerRefreshTask);
        }

    }

    public void updateFromWeatherDataHashMap(HashMap<String, WeatherPageData> weatherDataHashMap){
        mWeatherPageDataHashMap.putAll(weatherDataHashMap);
    }

    public void setWeatherHourlyData(String city, FutureHour[] hours) {
        WeatherPageData weatherPageData = mWeatherPageDataHashMap.get(city);
        if (weatherPageData != null) {
            weatherPageData.setHourlyData(hours);
        }
    }

    private boolean cityWeatherIsGetSuccess(String cityName){
        WeatherPageData weatherPageData = mWeatherPageDataHashMap.get(cityName);

        return weatherPageData != null
                && weatherPageData.getHourlyData() != null
                && weatherPageData.getWeather() != null;
    }

    public HashMap<String, WeatherPageData> getWeatherPageDataHashMap(){
        return mWeatherPageDataHashMap;
    }

}
