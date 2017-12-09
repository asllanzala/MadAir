package com.honeywell.hch.airtouch.ui.main.ui.dashboard.view;

import java.util.Map;

/**
 * Created by h127856 on 7/21/16.
 */
public interface IHomeFragmentView {

    /**
     * 设置没有网络，空气或是水质好或是不好时候topView的背景图
     */
    void setTopViewBackground(boolean isWorse, boolean isUnSupport);

    /**
     * 网络错误时候topview的背景图
     */
    void setNetWorkErrorTopViewBackground();

    void setNetWorkErrorNoData();

    /**
     * 设置没有设备的家的View
     */
    void setNoDeviceView();


    /**
     * topView最下面的一行文字
     * @param tip
     */
    void setTopViewTip(String tip);


    void setDefaultHomeIcon(boolean isDefault, boolean isSelfHome);

    /**
     * 传入设备的图标icon和文字，用于显示多个个设备
     * @param deviceIconAndStr
     */
    void initDeviceClassifyView(Map<Integer, String> deviceIconAndStr, boolean isunSupportDevice);

    void setAirtouchPMValue(String pmValue, int textColor, boolean isNeedShowCleanNow);

    void setAirtouchTVOCValue(String pmValue, int textColor, boolean isNeedShowCleanNow);

    void setWaterQuality(String level, int textColor, boolean isNeedShowDetail);


    void setControlFaile(int resultCode);

    void initScenarioModelView(int indexView);

    void setScenarioModelViewFlashing(int indexView);

    /**
     * 设置arrivehome icon的显示，只有在有空净设备的家里，才会显示arrive home的图标
     * @param visible
     */
    void setArriveHomeIconVisible(int visible);

    void setNoControllableDevice();
}
