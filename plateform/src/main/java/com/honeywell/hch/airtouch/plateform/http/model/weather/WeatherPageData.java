package com.honeywell.hch.airtouch.plateform.http.model.weather;


import com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi.FutureHour;
import com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi.Weather;

/**
 * Weather data for single city.
 * Created by lynnliu on 10/18/15.
 */
public class WeatherPageData {

    private Weather mWeather;

    private FutureHour[] mHourlyData;


    public Weather getWeather() {
        return mWeather;
    }

    public void setWeather(Weather weather) {
        mWeather = weather;
    }

    public FutureHour[] getHourlyData() {
        return mHourlyData;
    }

    public void setHourlyData(FutureHour[] hourlyData) {
        mHourlyData = hourlyData;
    }

}
