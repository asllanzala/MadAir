package com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.honeywell.hch.airtouch.library.util.CryptoUtil;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.SHA1Util;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.ble.manager.BLEManager;
import com.honeywell.hch.airtouch.plateform.ble.service.BluetoothLeService;
import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.BleUuidKey;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModelSharedPreference;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceStatus;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirHistoryRecord;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.request.RequestType;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirMotorSpeed;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.request.RequestCommand;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.request.SimpleRequestFactory;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.response.MotorSpeedResponse;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.response.ResponseCommand;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.response.ResponseType;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.response.SimpleResponseFactory;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Qian Jin on 11/9/16.
 */

public class MadAirBleDataManager implements IMadAirBLEDataManager {

    public static final int CONNECTED = 1;
    public static final int DISCONNECTED = 2;
    private HashMap<String, Integer> mDeviceStatus = new HashMap<>();

    private static BLEManager mBleManager;
    private static MadAirBleDataManager mMadAirBleDataManager;
    private DeviceStatusListener mDeviceStatusListener;

    private byte[] mSyncTimeStampHigh;
    private byte[] mSyncTimeStampLow;
    private long mSyncTimeStamp;
    private static final int BLE_OPER = 90000;
    private static final String TAG = "MadAir";


    public static MadAirBleDataManager getInstance() {
        if (mMadAirBleDataManager == null) {
            mMadAirBleDataManager = new MadAirBleDataManager();
        }
        return mMadAirBleDataManager;
    }

    @Override
    public void changeMotorSpeed(String macAddress, MadAirMotorSpeed madAirMotorSpeed) {
        RequestCommand requestCommand = SimpleRequestFactory.createMotorRequestCommand(madAirMotorSpeed);
        writeData(macAddress, requestCommand);
    }

    @Override
    public void saveTodayPm25(String macAddress, int pm25) {
        MadAirDeviceModelSharedPreference.saveTodayPm25(macAddress, pm25);

    }

    private MadAirBleDataManager() {
        mBleManager = BLEManager.getInstance();
    }

    public interface DeviceStatusListener {
        void onChange(String macAddress, int status);
    }

    public void setDeviceStatusListener(DeviceStatusListener deviceStatusListener) {
        mDeviceStatusListener = deviceStatusListener;
    }

    public void registerBleReceiver() {
        AppManager.getInstance().getApplication().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    public void unregisterBleReceiver() {
        AppManager.getInstance().getApplication().unregisterReceiver(mGattUpdateReceiver);
    }

    /**
     * Characteristic Notification
     */
    public void setNotification(String address) {
        mBleManager.setNotification(address, BleUuidKey.MAD_AIR_SERVICE,
                BleUuidKey.MAD_AIR_RW_CHARACTER, BleUuidKey.CLIENT_CHARACTERISTIC_CONFIG, true);
    }

    /**
     * Characteristic Read device info
     */
    public void readDeviceInfoCharacteristic(String address) {
        mBleManager.readCharacteristic(address, BleUuidKey.DEVICE_INFO, BleUuidKey.FIRMWARE_NUMBER);
    }

    /**
     * Command request API
     */
    public void requestSync(String macAddress) {
        RequestCommand requestCommand = SimpleRequestFactory.createRequestCommand(RequestType.SYNC);

        mSyncTimeStamp = ((requestCommand.getDataBytes()[5] & 0xFF) | ((requestCommand.getDataBytes()[4] & 0xFF) << 8)
                | ((requestCommand.getDataBytes()[3] & 0xFF) << 16) | ((requestCommand.getDataBytes()[2] & 0xFF) << 24));

        if (requestCommand.getDataBytes().length > 5) {
            mSyncTimeStampHigh = new byte[]{requestCommand.getDataBytes()[2], requestCommand.getDataBytes()[3]};
            mSyncTimeStampLow = new byte[]{requestCommand.getDataBytes()[4], requestCommand.getDataBytes()[5]};
        }

        mBleManager.writeCharacteristic(macAddress, requestCommand.getDataBytes(),
                BleUuidKey.MAD_AIR_SERVICE, BleUuidKey.MAD_AIR_RW_CHARACTER);

    }

    public void requestFlashData(String macAddress) {
        RequestCommand requestCommand = SimpleRequestFactory.createRequestCommand(RequestType.FLASH_DATA);
        writeData(macAddress, requestCommand);
    }

    public void requestReportData(String macAddress) {
        RequestCommand requestCommand = SimpleRequestFactory.createRequestCommand(RequestType.GET_DATA_REPORT);
        writeData(macAddress, requestCommand);
    }

    public void requestAuthentication(String macAddress, Long timeDeviation) {
        byte[] seed = StringUtil.hexStringToByteArray(String.format("%08X", timeDeviation));
        try {
            String hashResult = SHA1Util.sha1(seed);
            mBleManager.writeCharacteristic(macAddress, StringUtil.hexStringToByteArray(hashResult),
                    BleUuidKey.MAD_AIR_SERVICE, BleUuidKey.MAD_AIR_RW_CHARACTER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == BLE_OPER) {
                String address = (String) msg.obj;

                // Step 4 - request report data
                mMadAirBleDataManager.requestReportData(address);
            }
        }
    };

    /**
     * Handles various events fired by the BluetoothLeService.
     * ACTION_GATT_CONNECTED: connected to a GATT server.
     * ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
     * ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
     * ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
     * or notification operations.
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            final String action = intent.getAction();
            final String address = intent.getStringExtra(BluetoothLeService.DEVICE_ADDRESS);
            final byte type = intent.getByteExtra(BluetoothLeService.DEVICE_TYPE, (byte) 0);

            // Step 1 - BLE device found
            if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {

                // Step 2 - set notification
                setNotification(address);

                // save status CONNECT
                mDeviceStatus.put(address, CONNECTED);
                MadAirDeviceModelSharedPreference.saveStatus(address, MadAirDeviceStatus.CONNECT);

                // status callback
                if (mDeviceStatusListener != null)
                    mDeviceStatusListener.onChange(address, CONNECTED);

            } else if (BluetoothLeService.ACTION_DEVICE_PAIRED.equals(action)) {

                // save device model into SharedPreference
                MadAirDeviceModel device = new MadAirDeviceModel(
                        EnrollScanEntity.getEntityInstance().getmMacID(),
                        EnrollScanEntity.getEntityInstance().getmModel());
                MadAirDeviceModelSharedPreference.addDevice(device);
                MadAirDeviceModelSharedPreference.saveType(address, type);

                DIYInstallationState.setLocationId(device.getDeviceId());

            } else if (BluetoothLeService.ACTION_DESCRIPTOR_WRITE.equals(action)) {

                if (isDeviceNeedAuth(address)) {
                    // New Auth Step 2.1 - Sync
                    requestSync(address);
                } else {
                    // Step 3 - request flash data
                    requestFlashData(address);
                }

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

                //这段代码在ui线程上运行，会导致呼吸动画卡顿。这段代码放在线程中，需要把蓝牙相关操作放在ui线程里。
                //否则三星4.3手机，会出现蓝牙操作失败问题
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        byte[] data = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);

                        Bundle bundle = null;
                        ResponseCommand response = null;

                        if (isDeviceNeedAuth(address)) {
                            response = SimpleResponseFactory
                                    .createConcreteAuthResponseCommand(decryptBleDataForAll(address, data));
                        } else {
                            response = SimpleResponseFactory.createConcreteResponseCommand(data);
                        }

                        try {
                            bundle = response.readData();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            return;
                        }
                        if (bundle != null) {

                            int type = bundle.getInt(ResponseCommand.BUNDLE_RESPONSE_TYPE);
                            int breathFreq = bundle.getInt(ResponseCommand.BUNDLE_RESPONSE_BREATH_FREQ);
                            int runningSpeed = bundle.getInt(ResponseCommand.BUNDLE_RESPONSE_RUN_SPEED);
                            int batteryPercent = bundle.getInt(ResponseCommand.BUNDLE_RESPONSE_BATTERY_PERCENT);
                            int batteryRemain = bundle.getInt(ResponseCommand.BUNDLE_RESPONSE_BATTERY_REMAIN);
                            int filterDuration = bundle.getInt(ResponseCommand.BUNDLE_RESPONSE_FILTER_DURATION);
                            int changedSpeed = bundle.getInt(ResponseCommand.BUNDLE_RESPONSE_CHANGED_SPEED);
                            int motorType = bundle.getInt(ResponseCommand.BUNDLE_RESPONSE_MOTOR_TYPE);
                            int alarm = bundle.getInt(ResponseCommand.BUNDLE_RESPONSE_ALARM);

                            switch (type) {

                                // New Auth Step 2.1 - Sync ACK
                                case ResponseType.SYNC_ACK:

                                    // New Auth Step 2.2 - Authentication
                                    requestAuthentication(address, mSyncTimeStamp);

                                    break;

                                // Step 3 - get flash data
                                case ResponseType.FLASH_DATA_ACK:
                                    MadAirHistoryRecord flashData = (MadAirHistoryRecord) bundle
                                            .getSerializable(ResponseCommand.BUNDLE_RESPONSE_FLASH_DATA);

                                    if (flashData != null) {
                                        /*
                                         * 获取Flash data数据，由此得到设备昨天及以前的使用时间，再从本地Model里拿到昨天及以前的pm2.5，计算得到昨天的颗粒物
                                         * 取出历史数据里的颗粒物particleMap，把昨天的颗粒物添加到该map
                                         */
                                        MadAirDeviceModelSharedPreference.saveHistoryRecord(address, flashData);

                                    }

                                    // Step 4 - request report data
                                    Message message = Message.obtain();
                                    message.obj = address;
                                    message.what = BLE_OPER;
                                    mHandle.sendMessage(message);
                                    break;

                                case ResponseType.DATA_REPORT:
                                    // Step 4 - report data callback

                                    //个别手机在蓝牙断开以后还会收到report数据，需要忽略这个report
                                    if (mDeviceStatus.get(address) == CONNECTED) {
                                        MadAirDeviceModelSharedPreference.saveRealTimeData(address,
                                                batteryPercent, batteryRemain, filterDuration, breathFreq, runningSpeed, alarm);
                                    }

                                    break;

                                case ResponseType.MOTOR_ACK:

                                    if (motorType == MotorSpeedResponse.TYPE_AUTHENTICATION) {
                                        // New Auth Step 2.2 - Authentication ACK

                                        // Step 3 - request flash data
                                        requestFlashData(address);

                                    } else if (motorType == MotorSpeedResponse.TYPE_MOTOR) {
                                        MadAirDeviceModelSharedPreference.saveRealTimeData(address, changedSpeed);
                                    }
                                    break;

                                default:
                                    break;
                            }
                        }
                    }
                }).start();


                // Old Step 7 - request read firmware version
            } else if (BluetoothLeService.ACTION_DATA_READ.equals(action)) {

                byte[] value = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                MadAirDeviceModelSharedPreference.saveFirmware(address, new String(value));

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {

                mDeviceStatus.put(address, DISCONNECTED);
                MadAirDeviceModelSharedPreference.saveStatus(address, MadAirDeviceStatus.DISCONNECT);

                if (mDeviceStatusListener != null)
                    mDeviceStatusListener.onChange(address, DISCONNECTED);

            } else if (BluetoothLeService.ACTION_DATA_WRITE.equals(action)) {

            } else if (BluetoothLeService.ACTION_DEVICE_UNPAIR.equals(action)) {

            } else if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {

            }

        }
    };

    private IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_DEVICE_PAIRED);
        intentFilter.addAction(BluetoothLeService.ACTION_DEVICE_UNPAIR);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_WRITE);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_READ);
        intentFilter.addAction(BluetoothLeService.ACTION_DESCRIPTOR_WRITE);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private byte[] encryptBleData(byte[] request) {

        byte[] result = new byte[20];

        // 数据源改为16字节而不是原来的20字节
        byte[] raw = new byte[16];
        System.arraycopy(request, 0, raw, 0, 16);
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "[encryption] raw data: " + Arrays.toString(raw));

        try {
            byte[] encrypted = CryptoUtil.encryptAES(generateKey(), raw);
            System.arraycopy(encrypted, 0, result, 0, 20);

            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "[encryption] AES data result: " + Arrays.toString(result));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private byte[] decryptBleDataForAll(String address, byte[] data) {
        byte[] result = data;

        // Sync和Auth Ack这两步不做decrypt
        Bundle bundle = null;
        ResponseCommand response = SimpleResponseFactory.createConcreteResponseCommand(data);
        try {
            bundle = response.readData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (bundle != null) {
            switch (bundle.getInt(ResponseCommand.BUNDLE_RESPONSE_TYPE)) {

                case ResponseType.SYNC_ACK:
                    return result;

                case ResponseType.MOTOR_ACK:
                    if (bundle.getInt(ResponseCommand.BUNDLE_RESPONSE_MOTOR_TYPE)
                            == MotorSpeedResponse.TYPE_AUTHENTICATION)
                        return result;
            }
        }

        if (isDeviceNeedAuth(address))
            result = decryptBleData(data);

        return result;

    }

    private void writeData(String macAddress, RequestCommand requestCommand) {
        if (isDeviceNeedAuth(macAddress)) {
            byte[] encryptedData = encryptBleData(requestCommand.getDataBytes());
            mBleManager.writeCharacteristic(macAddress, encryptedData,
                    BleUuidKey.MAD_AIR_SERVICE, BleUuidKey.MAD_AIR_RW_CHARACTER);
        } else {
            mBleManager.writeCharacteristic(macAddress, requestCommand.getDataBytes(),
                    BleUuidKey.MAD_AIR_SERVICE, BleUuidKey.MAD_AIR_RW_CHARACTER);
        }
    }

    private boolean isDeviceNeedAuth(String macAddress) {
        MadAirDeviceModel device = MadAirDeviceModelSharedPreference.getDevice(macAddress);
        return device != null && device.getDeviceType() == HPlusConstants.MAD_AIR_AUTH;
    }

    private byte[] generateKey() {
        final String HONEYWELL = "Honeywell";
        final String KEY = "Key";
        byte[] key = new byte[16];
        System.arraycopy(HONEYWELL.getBytes(), 0, key, 0, HONEYWELL.length());
        System.arraycopy(mSyncTimeStampHigh, 0, key, HONEYWELL.length(), 2);
        System.arraycopy(KEY.getBytes(), 0, key, HONEYWELL.length() + 2, KEY.length());
        System.arraycopy(mSyncTimeStampLow, 0, key, HONEYWELL.length() + 2 + KEY.length(), 2);

        LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "[encryption] generate key: " + Arrays.toString(key));

        return key;
    }

    private byte[] decryptBleData(byte[] data) {
        byte[] result = null;
        byte[] raw = new byte[16];

        System.arraycopy(data, 0, raw, 0, 16);

        try {
            result = CryptoUtil.decryptAES(generateKey(), raw);
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "[decryption] result: " + Arrays.toString(result));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


}
