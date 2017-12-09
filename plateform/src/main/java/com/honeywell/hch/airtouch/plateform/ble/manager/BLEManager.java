package com.honeywell.hch.airtouch.plateform.ble.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.IBinder;


import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.ble.service.BluetoothLeService;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModelSharedPreference;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceStatus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class BLEManager {

    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private static BLEManager mBleManager;

    /**
     * the default BLUETOOTH Adapter.
     */
    private BluetoothAdapter mBluetoothAdapter;

    private BluetoothLeService mBluetoothLeService;

    /**
     * single instance
     * @return
     */
    public static BLEManager getInstance() {
        if (mBleManager == null) {
            mBleManager = new BLEManager();
        }
        return mBleManager;
    }

    private BLEManager() {}

    /**
     * get the device is support ble or not
     *
     * @return
     */
    public boolean isSupportBLE() {
        return AppManager.getInstance().getApplication().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }


    /**
     * get the ble is enabled or not
     *
     * @return
     */
    public boolean isBLEEnable() {
        BluetoothManager bluetoothManager = (BluetoothManager) AppManager.getInstance()
                .getApplication().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
    }



    /**
     * Get the default BLUETOOTH Adapter for this device.
     *
     * @return
     */
    public BluetoothAdapter getBluetoothAdapter() {
        if (mBluetoothAdapter == null) {
            final BluetoothManager bluetoothManager = (BluetoothManager) AppManager.getInstance()
                    .getApplication().getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }
        return mBluetoothAdapter;
    }

    /**
     * Stops an ongoing Bluetooth LE device scan.
     */
    public void stopBLEScan(BluetoothAdapter.LeScanCallback mLeScanCallback) {
        getBluetoothAdapter().stopLeScan(mLeScanCallback);
    }

    /**
     * Starts a scan for Bluetooth LE devices.
     *
     * @param mLeScanCallback
     */
    public void startBLEScan(BluetoothAdapter.LeScanCallback mLeScanCallback) {
        getBluetoothAdapter().startLeScan(mLeScanCallback);
    }

    public boolean isEnableBluetoothAdapter() {
        return getBluetoothAdapter().isEnabled();
    }

    public boolean isNeedShowOpenBleActivity() {
        return !isEnableBluetoothAdapter();
    }

    /**
     * @param service
     */
    public void setBleServiceFromServiceConnected(IBinder service) {
        mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
    }

    /**
     * connect BLE
     *
     * @param address
     * @return
     */
    public void connectBle(String address) {
        if (mBluetoothLeService != null) {
            mBluetoothLeService.connect(address);
        }
    }


    public void bondBle(BluetoothDevice device) {
        if (mBluetoothLeService != null) {
            mBluetoothLeService.bondDevice(device);
        }
    }


    /**
     * disconnect ble service
     */

    public void disconnectAllDevices() {
        for (MadAirDeviceModel device : MadAirDeviceModelSharedPreference.getDeviceList()) {
            if (!device.getDeviceName().equals(""))
                disconnectBle(device.getMacAddress());
        }
    }

    public void disconnectBle(String address) {
        if (mBluetoothLeService != null) {
            mBluetoothLeService.disconnect(address);

            MadAirDeviceModelSharedPreference.saveStatus(address, MadAirDeviceStatus.DISCONNECT);
//            removeBond(getBondDevice(address));
        }
    }


    private void removeBond(BluetoothDevice device) {
        if (device == null) {
            return;
        }
        try {
            LogUtil.log(LogUtil.LogLevel.ERROR, "removeBond", "Unpairing BT device " + device.getName() + "...");
            Class<?> btDeviceInstance = Class.forName(BluetoothDevice.class.getCanonicalName());
            Method removeBondMethod = btDeviceInstance.getMethod("removeBond");
            removeBondMethod.invoke(device);
        } catch (Throwable th) {
            LogUtil.log(LogUtil.LogLevel.ERROR, "removeBond", "Exception thrown");
            th.printStackTrace();
            return;
        }
        LogUtil.log(LogUtil.LogLevel.DEBUG, "removeBond", "Device " + device.getName() + " unpaired.");
    }

    public BluetoothDevice getBondDevice(String address) {
        Set<BluetoothDevice> devices = getBluetoothAdapter().getBondedDevices();
        if (devices != null && devices.size() > 0) {
            for (BluetoothDevice bluetoothDevice : devices) {
                if (bluetoothDevice.getAddress().equals(address)) {
                    return bluetoothDevice;
                }
            }
        }

        return null;
    }

    /**
     * reset ble service to null
     */
    public void resetBleService() {
        mBluetoothLeService = null;
    }

    /**
     * init ble service
     *
     * @return
     */
    public boolean initBluetoothLeService() {
        if (mBluetoothLeService != null && mBluetoothLeService.initialize()) {
            return true;
        }

        return false;
    }

    public List<BluetoothDevice> getConnectedDevices() {
        return mBluetoothLeService.getConnectedDevices();
    }

    public BluetoothLeService getBluetoothLeService() {
        return mBluetoothLeService;
    }

    public void setBluetoothLeService(BluetoothLeService mBluetoothLeService) {
        this.mBluetoothLeService = mBluetoothLeService;
    }

    public void setNotification(String address, String serviceUuid, String characterUuid,
                                String descriptorUuid, boolean enable) {
        BluetoothGattService bluetoothGattService
                = mBluetoothLeService.getService(address, UUID.fromString(serviceUuid));

        if (bluetoothGattService != null) {
            BluetoothGattCharacteristic characteristic
                    = bluetoothGattService.getCharacteristic(UUID.fromString(characterUuid));

            mBluetoothLeService.setCharacteristicNotification(characteristic, descriptorUuid, address, enable);
        } else {
            LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "bluetoothGattService is null");
        }
    }

    public void writeCharacteristic(String address, byte[] value, String serviceUuid, String characterUuid) {
        BluetoothGattService bluetoothGattService
                = mBluetoothLeService.getService(address, UUID.fromString(serviceUuid));
        if (bluetoothGattService != null) {
            BluetoothGattCharacteristic characteristic
                    = bluetoothGattService.getCharacteristic(UUID.fromString(characterUuid));
            mBluetoothLeService.writeCharacteristic(characteristic, address, value);
        } else {
            LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "bluetoothGattService is null");
        }
    }

    public void readCharacteristic(String address, String serviceUuid, String characterUuid) {
        BluetoothGattService bluetoothGattService
                = mBluetoothLeService.getService(address, UUID.fromString(serviceUuid));

        if (bluetoothGattService != null) {
            BluetoothGattCharacteristic characteristic
                    = bluetoothGattService.getCharacteristic(UUID.fromString(characterUuid));
            mBluetoothLeService.readCharacteristic(characteristic, address);
        } else {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "bluetoothGattService is null");
        }
    }

    public void pollCharacteristic(String address) {
        List<BluetoothGattService> services = mBluetoothLeService.getSupportedGattServices(address);
        for (BluetoothGattService service : services) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "s:" + service.getUuid());

            List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
            for (BluetoothGattCharacteristic characteristic : characteristics) {
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "c:" + characteristic.getUuid());
            }
        }
    }


    public void unregisterBleServiceBroadcast(){
        if (mBluetoothLeService != null){
            mBluetoothLeService.unRegisterServiceBroadcast();
        }
    }

}
