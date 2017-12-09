package com.honeywell.hch.airtouch.plateform.devices.madair.model;

/**
 * Created by Qian Jin on 11/9/16.
 */

public enum MadAirMotorSpeed {

    STOP((byte)0x00),
    LOW_SPEED((byte)0x01),
    MEDIUM_SPEED((byte)0x02),
    HIGH_SPEED((byte)0x03),
    AUTO_SPEED((byte)0x09);

    private byte mSpeed;

    MadAirMotorSpeed(byte speed) {
        this.mSpeed = speed;
    }

    public byte getSpeed() {
        return mSpeed;
    }


}
