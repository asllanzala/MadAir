package com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.response;

import android.os.Bundle;

import com.honeywell.hch.airtouch.library.util.LogUtil;

/**
 * Created by Qian Jin on 9/7/16.
 */
public class GetReportResponse extends ResponseCommand {

    private static final byte FILTER_NOT_INSTALLED_BIT = (1 << 0);
    private static final byte FILTER_DURATION_END_BIT = (1 << 1);
    private static final byte BATTERY_10_ALARM = (1 << 2);
    private static final byte BATTERY_30_ALARM = (1 << 3);
    private static final byte BATTERY_50_ALARM = (1 << 4);
    private static final byte BATTERY_70_ALARM = (1 << 5);

    private byte[] mTimeStamp;
    private int mBatteryPercent;
    private int mBatteryRemain;
    private byte[] mFilter;
    private byte mMotorSpeed;
    private byte mBreathFreq;
    private byte mAlarmInfo;

    public GetReportResponse(byte[] data) {
        super(data);

        if (data == null || data.length < MIN_TOTAL_LENGTH) {
            mBody = new byte[]{0};
            mCrc = new byte[]{0, 0};
            return;
        }

        // init fields
        mLength = data[0];
        mType = data[1];
        mBody = new byte[14];
        System.arraycopy(data, 2, mBody, 0, 14);
        mCrc = new byte[] {data[16], data[17]};

        // parse body
        mTimeStamp = new byte[] {data[2], data[3], data[4], data[5]};
        mBatteryPercent = convertNegative(data[8]);
        mBatteryRemain = convertNegative(data[9]);
        mFilter = new byte[] {data[10], data[11]};
        mMotorSpeed = data[12];
        mBreathFreq = data[13];
        mAlarmInfo = data[14];

    }

    @Override
    public Bundle readData() {

        Bundle bundle = new Bundle();

        if (IsDataValidate()) {
            bundle.putInt(BUNDLE_RESPONSE_BATTERY_PERCENT, mBatteryPercent);
            bundle.putInt(BUNDLE_RESPONSE_BATTERY_REMAIN, mBatteryRemain);
            bundle.putInt(BUNDLE_RESPONSE_FILTER_DURATION, getIntFrom2bytes(mFilter));
            bundle.putInt(BUNDLE_RESPONSE_RUN_SPEED, mMotorSpeed);
            bundle.putInt(BUNDLE_RESPONSE_BREATH_FREQ, mBreathFreq);
            bundle.putInt(BUNDLE_RESPONSE_ALARM, mAlarmInfo);
            bundle.putInt(BUNDLE_RESPONSE_TYPE, mType);
        } else {
            LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "GetReportResponse crc is wrong.");
        }

        return bundle;
    }

    public static boolean isFilterNotInstalled(byte alarm) {
        return (alarm & FILTER_NOT_INSTALLED_BIT) == FILTER_NOT_INSTALLED_BIT;
    }

    public static boolean isFilterDurationEnd(byte alarm) {
        return (alarm & FILTER_DURATION_END_BIT) == FILTER_DURATION_END_BIT;
    }

    public static boolean isBattery10Alarm(byte alarm) {
        return (alarm & BATTERY_10_ALARM) == BATTERY_10_ALARM;
    }

    public static boolean isBattery30Alarm(byte alarm) {
        return (alarm & BATTERY_30_ALARM) == BATTERY_30_ALARM;
    }

    public static boolean isBattery50Alarm(byte alarm) {
        return (alarm & BATTERY_50_ALARM) == BATTERY_50_ALARM;
    }

    public static boolean isBattery70Alarm(byte alarm) {
        return (alarm & BATTERY_70_ALARM) == BATTERY_70_ALARM;
    }

}