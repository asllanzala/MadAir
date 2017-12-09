package com.honeywell.hch.airtouch.plateform.http.manager.model;

import android.content.Context;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.plateform.devices.feature.controlable.airtouch.AirtouchCanControlableFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.controlable.madair.MadAirCanControlableFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.MadAirEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.filter.MadAirFilterFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;

/**
 * Created by Vincent on 30/11/16.
 */

public class MadAirDeviceObject extends HomeDevice {
    protected Context mContext = AppManager.getInstance().getApplication();
    private MadAirDeviceModel mMadAirDeviceModel;

    public MadAirDeviceObject(MadAirDeviceModel madAirDeviceModel) {
        mMadAirDeviceModel = madAirDeviceModel;
        addAirtouchDevicesFeature(mMadAirDeviceModel.getDeviceType());
    }

    @Override
    public int getDeviceId() {
        return mMadAirDeviceModel.getDeviceId();
    }

    /**
     * 创建设备的时候就为所属的设备进行特性的赋值。如果特性最后没有赋值，说明这个设备没有这个特性
     *
     * @param deviceType
     */
    private void addAirtouchDevicesFeature(int deviceType) {

        addFilterFeature(deviceType);
        addEnrollFeature(deviceType);
        addControlableFeature();
    }

    private void addControlableFeature(){
        iControlFeature = new MadAirCanControlableFeatureImpl(mMadAirDeviceModel);
    }

    private void addEnrollFeature(int deviceType) {
        if (DeviceType.isMadAIrSeries(deviceType))
            iEnrollFeature = new MadAirEnrollFeatureImpl();
    }

    private void addFilterFeature(int deviceType) {
        if (DeviceType.isMadAIrSeries(deviceType))
            iFilterFeature = new MadAirFilterFeatureImpl();
    }

    public MadAirDeviceModel getmMadAirDeviceModel() {
        return mMadAirDeviceModel;
    }

    public void setmMadAirDeviceModel(MadAirDeviceModel mMadAirDeviceModel) {
        this.mMadAirDeviceModel = mMadAirDeviceModel;
    }

    public int getDeviceType() {
        return mMadAirDeviceModel.getDeviceType();
    }
}
