package com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command;


import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.request.RequestType;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.request.RequestLength;

/**
 * Created by Qian Jin on 8/23/16.
 */
public abstract class Command {

    protected static final String TAG = "BLE command";

    public static final String DATE_2015 = "2015/01/01 00:00:00";
    public static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";

    public static final int MIN_TOTAL_LENGTH = 4;
    private static final int BLE_TOTAL_LENGTH = 20;

    private static final byte RESERVED = (byte) 0xFF;

    protected byte mLength;

    protected byte mType;

    protected byte[] mBody;

    protected byte[] mCrc;

    // Constructor for Request
    public Command(RequestLength length, RequestType type, byte[] requestBody) {
        mLength = length.getLength();
        mType = type.getType();
        mBody = requestBody;
        mCrc = calculateCrc(getCommandBytes());
    }

    // Constructor for Response
    public Command(byte[] responseData) {}

    public byte[] getDataBytes() {
        byte[] result = new byte[BLE_TOTAL_LENGTH];

        result[0] = mLength;
        result[1] = mType;
        System.arraycopy(mBody, 0, result, 2, mLength - 4);
        System.arraycopy(mCrc, 0, result, mLength - 2, 2);

        byte[] reserved = new byte[BLE_TOTAL_LENGTH - mLength];
        for (int i = 0; i < reserved.length; i++) {
            reserved[i] = RESERVED;
        }

        System.arraycopy(reserved, 0, result, mLength, reserved.length);

        return result;
    }

    protected long getLongFrom4bytes(byte[] data) {
        if (data == null || data.length != 4)
            return 0;

        return ((data[3] & 0xFF) | ((data[2] & 0xFF) << 8)
                | ((data[1] & 0xFF) << 16) | ((data[0] & 0xFF) << 24));
    }

    protected int getIntFrom2bytes(byte[] data) {
        if (data == null || data.length != 2)
            return 0;

        return ((data[1] & 0xFF) | ((data[0] & 0xFF) << 8));
    }

    protected boolean IsDataValidate() {

        if (isDataWrong())
            return false;

        if (isCrcWrong())
            return false;

        return true;
    }

    protected int convertNegative(byte value) {
        if (value < 0) {
            return value & Byte.MAX_VALUE | 0x80;
        } else {
            return value;
        }
    }

    protected byte[] get2BytesFromInt(int data) {
        return new byte[] {(byte) (0x00FF & data >> 8), (byte) (0x00FF & data)};
    }

    private byte[] calculateCrc(byte[] data) {
        int sum = 0;
        for (byte b : data) {
            sum += convertNegative(b);
        }

        return get2BytesFromInt(sum);
    }

    private boolean isDataWrong() {
        if ((mLength < MIN_TOTAL_LENGTH) || (mType == 0)) {
            mBody = new byte[] {0};
            mCrc = new byte[] {0, 0};
            return true;
        } else {
            return false;
        }
    }

    private byte[] getCommandBytes() {
        if (isDataWrong())
            return new byte[] {0};

        byte[] result = new byte[mLength - 2];
        result[0] = mLength;
        result[1] = mType;
        System.arraycopy(mBody, 0, result, 2, mLength - 4);

        return result;
    }

    private boolean isCrcWrong() {
        byte[] crc = calculateCrc(getCommandBytes());
        return (mCrc[0] != crc[0]) || (mCrc[1] != crc[1]);
    }

}
