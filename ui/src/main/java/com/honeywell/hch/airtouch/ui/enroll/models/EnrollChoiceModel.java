package com.honeywell.hch.airtouch.ui.enroll.models;

/**
 * Created by Vincent on 1/7/16.
 */
public class EnrollChoiceModel {

    private int deviceType;
    private int deviceImage;
    private int deviceName;
    private float alpha = 0.5f;

    public int getDeviceImage() {
        return deviceImage;
    }

    public void setDeviceImage(int deviceImage) {
        this.deviceImage = deviceImage;
    }

    public int getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(int deviceName) {
        this.deviceName = deviceName;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public EnrollChoiceModel(int deviceImage, int deviceName, int deviceType) {
        this.deviceImage = deviceImage;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
    }

}
