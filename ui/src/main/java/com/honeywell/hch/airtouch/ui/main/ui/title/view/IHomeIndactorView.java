package com.honeywell.hch.airtouch.ui.main.ui.title.view;

/**
 * Created by h127856 on 7/21/16.
 */
public interface IHomeIndactorView {


    /**
     * 设置没有家的fragment显示
     */
    void setNoHomeView();

    void setHomeName(String homeName);


    void setWeatherIcon(int weatherIcon);

    void setWeatherTemperature(String temperature);

    void setCityName(String cityName);

    void setDefaultHomeIcon(boolean isDefault, boolean isSelfHome,boolean isRealHome);


    void setGetWeatherFailed();

    void setCacheLoadingVisible(boolean isLoadingSuccessButRefreshFailed);
    void setCacheLoadingGone();
}
