package com.honeywell.hch.airtouch.ui.enroll.ui.controller.madair;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.ble.manager.BLEManager;
import com.honeywell.hch.airtouch.plateform.ble.manager.BleScanManager;
import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModelSharedPreference;
import com.honeywell.hch.airtouch.plateform.permission.Permission;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.enroll.constant.EnrollConstants;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;
import com.honeywell.hch.airtouch.ui.enroll.ui.view.EnrollLoadingButton;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.manager.MadAirBleDataManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jin on 11/23/16.
 */
public class MadAirEnrollManualSelectActivity extends MadAirEnrollBaseActivity {
    private final String TAG = "MadAirEnrollManualSelect";
    private EnrollLoadingButton mPairingButton;
    private Button mPairButton;
    private ImageView mScanRotateImageView;
    private ListView mScanListView;

    private ScanningDeviceListAdapter mScanDeviceListAdapter;
    private List<String> mBleNameList = new LinkedList<>();
    private HashMap<String, String> mBleMap = new HashMap<>();
    private HashMap<String, Integer> mBleTypeMap = new HashMap<>();
    private String mSelectedBleName;


    private static final String MAD_AIR_BLE_NAME = "MadAir";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enroll_mad_air_manual_select);

        mBleScanManager = new BleScanManager();

        initStatusBar();
        initView();
        initDragDownManager(R.id.root_view_id);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mHPlusPermission.checkAndRequestPermission(Permission.PermissionCodes.LOCATION_SERVICE_REQUEST_CODE, this);

        mBleNameList.clear();

        // refreshMadAirWeather ListView
        mBleScanManager.setScanListener(new BleScanManager.ScanListener() {
            @Override
            public void onScan(BluetoothDevice device, byte[] scanRecord) {
                if (device != null) {
                    String name = device.getName();
                    String address = device.getAddress();

                    if (isBleNameValid(name)) {
                        mBleMap.put(name, address);
                        if (scanRecord != null && scanRecord.length > 25)
                            mBleTypeMap.put(name, (int)scanRecord[25]);
                        mBleNameList.add(name);
                        mScanDeviceListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        initBleListView();
        updateBleListView();

        mPairingButton.setVisibility(View.GONE);
        mPairButton.setVisibility(View.VISIBLE);
        mPairButton.setBackgroundResource(R.drawable.enroll_big_btn_disable);
        mPairButton.setClickable(false);

        // device connected, go to next page.
        MadAirBleDataManager.getInstance().setDeviceStatusListener(new MadAirBleDataManager.DeviceStatusListener() {
            @Override
            public void onChange(String address, int status) {

                switch (status) {
                    case MadAirBleDataManager.CONNECTED:
                        LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "connected address: " + address);
                        // 排除自动连接也有会连上设备的可能，连上的设备必须是用户选择的那个
                        if (!StringUtil.isEmpty(address) && address.equals(mBleMap.get(mSelectedBleName))
                                && !MadAirDeviceModelSharedPreference.hasDevice(address)) {
                            LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "MadAirBleDataManager.CONNECTED--");
                            stopScan();

                            // save device model into SharedPreference
                            MadAirDeviceModel device = new MadAirDeviceModel(
                                    EnrollScanEntity.getEntityInstance().getmMacID(),
                                    EnrollScanEntity.getEntityInstance().getmModel());
                            MadAirDeviceModelSharedPreference.addDevice(device);
                            MadAirDeviceModelSharedPreference.saveType(EnrollScanEntity.getEntityInstance().getmMacID(),
                                    EnrollScanEntity.getEntityInstance().getmDeviceType());

                            startIntent(MadAirEnrollNameDeviceActivity.class);
                            DIYInstallationState.setLocationId(device.getDeviceId());
                            break;
                        }

                    default:
                        break;
                }
            }
        });

        mBleScanManager.setScanTimeoutListener(new BleScanManager.ScanTimeoutListener() {
            @Override
            public void onTimeout() {

                stopScan();

                startIntent(MadAirEnrollPairFailActivity.class);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

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


    private void initView() {
        super.initTitleView(true, getString(R.string.mad_air_enroll_manual_select_title), EnrollConstants.TOTAL_THREE_STEP,
                EnrollConstants.STEP_TWO, getString(R.string.mad_air_enroll_manual_select_desc), false);

        mPairButton = (Button) findViewById(R.id.mad_air_pair_btn);
        mPairingButton = (EnrollLoadingButton) findViewById(R.id.pairing_id);
        mPairingButton.initLoadingText("", getString(R.string.mad_air_enroll_manual_select_pairing));
        mPairingButton.setButtonStatus(false, true);

        mScanListView = (ListView) findViewById(R.id.scan_device_list);

        mScanRotateImageView = (ImageView) findViewById(R.id.scan_rotate_iv);
        startRotateAnimation();

        mBackFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopScan();

                backIntent();
            }
        });

    }

    public void doClick(View v) {
        if (v.getId() == R.id.mad_air_pair_btn) {

            mBleScanManager.startScanTimeoutTimer();
            String address = mBleMap.get(mSelectedBleName);
            int deviceType = mBleTypeMap.get(mSelectedBleName);
            if (StringUtil.isEmpty(address) || MadAirDeviceModelSharedPreference.hasDevice(address)) {
                mAlertDialog = MessageBox.createSimpleDialog(this, "", getString(R.string.mad_ari_enrolled_already),
                        getString(R.string.ok), quitEnroll);
                stopScan();
            } else {

                EnrollScanEntity.getEntityInstance().setmModel(HPlusConstants.MAD_AIR_MODEL_WHITE);
                EnrollScanEntity.getEntityInstance().setmMacID(address);
                EnrollScanEntity.getEntityInstance().setmDeviceType(deviceType);
                BLEManager.getInstance().connectBle(address);

                mPairingButton.setVisibility(View.VISIBLE);
                mPairButton.setVisibility(View.GONE);
            }
        }
    }

    private void initBleListView() {
        mBleNameList.clear();
        mScanDeviceListAdapter = new ScanningDeviceListAdapter(this, mBleNameList);
        mScanDeviceListAdapter.clearAllSelectImageView();
        mScanListView.setAdapter(mScanDeviceListAdapter);
        mScanListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mPairingButton.getVisibility() == View.GONE) {
                    mSelectedBleName = mScanDeviceListAdapter.getItem(position);
                    mScanDeviceListAdapter.clearAllSelectImageView();
                    mScanDeviceListAdapter.setSelectImageViewVisible(position);

                    mPairButton.setBackgroundResource(R.drawable.enroll_big_btn);
                    mPairButton.setClickable(true);
                }

            }
        });
    }

    private void updateBleListView() {

        mBleScanManager.setScanListener(new BleScanManager.ScanListener() {
            @Override
            public void onScan(final BluetoothDevice device, final byte[] scanRecord) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (device != null) {
                            String name = device.getName();
                            String address = device.getAddress();

                            if (isBleNameValid(name)) {
                                mBleMap.put(name, address);
                                if (scanRecord != null && scanRecord.length > 25)
                                    mBleTypeMap.put(name, (int)scanRecord[25]);
                                mBleNameList.add(name);
                                mScanDeviceListAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

            }
        });
    }

    private boolean isBleNameValid(String bleName) {

        if (bleName == null || bleName.isEmpty())
            return false;

        if (mBleNameList.contains(bleName))
            return false;

        if (!bleName.contains(MAD_AIR_BLE_NAME))
            return false;

        return true;
    }

    @Override
    protected void startScan() {
        mBleScanManager.startBleScan();
    }


    private void startRotateAnimation() {
        Animation operatingAnim = AnimationUtils.loadAnimation(MadAirEnrollManualSelectActivity.this, R.anim.enroll_rescan_rotate);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        mScanRotateImageView.startAnimation(operatingAnim);
    }

}
