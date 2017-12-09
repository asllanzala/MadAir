package com.honeywell.hch.airtouch.plateform.appmanager.role;


import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceInfo;

/**
 * Created by Qian Jin on 3/14/16.
 */
public interface Role {

    boolean canShowDeviceDetail();

    boolean canShowFilter(DeviceInfo deviceInfo);

    boolean canShowPurchase(DeviceInfo deviceInfo);

    boolean canControlDevice(DeviceInfo deviceInfo);

    boolean canDeleteDevice(DeviceInfo deviceInfo);

    boolean canGroup(DeviceInfo deviceInfo);

    boolean canControlGroup(int permission);

    boolean canRenameGroup(int permission);

}
