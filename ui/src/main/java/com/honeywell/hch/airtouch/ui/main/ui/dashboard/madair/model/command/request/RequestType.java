package com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.request;

/**
 * Created by Qian Jin on 9/7/16.
 */
public enum RequestType {

    SYNC((byte)0x01),
    FLASH_DATA((byte)0x03),
    GET_DATA_REPORT((byte)0x04),
    MOTOR_SPEED((byte)0x05),
    NULL_TYPE((byte)0x00);

    private byte mType;

    RequestType(byte type) {
        this.mType = type;
    }

    public byte getType() {
        return mType;
    }

}
