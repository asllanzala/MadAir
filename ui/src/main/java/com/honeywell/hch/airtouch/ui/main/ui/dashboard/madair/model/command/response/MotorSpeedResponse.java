package com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.response;

import android.os.Bundle;

import com.honeywell.hch.airtouch.library.util.LogUtil;

/**
 * Created by Qian Jin on 9/7/16.
 */
public class MotorSpeedResponse extends ResponseCommand {

    public static final int TYPE_MOTOR = 1;
    public static final int TYPE_AUTHENTICATION = 2;

    byte mMotorSpeed;
    byte mMotorType;

    public MotorSpeedResponse(byte[] data) {
        super(data);

        if (data == null || data.length < MIN_TOTAL_LENGTH) {
            mBody = new byte[]{0};
            mCrc = new byte[]{0, 0};
            return;
        }

        // init fields
        mLength = data[0];
        mType = data[1];
        mBody = new byte[2];
        System.arraycopy(data, 2, mBody, 0, 2);
        mCrc = new byte[] {data[4], data[5]};

        // parse body
        mMotorType = data[2];
        mMotorSpeed = data[3];
    }

    @Override
    public Bundle readData() {

        Bundle bundle = new Bundle();

        if (IsDataValidate()) {
            bundle.putInt(BUNDLE_RESPONSE_CHANGED_SPEED, getMotorSpeed());
            bundle.putInt(BUNDLE_RESPONSE_MOTOR_TYPE, getMotorType());
            bundle.putInt(BUNDLE_RESPONSE_TYPE, mType);
        } else {
            LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "MotorSpeedResponse is wrong.");
        }

        return bundle;
    }

    public byte getMotorSpeed() {
        return mMotorSpeed;
    }

    public byte getMotorType() {
        return mMotorType;
    }
}
