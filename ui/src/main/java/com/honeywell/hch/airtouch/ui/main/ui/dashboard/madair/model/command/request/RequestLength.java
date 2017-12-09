package com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.request;

/**
 * Created by Qian Jin on 9/7/16.
 */
public enum RequestLength {

    SYNC((byte) 0x0A),
    FLASH_UPDATE((byte) 0x0A),
    GET_DATA_REPORT((byte) 0x0A),
    MOTOR_SPEED((byte) 0x06),
    NULL_LENGTH((byte) 0x00);

    private byte mLength;

    RequestLength(byte length) {
        mLength = length;
    }

    public byte getLength() {
        return mLength;
    }

}