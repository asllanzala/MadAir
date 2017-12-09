package com.honeywell.hch.airtouch.plateform.devices.feature.speed;

/**
 * Created by h127856 on 16/10/25.
 * 空净这种具有风速指标的
 */
public interface ISpeedStatusFeature {
    int getMaxSpeed();
    int getWorstSpeed();
    int getWorseSpeed();
    int getDeviceControlPoint();
    int getPointsPerSpeed();

    String getSpeedLevel(String speed);


    int getSleepSpeed();
    int getSilentSpeed();
}
