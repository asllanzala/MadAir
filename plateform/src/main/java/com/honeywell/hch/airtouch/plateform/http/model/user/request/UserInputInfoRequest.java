package com.honeywell.hch.airtouch.plateform.http.model.user.request;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;

import java.io.Serializable;

/**
 * Created by zhujunyu on 2016/12/25.
 */

public class UserInputInfoRequest implements Serializable {

    private String title;
    private String feedInfo;
    private String categroy;
    private String[] imageUrl;
    private String[] imgType;
    private String macId = "";
    private String productName = "";
    private String deviceType = "";

    public UserInputInfoRequest(String title, String feedInfo, String categroy, String[] imageUrl, String[] imgType) {
        this.title = title;
        this.feedInfo = feedInfo;
        this.categroy = categroy;
        this.imageUrl = imageUrl;
        this.imgType = imgType;
    }

    public UserInputInfoRequest(String title, String feedInfo, String categroy, String[] imageUrl, String[] imgType, HomeDevice homeDevice) {
        this.title = title;
        this.feedInfo = feedInfo;
        this.categroy = categroy;
        this.imageUrl = imageUrl;
        this.imgType = imgType;
        this.macId = homeDevice.getDeviceInfo().getMacID();
        this.productName = AppManager.getInstance().getApplication().getString(homeDevice.getiEnrollFeature().getEnrollDeviceName());
        this.deviceType = String.valueOf(homeDevice.getDeviceType());
    }
}