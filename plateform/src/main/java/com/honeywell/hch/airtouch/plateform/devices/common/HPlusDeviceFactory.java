package com.honeywell.hch.airtouch.plateform.devices.common;

import com.honeywell.hch.airtouch.plateform.devices.water.model.UnSupportDeviceObject;
import com.honeywell.hch.airtouch.plateform.devices.water.model.WaterDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceInfo;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;

/**
 * Created by Qian Jin on 4/22/16.
 */
public class HPlusDeviceFactory {

    public static HomeDevice createHPlusDeviceObject(DeviceInfo deviceInfo) {
        if (DeviceType.isAirTouchSeries(deviceInfo.getDeviceType())) {
            return new AirTouchDeviceObject(deviceInfo);
        } else if (DeviceType.isWaterSeries(deviceInfo.getDeviceType())) {
            return new WaterDeviceObject(deviceInfo);
        } else {
            // un-supported device need to be added later..
            return new UnSupportDeviceObject(deviceInfo);
        }
    }

}
