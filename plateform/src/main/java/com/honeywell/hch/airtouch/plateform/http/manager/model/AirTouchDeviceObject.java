package com.honeywell.hch.airtouch.plateform.http.manager.model;

import android.content.Context;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.airtouch.model.AirtouchRunStatus;
import com.honeywell.hch.airtouch.plateform.devices.common.AirTouchControl;
import com.honeywell.hch.airtouch.plateform.devices.feature.Pm25.IPmSensorFeature;
import com.honeywell.hch.airtouch.plateform.devices.feature.Pm25.PmSensorFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.autospeed.AutoSpeedOnlyPmIForFFACImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.autospeed.AutoSpeedOnlyPmValueImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.autospeed.AutoSpeedWithTvocValueNumberImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.autospeed.AutoSpeedWithTvocValueStringImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.autospeed.IAutoSpeedFeature;
import com.honeywell.hch.airtouch.plateform.devices.feature.controlable.airtouch.AirtouchCanControlableFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AirTouch450EnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AirTouchFFACEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AirTouchJDSEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AirTouchSEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AirTouchX3CompactEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AirTouchXCompactEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AirTouchXEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.filter.AirTouch450FilterFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.filter.AirTouch450UpgradeFilterFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.filter.AirTouchSFilterFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.filter.AirTouchSUpgradeFilterFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.filter.AirtouchXFilterFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.filter.FFACFilterFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.filter.JDSFilterFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.filter.X3CompactFilterFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.filter.X3UpgradeCompactFilterFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.filter.X2CompactFilterFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.speed.ISpeedStatusFeature;
import com.honeywell.hch.airtouch.plateform.devices.feature.speed.NineLevelSpeedStatusFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.speed.SevenLevelSpeedStatusFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.status.AirtouchDeviceStausFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.tvoc.ITvocFeature;
import com.honeywell.hch.airtouch.plateform.devices.feature.tvoc.TvocValueNumberFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.tvoc.TvocValueStringFeatureImpl;


public class AirTouchDeviceObject extends HomeDevice {

    protected Context mContext = AppManager.getInstance().getApplication();

    private AirtouchRunStatus mAirtouchDeviceRunStatus;

    private IAutoSpeedFeature iAutoSpeedFeature;
    private ISpeedStatusFeature iSpeedStatusFeature;
    private IPmSensorFeature iPmSensorFeature;
    private ITvocFeature iTvocFeature;


    public AirTouchDeviceObject(int deviceType) {
        mAirtouchDeviceRunStatus = getRunStatusWhenReturnNull();
        addAirtouchDevicesFeature(deviceType);
    }

    public AirTouchDeviceObject(DeviceInfo deviceInfo) {
        mDeviceInfo = deviceInfo;
        mAirtouchDeviceRunStatus = getRunStatusWhenReturnNull();
        addAirtouchDevicesFeature(mDeviceInfo.getDeviceType());
    }

    public AirTouchDeviceObject(DeviceInfo deviceInfo, AirtouchRunStatus runStatus) {
        mAirtouchDeviceRunStatus = runStatus;
        mDeviceInfo = deviceInfo;
        addAirtouchDevicesFeature(mDeviceInfo.getDeviceType());
    }

    /**
     * 创建设备的时候就为所属的设备进行特性的赋值。如果特性最后没有赋值，说明这个设备没有这个特性
     *
     * @param deviceType
     */
    private void addAirtouchDevicesFeature(int deviceType) {
        addDeviceStatusFeature();
        addPmSensorValueFeature();
        addControlableFeature();

        addFilterFeature(deviceType);
        addEnrollFeature(deviceType);
        addAutoSpeedFeature(deviceType);
        addSpeedStatusFeature(deviceType);
        addTvocFeature(deviceType);

    }

    private void addTvocFeature(int deviceType) {
        switch (deviceType) {
            case HPlusConstants.AIRTOUCH_450_TYPE:
            case HPlusConstants.AIRTOUCH_XCOMPACT_TYPE:
            case HPlusConstants.AIRTOUCH_X3COMPACT_TYPE:
            case HPlusConstants.AIRTOUCH_X3COMPACT_UPDATE_TYPE:
            case HPlusConstants.AIRTOUCH_450_UPDATE_TYPE:
            case HPlusConstants.AIRTOUCH_S_UPDATE_TYPE:

                iTvocFeature = new TvocValueStringFeatureImpl(mAirtouchDeviceRunStatus);
                break;

            case HPlusConstants.AIRTOUCH_X_TYPE:
                iTvocFeature = new TvocValueNumberFeatureImpl(mAirtouchDeviceRunStatus);
                break;

        }
    }

    private void addPmSensorValueFeature() {

        iPmSensorFeature = new PmSensorFeatureImpl(mAirtouchDeviceRunStatus);
    }

    private void addControlableFeature() {
        iControlFeature = new AirtouchCanControlableFeatureImpl(mAirtouchDeviceRunStatus);
    }

    private void addSpeedStatusFeature(int deviceType) {
        switch (deviceType) {
            case HPlusConstants.AIRTOUCH_JD_TYPE:
            case HPlusConstants.AIRTOUCH_450_TYPE:
            case HPlusConstants.AIRTOUCH_S_TYPE:
            case HPlusConstants.AIRTOUCH_450_UPDATE_TYPE:
            case HPlusConstants.AIRTOUCH_S_UPDATE_TYPE:

                iSpeedStatusFeature = new SevenLevelSpeedStatusFeatureImpl(mAirtouchDeviceRunStatus, HPlusConstants.AIR_TOUCH_S_CONTROL_POINT_TOTAL, HPlusConstants.AIR_TOUCH_2_POINT_PER_SPEED
                        , HPlusConstants.SPEED1, HPlusConstants.SPEED2);
                break;

            case HPlusConstants.AIRTOUCH_X_TYPE:
            case HPlusConstants.AIRTOUCH_XCOMPACT_TYPE:
            case HPlusConstants.AIRTOUCH_X3COMPACT_TYPE:
            case HPlusConstants.AIRTOUCH_X3COMPACT_UPDATE_TYPE:
                iSpeedStatusFeature = new NineLevelSpeedStatusFeatureImpl(mAirtouchDeviceRunStatus, HPlusConstants.AIR_TOUCH_X_CONTROL_POINT_TOTAL, HPlusConstants.AIR_TOUCH_2_POINT_PER_SPEED
                        , HPlusConstants.SPEED1, HPlusConstants.SPEED2);
                break;


            case HPlusConstants.AIRTOUCH_FFAC_TYPE:
                iSpeedStatusFeature = new SevenLevelSpeedStatusFeatureImpl(mAirtouchDeviceRunStatus, HPlusConstants.AIR_TOUCH_S_CONTROL_POINT_TOTAL, HPlusConstants.AIR_TOUCH_3_POINT_PER_SPEED
                        , HPlusConstants.SPEED1, HPlusConstants.SPEED2);
                break;

        }
    }

    private void addAutoSpeedFeature(int deviceType) {
        switch (deviceType) {
            case HPlusConstants.AIRTOUCH_JD_TYPE:
            case HPlusConstants.AIRTOUCH_450_TYPE:
            case HPlusConstants.AIRTOUCH_450_UPDATE_TYPE:
                iAutoSpeedFeature = new AutoSpeedWithTvocValueStringImpl(mAirtouchDeviceRunStatus);
                break;

            case HPlusConstants.AIRTOUCH_S_TYPE:
            case HPlusConstants.AIRTOUCH_S_UPDATE_TYPE:
                iAutoSpeedFeature = new AutoSpeedOnlyPmValueImpl(mAirtouchDeviceRunStatus);

            case HPlusConstants.AIRTOUCH_X_TYPE:
            case HPlusConstants.AIRTOUCH_XCOMPACT_TYPE:
            case HPlusConstants.AIRTOUCH_X3COMPACT_TYPE:
            case HPlusConstants.AIRTOUCH_X3COMPACT_UPDATE_TYPE:
                iAutoSpeedFeature = new AutoSpeedWithTvocValueNumberImpl(mAirtouchDeviceRunStatus);
                break;

            case HPlusConstants.AIRTOUCH_FFAC_TYPE:
                iAutoSpeedFeature = new AutoSpeedOnlyPmIForFFACImpl(mAirtouchDeviceRunStatus);
                break;

        }
    }

    private void addEnrollFeature(int deviceType) {
        switch (deviceType) {
            case HPlusConstants.AIRTOUCH_JD_TYPE:
                iEnrollFeature = new AirTouchJDSEnrollFeatureImpl();
            case HPlusConstants.AIRTOUCH_S_TYPE:
            case HPlusConstants.AIRTOUCH_S_UPDATE_TYPE:
                iEnrollFeature = new AirTouchSEnrollFeatureImpl();
                break;
            case HPlusConstants.AIRTOUCH_X_TYPE:
                iEnrollFeature = new AirTouchXEnrollFeatureImpl();
                break;
            case HPlusConstants.AIRTOUCH_450_TYPE:
            case HPlusConstants.AIRTOUCH_450_UPDATE_TYPE:
                iEnrollFeature = new AirTouch450EnrollFeatureImpl();
                break;
            case HPlusConstants.AIRTOUCH_FFAC_TYPE:
                iEnrollFeature = new AirTouchFFACEnrollFeatureImpl();
                break;
            case HPlusConstants.AIRTOUCH_XCOMPACT_TYPE:
                iEnrollFeature = new AirTouchXCompactEnrollFeatureImpl();
                break;
            case HPlusConstants.AIRTOUCH_X3COMPACT_TYPE:
            case HPlusConstants.AIRTOUCH_X3COMPACT_UPDATE_TYPE:
                iEnrollFeature = new AirTouchX3CompactEnrollFeatureImpl();
                break;

        }
    }

    private void addDeviceStatusFeature() {
        iDeviceStatusFeature = new AirtouchDeviceStausFeatureImpl(mAirtouchDeviceRunStatus);
    }

    private void addFilterFeature(int deviceType) {
        switch (deviceType) {
            case HPlusConstants.AIRTOUCH_JD_TYPE:
                iFilterFeature = new JDSFilterFeatureImpl();
            case HPlusConstants.AIRTOUCH_S_TYPE:
                iFilterFeature = new AirTouchSFilterFeatureImpl();
                break;
            case HPlusConstants.AIRTOUCH_X_TYPE:
                iFilterFeature = new AirtouchXFilterFeatureImpl();
                break;
            case HPlusConstants.AIRTOUCH_450_TYPE:
                iFilterFeature = new AirTouch450FilterFeatureImpl();
                break;
            case HPlusConstants.AIRTOUCH_FFAC_TYPE:
                iFilterFeature = new FFACFilterFeatureImpl();
                break;
            case HPlusConstants.AIRTOUCH_XCOMPACT_TYPE:
                iFilterFeature = new X2CompactFilterFeatureImpl();
                break;
            case HPlusConstants.AIRTOUCH_X3COMPACT_TYPE:
                iFilterFeature = new X3CompactFilterFeatureImpl();
                break;
            case HPlusConstants.AIRTOUCH_X3COMPACT_UPDATE_TYPE:
                iFilterFeature = new X3UpgradeCompactFilterFeatureImpl();
                break;
            case HPlusConstants.AIRTOUCH_S_UPDATE_TYPE:
                iFilterFeature = new AirTouchSUpgradeFilterFeatureImpl();
                break;
            case HPlusConstants.AIRTOUCH_450_UPDATE_TYPE:
                iFilterFeature = new AirTouch450UpgradeFilterFeatureImpl();
                break;
        }
    }


    public IAutoSpeedFeature getAutoSpeedFeature() {
        return iAutoSpeedFeature;
    }

    public ISpeedStatusFeature getSpeedStatusFeature() {
        return iSpeedStatusFeature;
    }

    public IPmSensorFeature getPmSensorFeature() {
        return iPmSensorFeature;
    }

    public ITvocFeature getTvocFeature() {
        return iTvocFeature;
    }

    public boolean canShowTvoc() {
        return iTvocFeature != null;
    }


    /**
     * 获取TVOC的原始读数，不用除以1000的
     *
     * @return
     */
    public int getTVOCOriginalNumber() {
        return mAirtouchDeviceRunStatus.getTvocValue();
    }


    public boolean isModeOn() {
        return AirTouchControl.isModeOn(mAirtouchDeviceRunStatus);
    }


    public AirtouchRunStatus getAirtouchDeviceRunStatus() {
        return mAirtouchDeviceRunStatus;
    }

    public void setAirtouchDeviceRunStatus(AirtouchRunStatus mAirtouchDeviceRunStatus) {
        if (mAirtouchDeviceRunStatus == null) {
            mAirtouchDeviceRunStatus = getRunStatusWhenReturnNull();
        }
        this.mAirtouchDeviceRunStatus = mAirtouchDeviceRunStatus;
        addAirtouchDevicesFeature(mDeviceInfo.getDeviceType());
    }

    /**
     * set the default runstatus when server return null
     */
    public AirtouchRunStatus getRunStatusWhenReturnNull() {
        AirtouchRunStatus runStatus = new AirtouchRunStatus();
        runStatus.setmPM25Value(HPlusConstants.ERROR_MAX_VALUE);
        if (mDeviceInfo != null) {
            runStatus.setIsAlive(mDeviceInfo.getIsAlive());
        }
        runStatus.setScenarioMode(HPlusConstants.MODE_DEFAULT_NONE_MODE);
        runStatus.setTvocValue(HPlusConstants.ERROR_MAX_VALUE);
        runStatus.setFilter1Runtime(HPlusConstants.ERROR_FILTER_RUNTIME);
        runStatus.setFilter2Runtime(HPlusConstants.ERROR_FILTER_RUNTIME);
        runStatus.setFilter3Runtime(HPlusConstants.ERROR_FILTER_RUNTIME);
        return runStatus;
    }


    @Override
    public int getDeviceId() {
        return mDeviceInfo.getDeviceID();
    }
}
