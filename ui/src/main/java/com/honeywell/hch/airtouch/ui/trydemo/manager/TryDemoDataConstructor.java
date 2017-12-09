package com.honeywell.hch.airtouch.ui.trydemo.manager;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.airtouch.model.AirtouchRunStatus;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceStatus;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirHistoryRecord;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirMotorSpeed;
import com.honeywell.hch.airtouch.plateform.devices.water.model.WaterDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceInfo;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.manager.model.MadAirDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.SelectStatusDeviceItem;
import com.honeywell.hch.airtouch.plateform.http.manager.model.VirtualUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.AuthTo;
import com.honeywell.hch.airtouch.plateform.http.model.device.AquaTouchRunstatus;
import com.honeywell.hch.airtouch.plateform.http.model.device.SmartROFilterInfo;
import com.honeywell.hch.airtouch.ui.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by h127856 on 16/11/2.
 */
public class TryDemoDataConstructor {

    public static Object mLockObj = new Object();

    private static ArrayList<SelectStatusDeviceItem> mTryDemoHomeDeviceSelectStatusList = new ArrayList<>();
    private static ArrayList<HomeDevice> mTryDemoHomeDeviceList = new ArrayList<>();

    private static ArrayList<HomeDevice> mTryDemoVirtualDevicesList = new ArrayList<>();

    private static List<Float> historyData = new ArrayList<>();

    public static ArrayList<SelectStatusDeviceItem> getSelectStutasHomeDevicesList() {
        return mTryDemoHomeDeviceSelectStatusList;
    }

    public static ArrayList<HomeDevice> getTryDemoHomeDeviceList(){
        return mTryDemoHomeDeviceList;
    }

    public static ArrayList<HomeDevice> getTryDemoVirtualDevicesList(){
        return mTryDemoVirtualDevicesList;
    }

    public static void initDeviceList() {
        initAirtouchPDemoDevice();

        if (AppManager.getInstance().getLocalProtocol().isTryDemoShowWaterDevice()){
            initAquaTouchRO600();

            constructHistoryParticleData();
            initVirtualDevices();
        }

    }

    public static void exitTryDemo() {
        synchronized (mLockObj){
            mTryDemoHomeDeviceSelectStatusList.clear();
            mTryDemoHomeDeviceList.clear();
            mTryDemoVirtualDevicesList.clear();
            historyData.clear();
        }
    }

    private static void initVirtualDevices(){
        MadAirDeviceModel madAirDeviceModel = new MadAirDeviceModel("",HPlusConstants.MAD_AIR_MODEL_WHITE);
        madAirDeviceModel.setDeviceId(TryDemoConstant.MADAIR_DEVICEID);
        madAirDeviceModel.setDeviceStatus(MadAirDeviceStatus.USING);
        madAirDeviceModel.setmDeviceType(HPlusConstants.MAD_AIR_TYPE);
        madAirDeviceModel.setDeviceName(AppManager.getInstance().getApplication().getString(R.string.try_demo_madair_name));
        madAirDeviceModel.setBatteryPercent(80);
        madAirDeviceModel.setBatteryRemain(130);
        madAirDeviceModel.setFilterUsageDuration(215);
        madAirDeviceModel.setBreathFreq(22);
        madAirDeviceModel.setFirmwareVersion("XX.XX.XX");
        madAirDeviceModel.setMotorSpeed(MadAirMotorSpeed.AUTO_SPEED.getSpeed());
        madAirDeviceModel.setMadAirHistoryRecord(constructMadAirHistoryRecord());
        MadAirDeviceObject madAirDeviceObject = new MadAirDeviceObject(madAirDeviceModel);
        mTryDemoVirtualDevicesList.add(madAirDeviceObject);
    }

    private static MadAirHistoryRecord constructMadAirHistoryRecord(){
        MadAirHistoryRecord madAirHistoryRecord = new MadAirHistoryRecord();

        Calendar theCa = Calendar.getInstance();
        theCa.setTime(new Date());

        //第一天
        theCa.add(theCa.DATE, -30);
        madAirHistoryRecord.getParticleMap().put((theCa.get(Calendar.YEAR)) + "/" + (theCa.get(Calendar.MONTH) + 1) + "/" + theCa.get(Calendar.DAY_OF_MONTH),historyData.get(0));
        for (int i = 0; i < 29; i++) {
            theCa.add(theCa.DATE, +1);
            madAirHistoryRecord.getParticleMap().put((theCa.get(Calendar.YEAR)) + "/" + (theCa.get(Calendar.MONTH) + 1) + "/" + theCa.get(Calendar.DAY_OF_MONTH),historyData.get(i + 1));
        }

        return madAirHistoryRecord;
    }

    private static void constructHistoryParticleData(){
        historyData.add(27f);
        historyData.add(23f);
        historyData.add(13f);
        historyData.add(22f);
        historyData.add(39f);
        historyData.add(34f);
        historyData.add(29f);
        historyData.add(32f);
        historyData.add(39f);
        historyData.add(32f);
        historyData.add(25f);
        historyData.add(16f);
        historyData.add(18f);
        historyData.add(26f);
        historyData.add(38f);
        historyData.add(10f);
        historyData.add(28f);
        historyData.add(29f);
        historyData.add(37f);
        historyData.add(24f);
        historyData.add(32f);
        historyData.add(38f);
        historyData.add(42f);
        historyData.add(19f);
        historyData.add(22f);
        historyData.add(13f);
        historyData.add(29f);
        historyData.add(27f);
        historyData.add(20f);
        historyData.add(18f);

    }


    private static void initAirtouchPDemoDevice() {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceType(HPlusConstants.AIRTOUCH_450_TYPE);
        deviceInfo.setDeviceID(TryDemoConstant.AIRTOUCH_P_DEVICEID);
        deviceInfo.setIsAlive(true);
        deviceInfo.setName(AppManager.getInstance().getApplication().getString(R.string.try_demo_airtouch_name));
        deviceInfo.setPermission(AuthTo.OWNER);

        AirTouchDeviceObject airTouchDeviceObject = new AirTouchDeviceObject(deviceInfo);
        airTouchDeviceObject.setAirtouchDeviceRunStatus(initAirtouchPRunstatus());
        mTryDemoHomeDeviceList.add(airTouchDeviceObject);

        SelectStatusDeviceItem selectStatusDeviceItem = new SelectStatusDeviceItem();
        selectStatusDeviceItem.setDeviceItem(airTouchDeviceObject);
        mTryDemoHomeDeviceSelectStatusList.add(selectStatusDeviceItem);
    }

    private static AirtouchRunStatus initAirtouchPRunstatus() {
        AirtouchRunStatus airtouchRunStatus = new AirtouchRunStatus();
        airtouchRunStatus.setmPM25Value(TryDemoConstant.PM_DEFAULT_VAULE);
        airtouchRunStatus.setTvocValue(TryDemoConstant.TVOC_DEFAULT_VALUE);
        airtouchRunStatus.setScenarioMode(TryDemoConstant.AIRTOUCH_DEFAULT_MODE);
        airtouchRunStatus.setIsAlive(true);
        airtouchRunStatus.setCleanTime(new int[]{});
        airtouchRunStatus.setMobileCtrlFlags(HPlusConstants.ENABLE_CONTROL);
        airtouchRunStatus.setFilter1Runtime(200);
        airtouchRunStatus.setFilter2Runtime(200);
        return airtouchRunStatus;
    }

    private static void initAquaTouchRO600() {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceType(HPlusConstants.WATER_SMART_RO_600_TYPE);
        deviceInfo.setDeviceID(TryDemoConstant.AQUA_TOUCH_RO_600S_DEVICEID);
        deviceInfo.setIsAlive(true);
        deviceInfo.setName(AppManager.getInstance().getApplication().getString(R.string.try_demo_water_name));
        deviceInfo.setPermission(AuthTo.OWNER);


        WaterDeviceObject waterDeviceObject = new WaterDeviceObject(deviceInfo);
        waterDeviceObject.setAquaTouchRunstatus(initWaterRunstatus());
        mTryDemoHomeDeviceList.add(waterDeviceObject);


        SelectStatusDeviceItem selectStatusDeviceItem = new SelectStatusDeviceItem();
        selectStatusDeviceItem.setDeviceItem(waterDeviceObject);
        mTryDemoHomeDeviceSelectStatusList.add(selectStatusDeviceItem);
    }

    private static AquaTouchRunstatus initWaterRunstatus() {
        AquaTouchRunstatus aquaTouchRunstatus = new AquaTouchRunstatus();
        aquaTouchRunstatus.setIsAlive(true);
        aquaTouchRunstatus.setErrFlags(new int[]{});
        aquaTouchRunstatus.setInflowTDS(1000);
        aquaTouchRunstatus.setmMoblieCtrlFlags(HPlusConstants.WATER_ENABLE_CONTROL);
        aquaTouchRunstatus.setOutflowVolume(1000);
        aquaTouchRunstatus.setScenarioMode(TryDemoConstant.WATER_DEFAULT_MODE);
        aquaTouchRunstatus.setWaterQualityLevel(TryDemoConstant.WATER_DEFAULT_QUALITY);

        //构造三个滤网
        SmartROFilterInfo smartROFilterInfo = new SmartROFilterInfo();
        List<SmartROFilterInfo> smartROFilterInfos = new ArrayList<>();
        smartROFilterInfos.add(smartROFilterInfo);
        smartROFilterInfos.add(smartROFilterInfo);
        smartROFilterInfos.add(smartROFilterInfo);

        aquaTouchRunstatus.setmFilterInfoList(smartROFilterInfos);

        return aquaTouchRunstatus;
    }


}
