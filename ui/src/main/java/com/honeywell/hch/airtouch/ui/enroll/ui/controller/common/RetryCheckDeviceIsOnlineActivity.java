package com.honeywell.hch.airtouch.ui.enroll.ui.controller.common;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;

import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.task.RefreshSessionTask;
import com.honeywell.hch.airtouch.plateform.umeng.UmengUtil;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.control.manager.umeng.UmengUiManager;
import com.honeywell.hch.airtouch.ui.enroll.constant.EnrollConstants;
import com.honeywell.hch.airtouch.ui.enroll.manager.EnrollDeviceManager;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.afterplay.EnrollResultActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.afterplay.EnrollSelectedLocationActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.view.EnrollLoadingButton;

/**
 * Created by h127856 on 16/10/17.
 */
public class RetryCheckDeviceIsOnlineActivity extends EnrollBaseActivity {

    private static final int STATUS_SETTINGS = 1;
    private static final int STATUS_NEXT = 2;
    private static final int STATUS_CONNECTED = 3;

    private int mBtnStatus = STATUS_SETTINGS;
    private RefreshSessionTask mRefreshSessionTask = new RefreshSessionTask();
    private BroadcastReceiver mBroadcastReceiver;
    private EnrollLoadingButton mButton;
    private EnrollDeviceManager mEnrollManager;
    private AlertDialog mAlertDlalog;

    private class WifiCheckBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && HPlusConstants.REFRESH_SESSION_ACTION.equals(intent.getAction()) && mBtnStatus != STATUS_CONNECTED) {
                boolean isHasConenct = intent.getBooleanExtra(HPlusConstants.HAS_WIFI_CONNECTED, false);
                if (isHasConenct) {
                    mBtnStatus = STATUS_NEXT;
                    mButton.initLoadingText(getString(R.string.enroll_next), getString(R.string.enroll_connecting));
                    mButton.setButtonStatus(true, false);
                    initTitle();
                } else {
                    mBtnStatus = STATUS_SETTINGS;
                    mButton.initLoadingText(getString(R.string.go_to_setting), getString(R.string.go_to_setting));
                    mButton.setButtonStatus(true, false);
                    initTitle();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ap_timeout);
        initStatusBar();
        initView();
        initDragDownManager(R.id.root_view_id);
        mEnrollManager = new EnrollDeviceManager(this);

        registerUserAliveChangedReceiver();
        checkWifiIsConnectInetnet();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
        mRefreshSessionTask.setIsAbort(true);

        if (mAlertDlalog != null) {
            mAlertDlalog.dismiss();
        }

    }

    private void initView() {
        initTitle();
        mButton = (EnrollLoadingButton) findViewById(R.id.setting_id);
        mButton.initLoadingText(getString(R.string.go_to_setting), getString(R.string.go_to_setting));
        mButton.setButtonStatus(true, false);
        mButton.getmRootRl().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBtnStatus == STATUS_SETTINGS) {
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    startActivity(intent);
                } else if (mBtnStatus == STATUS_NEXT) {
                    mRefreshSessionTask.setIsAbort(true);
                    mBtnStatus = STATUS_CONNECTED;
                    mButton.initLoadingText(getString(R.string.enroll_next), getString(R.string.enroll_connecting));
                    mButton.setButtonStatus(false, true);
                    checkDeviceIsOnline();
                }

            }
        });
    }

    private void initTitle() {
        int totalStep = DIYInstallationState.getIsDeviceAlreadyEnrolled() ? EnrollConstants.TOTAL_TWO_STEP : EnrollConstants.TOTAL_FOUR_STEP;

        if (mBtnStatus == STATUS_SETTINGS) {
            super.initTitleView(false, getString(R.string.enroll_connection_timeout), totalStep, EnrollConstants.STEP_TWO, getString(R.string.connect_wifi_msg),false);
        } else {
            super.initTitleView(false, getString(R.string.enroll_connection_timeout), totalStep, EnrollConstants.STEP_TWO, getString(R.string.connect_wifi_msg2),false);

        }
    }

    /**
     * 用于测试wifi是否可以连上外网
     */
    public void checkWifiIsConnectInetnet() {
        AsyncTaskExecutorUtil.executeAsyncTask(mRefreshSessionTask);
    }

    private void registerUserAliveChangedReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(HPlusConstants.REFRESH_SESSION_ACTION);
        mBroadcastReceiver = new WifiCheckBroadcast();
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    IActivityReceive iActivityReceive = new IActivityReceive() {
        @Override
        public void onReceive(ResponseResult responseResult) {
            switch (responseResult.getRequestId()) {
                case CHECK_MAC:
                    if (responseResult.getResponseCode() == StatusCode.NETWORK_ERROR) {
                        checkDeviceError();
                    } else {
                        if (responseResult.getFlag() == HPlusConstants.CHECK_MAC_ALIVE) {
                            deviceIsOnlie();
                        } else {
                            checkDeviceError();
                            UmengUtil.enrollEvent(UmengUiManager.getEnrollProductName(), UmengUtil.EnrollEventType.ENROLL_FAIL, "check_mac_error");
                        }
                    }

                    return;

                default:
                    break;
            }

        }
    };

    private void checkDeviceIsOnline() {
        mEnrollManager.checkDeviceIsOnline(iActivityReceive);
    }

    private void deviceIsOnlie() {
        if (DIYInstallationState.getIsDeviceAlreadyEnrolled()) {
            goToResultActivity(EnrollConstants.ENROLL_RESULT_UPDATE_WIFI_SUCCESS);
        } else {
            Intent intent = new Intent();
            intent.setClass(this, EnrollSelectedLocationActivity.class);
            gotoActivityWithIntent(intent, false);
        }
    }


    private void goToResultActivity(int enrollResultType) {

        Intent intent = new Intent();
        intent.putExtra(EnrollConstants.ENROLL_RESULT, enrollResultType);
        intent.setClass(this, EnrollResultActivity.class);
        gotoActivityWithIntent(intent, false);
    }

    private void checkDeviceError() {
        if (EnrollScanEntity.getEntityInstance().isApMode()) {
            showRetryActionWithReconnectWifi(getString(R.string.device_not_connect_phone));
        } else {
            showRetryAction(getString(R.string.device_not_connect_phone));
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
