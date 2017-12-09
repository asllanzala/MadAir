package com.honeywell.hch.airtouch.ui.main.ui.title.view;

/**
 * Created by h127856 on 7/21/16.
 */
public interface IAllDeviceIndactorView {


    /**
     * 设置没有家的fragment显示
     */
    void setNoHomeView();

    void setHomeName(String homeName);


    void setCityName(String cityName);

    void setDefaultHomeIcon(boolean isDefault, boolean isSelfHome,boolean isRealHome);

    void setTitleNormalStatus();
}
