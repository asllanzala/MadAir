package com.honeywell.hch.airtouch.plateform.devices.common;

import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AirTouch450EnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AirTouchFFACEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AirTouchJDSEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AirTouchSEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AirTouchX3CompactEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AirTouchXCompactEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AirTouchXEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AquaTouch100SEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AquaTouch400SEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AquaTouch50SEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AquaTouch600SEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AquaTouch75SEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.IEnrollFeature;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.MadAirEnrollFeatureImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qian Jin on 4/21/16.
 */
public class DeviceType {

    public static boolean isAirTouchSeries(int deviceType) {
        return deviceType == HPlusConstants.AIRTOUCH_450_TYPE ||
                deviceType == HPlusConstants.AIRTOUCH_X_TYPE ||
                deviceType == HPlusConstants.AIRTOUCH_JD_TYPE ||
                deviceType == HPlusConstants.AIRTOUCH_S_TYPE ||
                deviceType == HPlusConstants.AIRTOUCH_FFAC_TYPE ||
                deviceType == HPlusConstants.AIRTOUCH_XCOMPACT_TYPE ||
                deviceType == HPlusConstants.AIRTOUCH_X3COMPACT_TYPE ||
                deviceType == HPlusConstants.AIRTOUCH_X3COMPACT_UPDATE_TYPE ||
                deviceType == HPlusConstants.AIRTOUCH_450_UPDATE_TYPE ||
                deviceType == HPlusConstants.AIRTOUCH_S_UPDATE_TYPE;
    }

    public static boolean isWaterSeries(int deviceType) {
        return isAquaTouch400(deviceType) ||
                isAquaTouch600(deviceType) ||
                isAquaTouch100(deviceType) ||
                isAquaTouch75(deviceType) ||
                isAquaTouch50(deviceType);
    }

    public static boolean isMadAIrSeries(int deviceType) {
        return isMadAir(deviceType);
    }

    public static boolean isMadAir(int deviceType) {
        return deviceType == HPlusConstants.MAD_AIR_TYPE
                || deviceType == HPlusConstants.MAD_AIR_AUTH;
    }

    public static boolean isAirTouchS(int deviceType) {
        return deviceType == HPlusConstants.AIRTOUCH_S_TYPE;
    }

    public static boolean isAirTouchX(int deviceType) {
        return deviceType == HPlusConstants.AIRTOUCH_X_TYPE;
    }

    public static boolean isAirTouch450(int deviceType) {
        return deviceType == HPlusConstants.AIRTOUCH_450_TYPE;
    }

    public static boolean isAirTouchFFAC(int deviceType) {
        return deviceType == HPlusConstants.AIRTOUCH_FFAC_TYPE;
    }

    public static boolean isAquaTouch600(int deviceType) {
        return deviceType == HPlusConstants.WATER_SMART_RO_600_TYPE;
    }

    public static boolean isAquaTouch400(int deviceType) {
        return deviceType == HPlusConstants.WATER_SMART_RO_400_TYPE;
    }

    public static boolean isAquaTouch100(int deviceType) {
        return deviceType == HPlusConstants.WATER_SMART_RO_100_TYPE;
    }

    public static boolean isAquaTouch75(int deviceType) {
        return deviceType == HPlusConstants.WATER_SMART_RO_75_TYPE;
    }

    public static boolean isAquaTouch50(int deviceType) {
        return deviceType == HPlusConstants.WATER_SMART_RO_50_TYPE;
    }

    public static boolean isAirTouch450Update(int deviceType){
        return deviceType == HPlusConstants.AIRTOUCH_450_UPDATE_TYPE;
    }

    public static boolean isAirTouchSUpdate(int deviceType){
        return deviceType == HPlusConstants.AIRTOUCH_S_UPDATE_TYPE;
    }

    public static boolean isAirTouch100GSeries(int deviceType) {
        //TODO: 授权判断是否可操作
        return deviceType == HPlusConstants.WATER_SMART_RO_50_TYPE ||
                deviceType == HPlusConstants.WATER_SMART_RO_75_TYPE ||
                deviceType == HPlusConstants.WATER_SMART_RO_100_TYPE;
    }


    public static boolean isAirTouchJPS(int deviceType) {
        return deviceType == HPlusConstants.AIRTOUCH_JD_TYPE;
    }

    public static boolean isAirTouchXCompact(int deviceType) {
        return deviceType == HPlusConstants.AIRTOUCH_XCOMPACT_TYPE;
    }

    public static boolean isAirTouchX3Compact(int deviceType) {
        return deviceType == HPlusConstants.AIRTOUCH_X3COMPACT_TYPE;
    }

    public static boolean isAirTouchX3UpdateCompact(int deviceType) {
        return deviceType == HPlusConstants.AIRTOUCH_X3COMPACT_UPDATE_TYPE;
    }

    public static boolean isAirTouchXCompactSeries(int deviceType) {
        return deviceType == HPlusConstants.AIRTOUCH_XCOMPACT_TYPE ||
                deviceType == HPlusConstants.AIRTOUCH_X3COMPACT_TYPE ||
                deviceType == HPlusConstants.AIRTOUCH_X3COMPACT_UPDATE_TYPE;
    }

    public static boolean isHplusSeries(int deviceType) {
        return deviceType == HPlusConstants.AIRTOUCH_450_TYPE ||
                deviceType == HPlusConstants.AIRTOUCH_X_TYPE ||
                deviceType == HPlusConstants.AIRTOUCH_JD_TYPE ||
                deviceType == HPlusConstants.AIRTOUCH_S_TYPE ||
                deviceType == HPlusConstants.AIRTOUCH_FFAC_TYPE ||
                deviceType == HPlusConstants.AIRTOUCH_450_UPDATE_TYPE ||
                deviceType == HPlusConstants.AIRTOUCH_S_UPDATE_TYPE ||

                deviceType == HPlusConstants.WATER_SMART_RO_600_TYPE ||
                deviceType == HPlusConstants.WATER_SMART_RO_400_TYPE ||
                deviceType == HPlusConstants.AIRTOUCH_XCOMPACT_TYPE ||
                deviceType == HPlusConstants.AIRTOUCH_X3COMPACT_TYPE ||
                deviceType == HPlusConstants.AIRTOUCH_X3COMPACT_UPDATE_TYPE ||
                deviceType == HPlusConstants.WATER_SMART_RO_100_TYPE ||
                deviceType == HPlusConstants.WATER_SMART_RO_75_TYPE ||
                deviceType == HPlusConstants.WATER_SMART_RO_50_TYPE;

    }

    public static List<Integer> getAllSupportDeviceType() {
        List<Integer> deviceTypeList = new ArrayList<>();
        deviceTypeList.add(HPlusConstants.AIRTOUCH_450_TYPE);
        deviceTypeList.add(HPlusConstants.AIRTOUCH_X_TYPE);
        deviceTypeList.add(HPlusConstants.AIRTOUCH_JD_TYPE);
        deviceTypeList.add(HPlusConstants.AIRTOUCH_S_TYPE);
        deviceTypeList.add(HPlusConstants.AIRTOUCH_FFAC_TYPE);
        deviceTypeList.add(HPlusConstants.AIRTOUCH_XCOMPACT_TYPE);
        deviceTypeList.add(HPlusConstants.AIRTOUCH_X3COMPACT_TYPE);
        deviceTypeList.add(HPlusConstants.AIRTOUCH_X3COMPACT_UPDATE_TYPE);
        deviceTypeList.add(HPlusConstants.AIRTOUCH_450_UPDATE_TYPE);
        deviceTypeList.add(HPlusConstants.AIRTOUCH_S_UPDATE_TYPE);

        deviceTypeList.add(HPlusConstants.WATER_SMART_RO_600_TYPE);
        deviceTypeList.add(HPlusConstants.WATER_SMART_RO_400_TYPE);
        deviceTypeList.add(HPlusConstants.WATER_SMART_RO_100_TYPE);
        deviceTypeList.add(HPlusConstants.WATER_SMART_RO_75_TYPE);
        deviceTypeList.add(HPlusConstants.WATER_SMART_RO_50_TYPE);

        return deviceTypeList;
    }

    public static IEnrollFeature getEnrollFeatureByDeviceType(int deviceType) {
        switch (deviceType) {
            case HPlusConstants.AIRTOUCH_S_TYPE:
            case HPlusConstants.AIRTOUCH_S_UPDATE_TYPE:

                return new AirTouchSEnrollFeatureImpl();

            case HPlusConstants.AIRTOUCH_X_TYPE:
                return new AirTouchXEnrollFeatureImpl();

            case HPlusConstants.AIRTOUCH_450_TYPE:
            case HPlusConstants.AIRTOUCH_450_UPDATE_TYPE:

                return new AirTouch450EnrollFeatureImpl();

            case HPlusConstants.AIRTOUCH_FFAC_TYPE:
                return new AirTouchFFACEnrollFeatureImpl();

            case HPlusConstants.AIRTOUCH_JD_TYPE:
                return new AirTouchJDSEnrollFeatureImpl();

            case HPlusConstants.WATER_SMART_RO_600_TYPE:
                return new AquaTouch600SEnrollFeatureImpl();

            case HPlusConstants.AIRTOUCH_XCOMPACT_TYPE:
                return new AirTouchXCompactEnrollFeatureImpl();

            case HPlusConstants.WATER_SMART_RO_400_TYPE:
                return new AquaTouch400SEnrollFeatureImpl();

            case HPlusConstants.AIRTOUCH_X3COMPACT_TYPE:
            case HPlusConstants.AIRTOUCH_X3COMPACT_UPDATE_TYPE:
                return new AirTouchX3CompactEnrollFeatureImpl();

            case HPlusConstants.WATER_SMART_RO_100_TYPE:
                return new AquaTouch100SEnrollFeatureImpl();

            case HPlusConstants.WATER_SMART_RO_75_TYPE:
                return new AquaTouch75SEnrollFeatureImpl();

            case HPlusConstants.WATER_SMART_RO_50_TYPE:
                return new AquaTouch50SEnrollFeatureImpl();

            case HPlusConstants.MAD_AIR_TYPE:
                return new MadAirEnrollFeatureImpl();

        }
        return new AirTouchSEnrollFeatureImpl();
    }

    public static IEnrollFeature getEnrollFeatureByDeviceType(int deviceType, String model) {
        switch (deviceType) {
            case HPlusConstants.MAD_AIR_TYPE:
                return new MadAirEnrollFeatureImpl(model);

        }
        return new MadAirEnrollFeatureImpl();
    }


    public static boolean isApMode(int deviceType) {
        switch (deviceType) {
            case HPlusConstants.AIRTOUCH_S_TYPE:
            case HPlusConstants.AIRTOUCH_X_TYPE:
            case HPlusConstants.AIRTOUCH_JD_TYPE:
                return true;
        }
        return false;
    }
}
