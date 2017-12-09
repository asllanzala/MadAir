package com.honeywell.hch.airtouch.plateform.devices.feature.autospeed;

/**
 * Created by h127856 on 16/10/25.
 */
public interface IAutoSpeedFeature {

    /**
     * If get auto mode from cloud,
     * App needs to calculate speed by combining pm25 and Tvoc value.
     * @return speed at Auto mode.
     */
    int getAutoSpeed();
}
