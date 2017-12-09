package com.honeywell.hch.airtouch.ui.enroll.ui.controller.ap;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.library.util.WifiUtil;
import com.honeywell.hch.airtouch.plateform.ap.WAPIRouter;
import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.plateform.permission.Permission;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.enroll.constant.EnrollConstants;
import com.honeywell.hch.airtouch.ui.enroll.interfacefile.IApConncetDeviceView;
import com.honeywell.hch.airtouch.ui.enroll.manager.presenter.ap.ApConncetDevicePresenter;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.view.EnrollLoadingButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Enrollment Step 1 - SmartPhone find Air Touch's SSID.
 * Scanning surrounding every 1s.
 */
public class ApPhoneToDeviceActivity extends EnrollBaseActivity implements IApConncetDeviceView {

    private static final String TAG = "AirTouchEnrollWelcome";
    private ArrayList<WAPIRouter> mWAPIRouters;
    private ListView mNetworkList;
    private NetworkListAdapter networkListAdapter;
    private TextView mTitleTextView;
    private TextView mTitleContentTv;
    private static final String THREE_SCONDS = "3";
    private static final String TEN_SCONDS = "10";
    private String mSeconds = TEN_SCONDS;
    private String wifi;
    private TextView mWifiTv;
    private ImageView mChooseIv;
    private EnrollScanEntity mEnrollScanEntity;
    private EnrollLoadingButton mNextButton;
    private ApConncetDevicePresenter mApConncetDevicePresenter;
    private TextView mWlanTv;
    private int showDialogIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.TAG = TAG;
        setContentView(R.layout.ap_activity_device_wifi);
        init();
        initTitle();
        initListener();
        initData();
        initReceiver();
        initStatusBar();
        initDragDownManager(R.id.root_view_id);
    }

    private void init() {
        initPresenter();
        mNetworkList = (ListView) findViewById(R.id.wifi_list);
        networkListAdapter = new NetworkListAdapter(ApPhoneToDeviceActivity.this);
        mWifiTv = (TextView) findViewById(R.id.ap_device_wifi_tv);
        mChooseIv = (ImageView) findViewById(R.id.ap_device_choose_iv);
        mNextButton = (EnrollLoadingButton) findViewById(R.id.nextBtn_id);
        mWlanTv = (TextView) findViewById(R.id.ap_device_wlan_tv);
    }

    private void initPresenter() {
        if (mApConncetDevicePresenter == null) {
            mApConncetDevicePresenter = new ApConncetDevicePresenter(mContext, this);
        }
    }

    private void initTitle() {
        mTitleTextView = (TextView) findViewById(R.id.title_textview_id);
        mTitleTextView.setText(R.string.enroll_title_welcome);
        mTitleContentTv = (TextView) findViewById(R.id.input_tip_id);
        mTitleContentTv.setText(R.string.enroll_connect_phone_to_device);
        mTitleContentTv.setVisibility(View.VISIBLE);
        int totalStep = DIYInstallationState.getIsDeviceAlreadyEnrolled() ? EnrollConstants.TOTAL_TWO_STEP : EnrollConstants.TOTAL_FOUR_STEP;
        initEnrollStepView(true, true, totalStep, EnrollConstants.STEP_TWO);
    }

    private void initListener() {
        mNetworkList.setOnItemClickListener(mScanResultClickListener);
    }

    private void initReceiver() {
        //do something, permission was previously granted; or legacy device
        IntentFilter scanResultsFilter = new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(mScanResultsReceiver, scanResultsFilter);
    }

    private void initData() {
        String country = UserInfoSharePreference.getCountryCode();
        if (StringUtil.isEmpty(country) || country.equals(HPlusConstants.CHINA_CODE)) {
            mWlanTv.setText(getString(R.string.enroll_wlan));
        } else {
            mWlanTv.setText(getString(R.string.enroll_wifi));
        }
        mEnrollScanEntity = EnrollScanEntity.getEntityInstance();
        if (DeviceType.isApMode(EnrollScanEntity.getEntityInstance().getmDeviceType())) {
            mSeconds = THREE_SCONDS;
        }
        wifi = mApConncetDevicePresenter.getDeviceWifiStr();
        mNextButton.initLoadingText(getResources().getString(R.string.samart_next_btn), getResources().getString(R.string.enroll_connecting));
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "deviceName: " + getString(EnrollScanEntity.getEntityInstance().getDeviceName()));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWifiTv.setText(mEnrollScanEntity.getEnrollFeature().getEnrollDeviceWifiPre() + getString(R.string.enroll_xxx));
        mHPlusPermission.checkAndRequestPermission(Permission.PermissionCodes.LOCATION_SERVICE_REQUEST_CODE, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mApConncetDevicePresenter.setIsScanning(false);
    }

//    @Override
//    protected  void onStop(){
//        super.onStop();
//        mNextButton.setButtonStatus(true,false);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mApConncetDevicePresenter.setIsActivityDestoryed(true);
        if (mScanResultsReceiver != null) {
            unregisterReceiver(mScanResultsReceiver);
            mScanResultsReceiver = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case Permission.PermissionCodes.LOCATION_SERVICE_REQUEST_CODE:

                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        showHasPermissionAndGpsOpen();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    noLocatedPermission();
                }
                break;
        }
    }


    private BroadcastReceiver mScanResultsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            mApConncetDevicePresenter.dealWithScanResultsReceiver();
        }
    };

    private void startLoadingButton() {
        mNextButton.setButtonStatus(false, true);
    }


    protected MessageBox.MyOnClick update = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            startIntent(ApEnrollConnectWifiActivity.class);
        }
    };


    private class NetworkListAdapter extends ArrayAdapter<WAPIRouter> {

        public NetworkListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public WAPIRouter getItem(int position) {
            return mWAPIRouters.get(position);
        }

        @Override
        public int getCount() {
            if (mWAPIRouters == null) {
                return 0;
            }
            return mWAPIRouters.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_network,
                        parent, false);
            }

            WAPIRouter wapiRouter = getItem(position);

            if (wapiRouter.getSSID() != null) {
                TextView ssidTextView = (TextView) convertView
                        .findViewById(R.id.list_item_network_text);
                ssidTextView.setText(wapiRouter.getSSID());
            }

            Drawable drawable = null;
            if (wapiRouter.isLocked()) {
                drawable = getContext().getResources().getDrawable(R.drawable.wifi_lock);
            }

            ImageView image = (ImageView) convertView
                    .findViewById(R.id.list_item_network_lock_image);
            image.setImageDrawable(drawable);
            return convertView;
        }

    }

    private AdapterView.OnItemClickListener mScanResultClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            WAPIRouter wapiRouter = (WAPIRouter) parent.getItemAtPosition(position);
            mApConncetDevicePresenter.dealWithScanResultClickListener(wapiRouter);
            mNetworkList.setVisibility(View.GONE);
        }
    };


    private void showHasPermissionAndGpsOpen() {
        mApConncetDevicePresenter.setIsScanning(true);

        if (WifiUtil.isWifiOpen(mContext)) {
            mApConncetDevicePresenter.setConfigureStateOn();

            //解决：手机连接一个正确的wifi后，切换回去又连接一个错误的wifi，因为状态已经是Connected，所以不重新scanning，
            // 导致界面显示已经连接的wifi不是要enroll的设备wifi类型。
            // 所以每次onResume重新设置为rescan
            mApConncetDevicePresenter.setConfigureStateWhenResume(NetWorkUtil.isWifiAvailable(this) ?
                    ApConncetDevicePresenter.ConfigureState.WIFI_ON : ApConncetDevicePresenter.ConfigureState.WIFI_OFF);
            mApConncetDevicePresenter.loadData();
        } else {
            wifiIsOff();
        }

        mChooseIv.setVisibility(View.INVISIBLE);
        startLoadingButton();

    }

    @Override
    public void onPermissionGranted(int permissionCode) {
        if (!mHPlusPermission.isGPSOPen(this) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            gpsNoOpen();
        } else {
            showHasPermissionAndGpsOpen();
        }
    }

    private void gpsNoOpen() {
        mAlertDialog = MessageBox.createTwoButtonDialog((Activity) mContext, null, getString(R.string.gps_no_open),
                getString(R.string.cancel), leftButton, getString(R.string.go_to_setting), rightButton);
    }

    private void noLocatedPermission() {
        MessageBox.createTwoButtonDialog((Activity) mContext, null, getString(R.string.no_located_permission_scantxt),
                getString(R.string.cancel), leftButton, getString(R.string.go_to_setting), goToSetting);

    }

    protected MessageBox.MyOnClick goToSetting = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            goToPermissionSetting();
            showDialogIndex =0;
        }
    };

    MessageBox.MyOnClick leftButton = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            backIntent();
        }
    };

    MessageBox.MyOnClick rightButton = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            if (mHPlusPermission != null) {
                mHPlusPermission.fourceOpenGPS(ApPhoneToDeviceActivity.this);
            }
        }
    };

    @Override
    public void onPermissionNotGranted(String[] permission, int permissionCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && showDialogIndex < 1) {
            this.requestPermissions(permission, permissionCode);
            showDialogIndex++;
        }
    }

    @Override
    public void onPermissionDenied(int permissionCode) {
//        noLocatedPermission();

    }

    public void doClick(View v) {
        if (v.getId() == R.id.enroll_back_layout) {
            backIntent();
        }
    }

    @Override
    public void settingDialog() {
        disMissDialog();
        mAlertDialog = MessageBox.createTwoButtonDialog((Activity) mContext, null, mContext.getString(R.string.connect_wifi_timeout, wifi, mSeconds),
                mContext.getString(R.string.enroll_btn_quite), quitAPEnroll, mContext.getString(R.string.enroll_btn_setting), new MessageBox.MyOnClick() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void showNetWorkListItem(ArrayList<WAPIRouter> wAPIRouters) {
        mNetworkList.setVisibility(View.VISIBLE);
        mWAPIRouters = wAPIRouters;
        mNetworkList.setAdapter(networkListAdapter);
    }

    @Override
    public void wifiIsOff() {

        if (mAlertDialog == null || !mAlertDialog.isShowing()) {
            mAlertDialog = MessageBox.createTwoButtonDialog((Activity) mContext, null, mContext.getString(R.string.connect_wifi_timeout, wifi, mSeconds),
                    mContext.getString(R.string.enroll_btn_quite), quitAPEnroll, mContext.getString(R.string.enroll_btn_setting), new MessageBox.MyOnClick() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(intent);
                        }
                    });
        }
    }

    @Override
    public void connectWifiSuccess() {
        String ssid = NetWorkUtil.updateWifiInfo(mContext);
        if (!"".equals(ssid)) {
            mWifiTv.setText(ssid);
        }
        mChooseIv.setVisibility(View.VISIBLE);
        mNextButton.setButtonStatus(true, false);
    }

    @Override
    public void disableNextButton() {
        mNextButton.setButtonStatus(false, false);
    }

    @Override
    public void dealWithSelfDeviceAlreadyEnrolled() {
        mNextButton.getmRootRl().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog = MessageBox.createTwoButtonDialog((Activity) mContext, null,
                        getString(R.string.device_register_id), getString(R.string.enroll_exit),
                        quitAPEnroll, getString(R.string.enroll_update), update);
            }
        });
    }

    @Override
    public void dealWithAuthDeviceAlreadyEnrolled() {
        mNextButton.getmRootRl().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageBox.createSimpleDialog((Activity) mContext, null,
                        getString(R.string.device_already_registered_authorize), getString(R.string.ok),
                        quitAPEnroll);
            }
        });
    }

    @Override
    public void dealWithConnectDeviceSuccess() {
        mNextButton.getmRootRl().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(ApEnrollConnectWifiActivity.class);
            }
        });
    }

    @Override
    public void reConncetDeviceWifiError() {
        if (mAlertDialog == null || !mAlertDialog.isShowing()) {
            String seconds = TEN_SCONDS;
            if (DeviceType.isApMode(EnrollScanEntity.getEntityInstance().getmDeviceType())) {
                seconds = THREE_SCONDS;
            }
            mAlertDialog = MessageBox.createTwoButtonDialog((Activity) mContext, null, mContext.getString(R.string.connect_wifi_timeout, wifi, seconds),
                    mContext.getString(R.string.enroll_btn_quite), quitAPEnroll, mContext.getString(R.string.enroll_btn_setting), new MessageBox.MyOnClick() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(intent);
                        }
                    });
        }

    }

    @Override
    public void connectWifiButCantCommunication() {
        if (mAlertDialog == null || !mAlertDialog.isShowing()) {
            mAlertDialog = MessageBox.createTwoButtonDialog((Activity) mContext, null, mContext.getString(R.string.enroll_commnunication_other_channel),
                    mContext.getString(R.string.enroll_btn_quite), quitAPEnroll, mContext.getString(R.string.enroll_btn_setting), new MessageBox.MyOnClick() {
                        @Override
                        public void onClick(View v) {
                            goToPermissionSetting();
                        }
                    });
        }


    }

    @Override
    public void connectWifiButSocektTimeOut() {
        if (mAlertDialog == null || !mAlertDialog.isShowing()) {
            String seconds = TEN_SCONDS;
            if (DeviceType.isApMode(EnrollScanEntity.getEntityInstance().getmDeviceType())) {
                seconds = THREE_SCONDS;
            }
            showRetryActionWithReconnectWifi(mContext.getString(R.string.enroll_socket_timeout, seconds));
        }

    }

    @Override
    public void connectWifiError(String errorMsg) {
        mAlertDialog = MessageBox.createSimpleDialog((Activity) mContext, null,
                errorMsg, getString(R.string.enroll_exit), quitAPEnroll);
    }

    protected void dealNoNetwork() {
        initPresenter();
        mApConncetDevicePresenter.setConfigureStateOff();
    }

    protected void dealNetworkConnect() {
        initPresenter();
        mApConncetDevicePresenter.setConfigureStateOn();
    }
}
