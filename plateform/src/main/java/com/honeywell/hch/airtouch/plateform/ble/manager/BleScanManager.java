package com.honeywell.hch.airtouch.plateform.ble.manager;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.ble.service.BluetoothLeService;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Qian Jin on 11/7/16.
 */

public class BleScanManager {

    public static final long SCANNING_TIMEOUT = 30 * 1000;

    private static final String TAG = "BLEScan";

    private BLEManager mBleManager;
    private ScanListener mScanListener;
    private ScanTimeoutListener mScanTimeoutListener;

    private Timer mScanTimer;
    private TimerTask mScanTimeTask;

    private String mDeviceAddress;

    private Activity mActivity;

    public BleScanManager() {
        mBleManager = BLEManager.getInstance();
    }

    public interface ScanListener {
        void onScan(BluetoothDevice device, byte[] scanRecord);
    }

    public interface ScanTimeoutListener {
        void onTimeout();
    }

    public void setScanListener(ScanListener scanListener) {
        this.mScanListener = scanListener;
    }

    public void setScanTimeoutListener(ScanTimeoutListener scanTimeoutListener) {
        this.mScanTimeoutListener = scanTimeoutListener;
    }

    public void startBleScan(String deviceAddress,Activity activity) {
        mActivity = activity;
        mDeviceAddress = deviceAddress;
        mBleManager.startBLEScan(mLeScanAutoConnectCallback);
    }

    public void startBleScan() {
        mBleManager.startBLEScan(mLeScanManualSelectCallback);
    }

    public void stopBleScan() {
        mBleManager.stopBLEScan(mLeScanAutoConnectCallback);
        mBleManager.stopBLEScan(mLeScanManualSelectCallback);
    }

    private BluetoothAdapter.LeScanCallback mLeScanManualSelectCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {

            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "===" + device.getAddress());
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "===" + device.getName());

            if (mScanListener != null)
                mScanListener.onScan(device, scanRecord);
        }
    };

    private BluetoothAdapter.LeScanCallback mLeScanAutoConnectCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {

            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "===" + device.getAddress() +  ", === " + Thread.currentThread().getId());
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "===" + device.getName());

            if (device.getAddress() != null && device.getAddress().equals(mDeviceAddress)) {
                final Intent intent = new Intent(BluetoothLeService.ACTION_DEVICE_PAIRED);
                intent.putExtra(BluetoothLeService.DEVICE_ADDRESS, device.getAddress());
                intent.putExtra(BluetoothLeService.DEVICE_NAME, device.getName());

                // Authentication process
                if (scanRecord != null && scanRecord.length > 25)
                    intent.putExtra(BluetoothLeService.DEVICE_TYPE, scanRecord[25]);

                AppManager.getInstance().getApplication().sendBroadcast(intent);

                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "--> connect device " + device.getName());

                //在Galaxy S4 4.3手机，这个callback不是运行在主线程中，所以会出问题。（蓝牙的所有操作都应该在主线程中操作）
                if (mActivity != null){
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "begin connect device === " + Thread.currentThread().getId());
                            mBleManager.connectBle(device.getAddress());
                        }
                    });
                }

            }
        }
    };

    public void startScanTimeoutTimer() {
        mScanTimer = new Timer();
        mScanTimeTask = new TimerTask() {
            @Override
            public void run() {
                if (mScanTimeoutListener != null)
                    mScanTimeoutListener.onTimeout();
            }
        };

        mScanTimer.schedule(mScanTimeTask, BleScanManager.SCANNING_TIMEOUT);
    }

    public void stopScanTimeoutTimer() {
        if (mScanTimer != null)
            mScanTimer.cancel();

        mScanTimeTask = null;
        mScanTimer = null;
    }

}
