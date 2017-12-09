package com.honeywell.hch.airtouch.ui.enroll.ui.controller.madair;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.ble.manager.BleScanManager;
import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModelSharedPreference;
import com.honeywell.hch.airtouch.plateform.permission.Permission;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.enroll.constant.EnrollConstants;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;
import com.honeywell.hch.airtouch.ui.enroll.ui.view.EnrollLoadingButton;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.manager.MadAirBleDataManager;


/**
 * Created by Jin on 11/23/16.
 */
public class MadAirEnrollPairingActivity extends MadAirEnrollBaseActivity {

    private ImageView mDeviceInfoIv;
    private EnrollLoadingButton mEnrollLoadingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.enroll_mad_air_pairing);

        mBleScanManager = new BleScanManager();

        initStatusBar();
        initView();
        initDragDownManager(R.id.root_view_id);
    }

    private void initView() {
        super.initTitleView(true, getString(R.string.mad_air_enroll_turn_on_device_title), EnrollConstants.TOTAL_THREE_STEP,
                EnrollConstants.STEP_ONE, getString(R.string.mad_air_enroll_turn_on_device_desc), false);

        mDeviceInfoIv = (ImageView) findViewById(R.id.mad_air_device_iv);
        mDeviceInfoIv.setImageResource(EnrollScanEntity.getEntityInstance().getDeviceImage());

        mEnrollLoadingBtn = (EnrollLoadingButton) findViewById(R.id.bleBtn_id);
        mEnrollLoadingBtn.initLoadingText(getString(R.string.mad_air_enroll_manual_select_pair), getString(R.string.mad_air_enroll_manual_select_pairing));
        mEnrollLoadingBtn.setButtonStatus(false, false);

        mBackFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopScan();

                backIntent();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

//        startScan();

        // device connected, go to next page.
        MadAirBleDataManager.getInstance().setDeviceStatusListener(new MadAirBleDataManager.DeviceStatusListener() {
            @Override
            public void onChange(String address, int status) {
                switch (status) {
                    case MadAirBleDataManager.CONNECTED:

                        // 排除自动连接也有会连上设备的可能，连上的设备必须是用户扫描二维码的那个
                        if (!StringUtil.isEmpty(address) && address.equals(EnrollScanEntity.getEntityInstance().getmMacID())
                                && !MadAirDeviceModelSharedPreference.hasDevice(address)) {

                            stopScan();

                            startIntent(MadAirEnrollNameDeviceActivity.class);
                        }

                        break;

                    default:
                        break;
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        mHPlusPermission.checkAndRequestPermission(Permission.PermissionCodes.LOCATION_SERVICE_REQUEST_CODE, this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        stopScan();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

            stopScan();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void startScan() {

        mBleScanManager.setScanTimeoutListener(new BleScanManager.ScanTimeoutListener() {
            @Override
            public void onTimeout() {

                stopScan();

                startIntent(MadAirEnrollPairFailActivity.class);
            }
        });

        mEnrollLoadingBtn.setButtonStatus(false, true);
        mBleScanManager.startScanTimeoutTimer();
        mBleScanManager.startBleScan(EnrollScanEntity.getEntityInstance().getmMacID(), this);
    }

}
