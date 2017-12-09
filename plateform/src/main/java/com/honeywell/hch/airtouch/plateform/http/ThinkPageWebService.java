package com.honeywell.hch.airtouch.plateform.http;

import com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi.HourlyFuture;
import com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi.HourlyHistory;

/**
 * Created by lynnliu on 10/15/15.
 */
public interface ThinkPageWebService {

    public HourlyHistory getHistoryWeather(String city, String lang);

    public HourlyFuture getFutureWeather(String city, String lang);
}
