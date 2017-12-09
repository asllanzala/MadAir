package com.honeywell.hch.airtouch.plateform.devices.water.model;


import android.content.Context;

import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.DeviceConstant;
import com.honeywell.hch.airtouch.plateform.devices.feature.controlable.water.WaterCanControlableFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.controlable.water.WaterUnControlableFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AquaTouch100SEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AquaTouch400SEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AquaTouch50SEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AquaTouch600SEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AquaTouch75SEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.filter.Water100SFilterFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.filter.Water400SFilterFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.filter.Water50SFilterFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.filter.Water600SFilterFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.filter.Water75SFilterFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.quality.AquaTouchQualityFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.quality.IWaterQualityFeature;
import com.honeywell.hch.airtouch.plateform.devices.feature.status.WaterDeviceStausFeatureImpl;
import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceInfo;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.model.device.AquaTouchRunstatus;
import com.honeywell.hch.airtouch.plateform.http.model.device.SmartROFilterInfo;

import java.util.ArrayList;


public class WaterDeviceObject extends HomeDevice {

    protected Context mContext = AppManager.getInstance().getApplication();

    private AquaTouchRunstatus mAquaTouchRunStatus;


    private IWaterQualityFeature iWaterQualityFeature;

    public WaterDeviceObject(DeviceInfo deviceInfo) {
        mDeviceInfo = deviceInfo;
        mAquaTouchRunStatus = getRunStatusWhenReturnNull();
        addDevicesFeature();
    }

    public WaterDeviceObject(DeviceInfo deviceInfo,AquaTouchRunstatus aquaTouchRunstatus) {
        mDeviceInfo = deviceInfo;
        mAquaTouchRunStatus = aquaTouchRunstatus;
        addDevicesFeature();
    }

    private void addDevicesFeature(){
        addWaterQualityFeature();
        addDeviceStatusFeature();
        addFilterFeature(mDeviceInfo.getDeviceType());
        addEnrollFeature(mDeviceInfo.getDeviceType());
        addControlableFeature(mDeviceInfo.getDeviceType());
    }


    private void addWaterQualityFeature(){
        iWaterQualityFeature = new AquaTouchQualityFeatureImpl(mAquaTouchRunStatus);
    }

    private void addFilterFeature(int deviceType){
        switch (deviceType) {
            case HPlusConstants.WATER_SMART_RO_600_TYPE:
                iFilterFeature = new Water600SFilterFeatureImpl();
                break;
            case HPlusConstants.WATER_SMART_RO_400_TYPE:
                iFilterFeature = new Water400SFilterFeatureImpl();
                break;

            case HPlusConstants.WATER_SMART_RO_100_TYPE:
                iFilterFeature = new Water100SFilterFeatureImpl();
                break;
            case HPlusConstants.WATER_SMART_RO_75_TYPE:
                iFilterFeature = new Water75SFilterFeatureImpl();
                break;
            case HPlusConstants.WATER_SMART_RO_50_TYPE:
                iFilterFeature = new Water50SFilterFeatureImpl();
                break;
        }
    }

    private void addEnrollFeature(int deviceType){
        switch (deviceType) {
            case HPlusConstants.WATER_SMART_RO_600_TYPE:
                iEnrollFeature = new AquaTouch600SEnrollFeatureImpl();
                break;
            case HPlusConstants.WATER_SMART_RO_400_TYPE:
                iEnrollFeature = new AquaTouch400SEnrollFeatureImpl();
                break;
            case HPlusConstants.WATER_SMART_RO_100_TYPE:
                iEnrollFeature = new AquaTouch100SEnrollFeatureImpl();
                break;
            case HPlusConstants.WATER_SMART_RO_75_TYPE:
                iEnrollFeature = new AquaTouch75SEnrollFeatureImpl();
                break;
            case HPlusConstants.WATER_SMART_RO_50_TYPE:
                iEnrollFeature = new AquaTouch50SEnrollFeatureImpl();
                break;
        }
    }

    private void addDeviceStatusFeature(){
        iDeviceStatusFeature = new WaterDeviceStausFeatureImpl(mAquaTouchRunStatus);
    }


    private void addControlableFeature(int deviceType){
        switch (deviceType) {
            case HPlusConstants.WATER_SMART_RO_600_TYPE:
            case HPlusConstants.WATER_SMART_RO_400_TYPE:
                iControlFeature = new WaterCanControlableFeatureImpl(mAquaTouchRunStatus);
                break;
            case HPlusConstants.WATER_SMART_RO_100_TYPE:
            case HPlusConstants.WATER_SMART_RO_75_TYPE:
            case HPlusConstants.WATER_SMART_RO_50_TYPE:
                iControlFeature = new WaterUnControlableFeatureImpl(mAquaTouchRunStatus);
                break;
        }
    }


    public IWaterQualityFeature getWaterQualityFeature() {
        return iWaterQualityFeature;
    }


    public AquaTouchRunstatus getAquaTouchRunstatus() {
        return mAquaTouchRunStatus;
    }

    public void setAquaTouchRunstatus(AquaTouchRunstatus mDeviceRunStatus) {
        if (mDeviceRunStatus == null) {
            mDeviceRunStatus = getRunStatusWhenReturnNull();
        }
        this.mAquaTouchRunStatus = mDeviceRunStatus;
        addDevicesFeature();
    }


    public String getErrorAlarm() {
        int[] errorFlag = mAquaTouchRunStatus.getErrFlags();
        if (errorFlag.length > 0) {
            switch (errorFlag[0]) {
                case DeviceConstant.PUMPOVERTIMEERR:
                    return AppManager.getInstance().getApplication().getString(R.string.water_pump_over_alarm);
                case DeviceConstant.PUMPFREQBOOTUPERR:
                    return AppManager.getInstance().getApplication().getString(R.string.pump_bootup_alarm);
                case DeviceConstant.PUMPFAULT:
                    return AppManager.getInstance().getApplication().getString(R.string.pump_fault_alarm);
                case DeviceConstant.PIPEBLOCKERR:
                    return AppManager.getInstance().getApplication().getString(R.string.pipe_block_alarm);
                case DeviceConstant.INFTDSFAULT:
                    return AppManager.getInstance().getApplication().getString(R.string.inftds_alarm);
                case DeviceConstant.OUTFTDSFAULT:
                    return AppManager.getInstance().getApplication().getString(R.string.outtds_alarm);
                case DeviceConstant.WATERLEAKAGEERR:
                    return AppManager.getInstance().getApplication().getString(R.string.water_leak_alarm);
                case DeviceConstant.EEPROMERR:
                    return AppManager.getInstance().getApplication().getString(R.string.eeprom_alarm);
                case DeviceConstant.NOWATER:
                    return AppManager.getInstance().getApplication().getString(R.string.no_water);
            }
        }
        return AppManager.getInstance().getApplication().getString(R.string.water_unknow_alarm);
    }



    /**
     * set the default runstatus when server return null
     */
    public AquaTouchRunstatus getRunStatusWhenReturnNull() {
        AquaTouchRunstatus runStatus = new AquaTouchRunstatus();
        runStatus.setWaterQualityLevel(HPlusConstants.WATER_INIT_QUALITY_LEVEL);

        if (getDeviceInfo() != null) {
            runStatus.setIsAlive(getDeviceInfo().getIsAlive());
        }
        runStatus.setErrFlags(new int[]{});
        runStatus.setmFilterInfoList(new ArrayList<SmartROFilterInfo>());
        runStatus.setScenarioMode(HPlusConstants.MODE_DEFAULT_NONE_MODE);
        return runStatus;
    }

    @Override
    public int getDeviceId() {
        return mDeviceInfo.getDeviceID();
    }
}
