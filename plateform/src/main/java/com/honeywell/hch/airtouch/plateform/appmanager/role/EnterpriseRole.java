package com.honeywell.hch.airtouch.plateform.appmanager.role;


import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceInfo;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.AuthTo;

/**
 * Created by Qian Jin on 3/14/16.
 */
public class EnterpriseRole implements Role {
    @Override
    public boolean canShowDeviceDetail() {
        return false;
    }

    @Override
    public boolean canShowFilter(DeviceInfo deviceInfo) {
        return false;
    }

    @Override
    public boolean canShowPurchase(DeviceInfo deviceInfo) {
        return false;
    }

    @Override
    public boolean canControlDevice(DeviceInfo deviceInfo) {
        return deviceInfo.getPermission() >= AuthTo.CONTROLLER;
    }

    @Override
    public boolean canDeleteDevice(DeviceInfo deviceInfo) {
        return deviceInfo.getPermission() >= AuthTo.OWNER;
    }

    @Override
    public boolean canGroup(DeviceInfo deviceInfo) {
        return false;
    }

    @Override
    public boolean canControlGroup(int permission) {
        return permission > AuthTo.OBSERVER;
    }

    @Override
    public boolean canRenameGroup(int permission) {
        return permission >= AuthTo.OWNER;
    }

}
