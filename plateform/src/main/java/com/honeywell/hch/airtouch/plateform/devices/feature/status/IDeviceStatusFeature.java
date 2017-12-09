package com.honeywell.hch.airtouch.plateform.devices.feature.status;

/**
 * Created by h127856 on 16/10/25.
 * 这部分是水设备和空气都共有的特性，放在HomeDevice
 */
public interface IDeviceStatusFeature {

     boolean isOffline();

     boolean isDeviceStatusWorse();

     boolean isPowerOff();

     boolean isNormal();

}
