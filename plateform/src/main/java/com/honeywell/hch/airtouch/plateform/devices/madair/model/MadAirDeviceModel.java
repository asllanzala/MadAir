package com.honeywell.hch.airtouch.plateform.devices.madair.model;

import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;


/**
 * Created by Qian Jin on 11/3/16.
 */

public class MadAirDeviceModel implements Serializable {

    public static final int ERROR_DATA = -1;
    private static final int FILTER_LIFE = 12 * 60;

    // properties never changed
    private int mDeviceType = HPlusConstants.MAD_AIR_TYPE;
    private String mModelName = "";
    private String mMacAddress = "";
    private int mDeviceId;

    // properties often changed
    private MadAirDeviceStatus mDeviceStatus = MadAirDeviceStatus.NOT_READY;
    private String mDeviceName = "";
    private String mFirmwareVersion = "";
    private MadAirHistoryRecord mMadAirHistoryRecord;

    // properties for real time data
    private int mBatteryPercent = ERROR_DATA;
    private int mBatteryRemain = ERROR_DATA;
    private int mFilterUsageDuration = ERROR_DATA;
    private int mBreathFreq = ERROR_DATA;
    private int mMotorSpeed;
    private int mAlarmInfo;

    // 保存今天的pm2.5到本地，等到第二天去取前一天的pm2.5和口罩使用时间，计算出颗粒物
    private HashMap<String, Integer> mPm25 = new HashMap<>();


    public MadAirDeviceModel(String macAddress, String modelName) {
        this.mModelName = modelName;
        this.mMacAddress = macAddress;
        this.mDeviceId = generateDeviceId(macAddress);
    }

    public void setDeviceId(int deviceId){
        this.mDeviceId = deviceId;
    }

    public MadAirHistoryRecord getMadAirHistoryRecord() {
        return mMadAirHistoryRecord;
    }

    public void setMadAirHistoryRecord(MadAirHistoryRecord madAirHistoryRecord) {
        this.mMadAirHistoryRecord = madAirHistoryRecord;
    }

    public MadAirDeviceStatus getDeviceStatus() {
        return mDeviceStatus;
    }

    public void setDeviceStatus(MadAirDeviceStatus deviceStatus) {
        this.mDeviceStatus = deviceStatus;
    }

    public String getMacAddress() {
        return mMacAddress;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.mFirmwareVersion = firmwareVersion;
    }

    public String getFirmwareVersion() {
        return mFirmwareVersion;
    }

    public String getDeviceName() {
        return mDeviceName;
    }

    public void setDeviceName(String mDeviceName) {
        this.mDeviceName = mDeviceName;
    }

    public String getModelName() {
        return mModelName;
    }

    public int getBatteryPercent() {
        return mBatteryPercent;
    }

    public void setBatteryPercent(int percent) {
        this.mBatteryPercent = percent;
    }

    public int getBatteryRemain() {
        return mBatteryRemain;
    }

    public void setBatteryRemain(int batteryRemain) {
        this.mBatteryRemain = batteryRemain;
    }

    public int getFilterUsageDuration() {
        return mFilterUsageDuration;
    }

    public void setFilterUsageDuration(int duration) {
        this.mFilterUsageDuration = duration;
    }

    public int getBreathFreq() {
        return mBreathFreq;
    }

    public void setBreathFreq(int breathFreq) {
        this.mBreathFreq = breathFreq;
    }

    public int getMotorSpeed() {
        return mMotorSpeed;
    }

    public void setMotorSpeed(int motorSpeed) {
        this.mMotorSpeed = motorSpeed;
    }

    public int getAlarmInfo() {
        return mAlarmInfo;
    }

    public void setAlarmInfo(int alarmInfo) {
        this.mAlarmInfo = alarmInfo;
    }

    public HashMap<String, Integer> getPm25() {
        return mPm25;
    }

    public void setPm25(HashMap<String, Integer> pm25) {
        this.mPm25 = pm25;
    }

    public static int calculateBatteryPercent(int voltage) {
        if (voltage <= 4200 && voltage >= 4000) {
            return (8000 + (voltage - 4000) * 10) / 100;
        } else if (voltage < 4000 && voltage >= 3900) {
            return (6000 + (voltage - 3900) * 20) / 100;
        } else if (voltage < 3900 && voltage >= 3880) {
            return (5000 + (voltage - 3880) * 50) / 100;
        } else if (voltage < 3880 && voltage >= 3850) {
            return (4000 + (voltage - 3850) * 33) / 100;
        } else if (voltage < 3850 && voltage >= 3780) {
            return (3000 + (voltage - 3780) * 14) / 100;
        } else if (voltage < 3780 && voltage >= 3765) {
            return (2000 + (voltage - 3765) * 67) / 100;
        } else if (voltage < 3765 && voltage >= 3750) {
            return (1000 + (voltage - 3750) * 17) / 100;
        } else if (voltage < 3750 && voltage >= 3660) {
            return (500 + (voltage - 3660) * 11) / 100;
        } else if (voltage < 3660 && voltage >= 3450) {
            return (voltage - 3450) * 2 / 100;
        } else {
            return ERROR_DATA;
        }
    }

    public static int calculateBatteryRemaining(int voltage) {

        if (voltage <= 4200 && voltage >= 4000) {
            return (int) ((122 + (voltage - 4000) * 0.08));
        } else if (voltage < 4000 && voltage >= 3900) {
            return (int) ((109 + (voltage - 3900) * 0.13));
        } else if (voltage < 3900 && voltage >= 3880) {
            return (int) ((95 + (voltage - 3880) * 0.7));
        } else if (voltage < 3880 && voltage >= 3850) {
            return (int) ((82 + (voltage - 3850) * 0.43));
        } else if (voltage < 3850 && voltage >= 3780) {
            return (int) ((68 + (voltage - 3780) * 0.2));
        } else if (voltage < 3780 && voltage >= 3765) {
            return (int) ((54 + (voltage - 3765) * 0.93));
        } else if (voltage < 3765 && voltage >= 3750) {
            return (int) ((41 + (voltage - 3750) * 0.86));
        } else if (voltage < 3750 && voltage >= 3660) {
            return (int) ((27 + (voltage - 3660) * 0.15));
        } else if (voltage < 3660 && voltage >= 3450) {
            return (int) ((5 + (voltage - 3450) * 0.1));
        } else {
            return ERROR_DATA;
        }
    }

    public static int calculateUsagePercent(int duration) {
        if (duration < 0)
            return ERROR_DATA;

        float percent = (float)(FILTER_LIFE - duration) * 100 / FILTER_LIFE;
        if (percent < 0 || percent > 100) {
            return ERROR_DATA;
        } else {
            return Integer.valueOf(round(percent));
        }
    }

    public static int calculateUsageRemaining(int duration) {
        float remain = (float) (FILTER_LIFE - duration) / 60;

        if (remain < 0) {
            return ERROR_DATA;
        } else {
            return Integer.valueOf(round(remain));
        }
    }

    public static String round(float data) {
        DecimalFormat decimalFormat = new DecimalFormat("0");
        return decimalFormat.format(data);
    }

    public int getDeviceType() {
        return mDeviceType;
    }

    public void setmDeviceType(int mDeviceType) {
        this.mDeviceType = mDeviceType;
    }

    public int getStatusBigImage(MadAirDeviceStatus status) {
        switch (status) {
            case DISCONNECT:
                return R.drawable.mad_air_dashboard_disconnect;

            case CONNECT:
                return R.drawable.mad_air_dashboard_connect;

            case USING:
                return R.drawable.mad_air_dashboard_connect;
        }

        return 0;
    }

    public int getDeviceId() {
        return mDeviceId;
    }

    private int generateDeviceId(String address) {
        if (address != null && address.length() == 17) {
            String halfAddress = address.substring(9, 11) + address.substring(12, 14) + address.substring(15);
            return Integer.valueOf(halfAddress, 16);
        } else {
            return 0;
        }
    }

}
