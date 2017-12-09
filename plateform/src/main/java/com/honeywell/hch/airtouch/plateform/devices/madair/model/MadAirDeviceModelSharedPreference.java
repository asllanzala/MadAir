package com.honeywell.hch.airtouch.plateform.devices.madair.model;

import android.content.Intent;

import com.google.gson.Gson;
import com.honeywell.hch.airtouch.library.util.DateTimeUtil;
import com.honeywell.hch.airtouch.library.util.SharePreferenceUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Qian Jin on 11/23/16.
 */

public class MadAirDeviceModelSharedPreference {

    private static final String PREFERENCE_FILE_NAME = "mad_air_device_model_sp";
    private static final String PREFERENCE_KEY = "device_model_key";

    private static final int AUTH_DEVICE_TYPE = 1;


    public static void addDevice(MadAirDeviceModel newDevice) {

        List<MadAirDeviceModel> devices = getDeviceList();

        if (devices.size() == 0) {
            devices.add(newDevice);

        } else {
            // do not add same device
            MadAirDeviceModel oldDevice = getDevice(newDevice.getMacAddress(), devices);
            if (oldDevice == null)
                devices.add(newDevice);
        }

        saveDevices(devices);
    }

    public static void deleteDevice(String macAddress) {
        List<MadAirDeviceModel> devices = getDeviceList();
        MadAirDeviceModel device = getDevice(macAddress, devices);
        if (device != null) {
            devices.remove(device);
            saveDevices(devices);
        }
    }

    public static boolean hasDevice(String macAddress) {
        List<MadAirDeviceModel> devices = MadAirDeviceModelSharedPreference.getDeviceList();
        MadAirDeviceModel device = getDevice(macAddress, devices);

        return device != null && !StringUtil.isEmpty(device.getDeviceName());
    }

    public static void saveType(String macAddress, int type) {
        List<MadAirDeviceModel> devices = getDeviceList();
        MadAirDeviceModel device = getDevice(macAddress, devices);
        if (device != null) {

            if (type == AUTH_DEVICE_TYPE)
                device.setmDeviceType(HPlusConstants.MAD_AIR_AUTH);
            else
                device.setmDeviceType(HPlusConstants.MAD_AIR_TYPE);

            saveDevices(devices);
        }
    }

    public static void saveStatus(String macAddress, MadAirDeviceStatus status) {
        List<MadAirDeviceModel> devices = getDeviceList();
        MadAirDeviceModel device = getDevice(macAddress, devices);
        if (device != null) {
            device.setDeviceStatus(status);
            saveDevices(devices);
        }
    }

    public static void saveStatus(MadAirDeviceStatus status) {
        List<MadAirDeviceModel> devices = getDeviceList();
        for (MadAirDeviceModel madAirDeviceModel : devices) {
            madAirDeviceModel.setDeviceStatus(status);
        }
        saveDevices(devices);
    }

    public static void saveTodayPm25(String macAddress, int pm25) {
        List<MadAirDeviceModel> devices = getDeviceList();
        MadAirDeviceModel device = getDevice(macAddress, devices);

        if (device != null) {
            String today = DateTimeUtil.getNowDateTimeString(MadAirHistoryRecord.DATE_FORMAT2);

            HashMap<String, Integer> pm25Map = device.getPm25();

            if (pm25Map == null)
                return;

            if (pm25Map.containsKey(today) && pm25Map.get(today) == pm25)
                return;

            pm25Map.put(today, pm25);
            device.setPm25(pm25Map);
            saveDevices(devices);
        }

    }

    public static void savePm25Mock(String macAddress, HashMap<String, Integer> pm25Map) {
        List<MadAirDeviceModel> devices = getDeviceList();
        MadAirDeviceModel device = getDevice(macAddress, devices);
        if (device != null) {
            device.setPm25(pm25Map);
            saveDevices(devices);
        }
    }

    public static void saveHistoryRecordMock(String macAddress, MadAirHistoryRecord flashData,
                                             HashMap<String, Float> particleMap) {
        List<MadAirDeviceModel> devices = getDeviceList();
        MadAirDeviceModel device = getDevice(macAddress, devices);
        if (device != null) {
            flashData.setParticleMap(particleMap);
            device.setMadAirHistoryRecord(flashData);
            saveDevices(devices);
        }
    }

    public static void saveHistoryRecord(String macAddress, MadAirHistoryRecord flashData) {
        List<MadAirDeviceModel> devices = getDeviceList();
        MadAirDeviceModel device = getDevice(macAddress, devices);
        if (device != null) {
            restoreHistoryParticleMap(device, flashData, device.getMadAirHistoryRecord());
            saveDevices(devices);
        }
    }

    private static void restoreHistoryParticleMap(MadAirDeviceModel device,
                                                  MadAirHistoryRecord flashData, MadAirHistoryRecord localRecord) {
        if (flashData == null || device == null)
            return;

        if (localRecord == null)
            localRecord = flashData;

        localRecord.savePm25IntoParticleMap(device.getPm25(), flashData.getFlashDataMap(), localRecord.getParticleMap());
        device.setMadAirHistoryRecord(localRecord);
    }

    public static void saveDeviceName(String macAddress, String name) {
        List<MadAirDeviceModel> devices = getDeviceList();
        MadAirDeviceModel device = getDevice(macAddress, devices);
        if (device != null) {
            device.setDeviceName(name);
            saveDevices(devices);
        }
    }

    public static void saveFirmware(String macAddress, String firmware) {
        List<MadAirDeviceModel> devices = getDeviceList();
        MadAirDeviceModel device = getDevice(macAddress, devices);
        if (device != null) {
            device.setFirmwareVersion(firmware);
            saveDevices(devices);
        }
    }

    public static void saveRealTimeData(String macAddress, int batteryPercent, int batteryRemain,
                                        int filter, int freq, int speed, int alarm) {
        List<MadAirDeviceModel> devices = getDeviceList();
        MadAirDeviceModel device = getDevice(macAddress, devices);

        if (device != null) {
            device.setBatteryPercent(batteryPercent);
            device.setBatteryRemain(batteryRemain);
            device.setFilterUsageDuration(filter);
            device.setBreathFreq(freq);
            device.setMotorSpeed(speed);
            device.setAlarmInfo(alarm);

            if (speed > 0)
                device.setDeviceStatus(MadAirDeviceStatus.USING);
            else
                device.setDeviceStatus(MadAirDeviceStatus.CONNECT);

            saveDevices(devices);
        }
    }

    public static void saveRealTimeData(String macAddress, int speed) {
        List<MadAirDeviceModel> devices = getDeviceList();
        MadAirDeviceModel device = getDevice(macAddress, devices);

        if (device != null) {
            device.setMotorSpeed(speed);
            saveDevices(devices);
        }
    }

    public static List<MadAirDeviceModel> getDeviceList() {
        JSONArray responseArray;
        List<MadAirDeviceModel> results = new ArrayList<>();

        String record = SharePreferenceUtil.getPrefString(PREFERENCE_FILE_NAME, PREFERENCE_KEY, null);

        if (StringUtil.isEmpty(record)) {
            return results;
        }

        try {
            responseArray = new JSONArray(record);

            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject responseJSON = responseArray.getJSONObject(i);
                MadAirDeviceModel model = new Gson().fromJson(responseJSON.toString(), MadAirDeviceModel.class);
                results.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return results;
        }

        return results;
    }

    public static void clearData() {
        SharePreferenceUtil.clearPreference(SharePreferenceUtil
                .getSharedPreferenceInstanceByName(PREFERENCE_FILE_NAME));
    }

    public static MadAirDeviceModel getDevice(String address) {
        List<MadAirDeviceModel> devices = getDeviceList();

        for (MadAirDeviceModel device : devices) {
            if (device.getMacAddress().equals(address))
                return device;
        }

        return null;
    }

    private static MadAirDeviceModel getDevice(String address, List<MadAirDeviceModel> devices) {

        if (devices != null) {
            for (MadAirDeviceModel device : devices) {
                if (device.getMacAddress().equals(address))
                    return device;
            }
        }

        return null;
    }

    private static void saveDevices(List<MadAirDeviceModel> devices) {
        SharePreferenceUtil.setPrefString(PREFERENCE_FILE_NAME, PREFERENCE_KEY,
                new Gson().toJson(devices));

        // Send broadcast to every page relative to MadAir after MadAirModel saved locally.
        UserAllDataContainer.shareInstance().updateMadAirData();
        Intent intent = new Intent(HPlusConstants.REFRESH_MADAIR_DATA);
        AppManager.getInstance().getApplication().getApplicationContext().sendBroadcast(intent);
    }

}
