package com.honeywell.hch.airtouch.plateform.devices.feature.controlable;

/**
 * Created by h127856 on 16/10/27.
 */
public interface IControlFeature {
    boolean canRemoteControl();

    int getAllDeviceStatusImage();

    String getScenerioModeAction();

    boolean isCanControlable();

    int getAllDeviceBigImg();
}
