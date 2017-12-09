package com.honeywell.hch.airtouch.ui.enroll.ui.controller.madair;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.honeywell.hch.airtouch.plateform.ble.manager.BLEManager;
import com.honeywell.hch.airtouch.plateform.ble.service.BluetoothLeService;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.enroll.constant.EnrollConstants;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;

/**
 * Created by Jin on 11/23/16.
 * Step 1 - check if BLE is enabled.
 * Step 2 - turn on device, click next button
 * Step 3 - auto scanning / manual select ble
 */
public class MadAirEnrollTurnOnActivity extends EnrollBaseActivity {

    private ImageView mDeviceInfoIv;
    private Button mSettingButton;
    private Button mNextButton;

    private final int DOUBLE_CLICK_INTERVAL = 1000;
    private long mClickTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enroll_mad_air_turn_on);
        initStatusBar();
        initView();
        initDragDownManager(R.id.root_view_id);

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    private void initView() {
        mSettingButton = (Button) findViewById(R.id.mad_air_setting_btn);
        mNextButton = (Button) findViewById(R.id.mad_air_next_btn);
        mDeviceInfoIv = (ImageView) findViewById(R.id.mad_air_device_iv);
        mDeviceInfoIv.setImageResource(EnrollScanEntity.getEntityInstance().getDeviceImage());
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (BLEManager.getInstance().isBLEEnable()) {
            displayTurnOnDevice();
        } else {
            displayTurnOnBluetooth();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mGattUpdateReceiver);
    }

    public void doClick(View v) {
        if (v.getId() == R.id.mad_air_next_btn) {

            if (isClickTooQuick())
                return;

            if (EnrollScanEntity.getEntityInstance().isNoQRcode()) {
                startIntent(MadAirEnrollManualSelectActivity.class);
            } else {
                startIntent(MadAirEnrollPairingActivity.class);
            }

        } else if (v.getId() == R.id.mad_air_setting_btn) {
            startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
        }
    }

    private void displayTurnOnDevice() {
        super.initTitleView(true, getString(R.string.mad_air_enroll_turn_on_device_title), EnrollConstants.TOTAL_THREE_STEP,
                EnrollConstants.STEP_ONE, getString(R.string.mad_air_enroll_turn_on_device_desc), false);

        mNextButton.setVisibility(View.VISIBLE);
        mDeviceInfoIv.setVisibility(View.VISIBLE);
        mSettingButton.setVisibility(View.GONE);
    }

    private void displayTurnOnBluetooth() {
        super.initTitleView(true, getString(R.string.mad_air_enroll_turn_on_bluetooth_title), EnrollConstants.TOTAL_THREE_STEP,
                EnrollConstants.STEP_ONE, getString(R.string.mad_air_enroll_turn_on_bluetooth_desc), false);

        mNextButton.setVisibility(View.GONE);
        mDeviceInfoIv.setVisibility(View.INVISIBLE);
        mSettingButton.setVisibility(View.VISIBLE);
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();

            if (BluetoothLeService.ACTION_PHONE_BLUETOOTH_ON.equals(action)) {
                displayTurnOnDevice();
            } else if (BluetoothLeService.ACTION_PHONE_BLUETOOTH_OFF.equals(action)) {
                displayTurnOnBluetooth();
            }
        }
    };

    private IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_PHONE_BLUETOOTH_ON);
        intentFilter.addAction(BluetoothLeService.ACTION_PHONE_BLUETOOTH_OFF);
        return intentFilter;
    }

    private boolean isClickTooQuick() {
        if (System.currentTimeMillis() - mClickTime < DOUBLE_CLICK_INTERVAL) {
            mClickTime = System.currentTimeMillis();
            return true;
        } else {
            mClickTime = System.currentTimeMillis();
            return false;
        }
    }

}

