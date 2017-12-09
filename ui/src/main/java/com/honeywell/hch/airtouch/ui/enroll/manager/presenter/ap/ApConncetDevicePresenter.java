package com.honeywell.hch.airtouch.ui.enroll.manager.presenter.ap;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.WifiUtil;
import com.honeywell.hch.airtouch.plateform.ap.WAPIRouter;
import com.honeywell.hch.airtouch.plateform.ap.model.WAPIDeviceResponse;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.enroll.interfacefile.IApConncetDeviceView;
import com.honeywell.hch.airtouch.ui.enroll.manager.EnrollDeviceManager;
import com.honeywell.hch.airtouch.ui.enroll.manager.SmartLinkUiManager;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 14/10/16.
 */
public class ApConncetDevicePresenter {

    private final String TAG = "RegisterDevicePresenter";


    public enum ConfigureState {
        WIFI_OFF, WIFI_ON, SCANNING, RESCAN, NO_STAT_NETWORKS,
        MULTIPLE_STAT_NETWORKS, STAT_NETWORK_SELECTED, CONNECTING, CONNECTED, CONFIGURED
    }

    private IApConncetDeviceView iApConncetDeviceView;
    private EnrollDeviceManager mEnrollDeviceManager;
    private WifiManager mWifi;
    private Context mContext;
    private ArrayList<ScanResult> mScanResults = new ArrayList<>();
    private ScanResult mSelectedScanResult;
    private ConfigureState mConfigureState = ConfigureState.WIFI_OFF;
    private int noticeCount = 0;
    private boolean isScanning = false;
    private SmartLinkUiManager mSmartLinkUiManager;
    private boolean isActivityDestoryed = false;


    public ApConncetDevicePresenter(Context context, IApConncetDeviceView iApConncetDeviceView) {
        mContext = context;
        this.iApConncetDeviceView = iApConncetDeviceView;
        mWifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        mSmartLinkUiManager = new SmartLinkUiManager();
        mEnrollDeviceManager = new EnrollDeviceManager(mContext);
        initEnrollDeviceManager();
    }

    public void setConfigureStateOff() {
        if (mConfigureState != ConfigureState.CONNECTED) {
            mConfigureState = ConfigureState.WIFI_OFF;
            iApConncetDeviceView.wifiIsOff();
        }
    }

    public void setConfigureStateOn() {
        if (mConfigureState == ConfigureState.WIFI_OFF) {
            mConfigureState = ConfigureState.WIFI_ON;
        }
    }

    public void setConfigureStateWhenResume(ConfigureState configureState){
        mConfigureState = configureState;
    }


    public void addScanResult() {
        List<ScanResult> scanResults = mWifi.getScanResults();
        mScanResults.clear();

            /*
             *   Smart phone is scanning Air Touch's SSID,
             *   if found one, connect to it and go to the next step.
             *   Ignore others similar SSID.
             */
        for (ScanResult scanResult : scanResults) {
            if (EnrollScanEntity.getEntityInstance().getDeviceName() == R.string.airtouch_x_str) {
                if (scanResult.SSID.contains(HPlusConstants.AIR_TOUCH_X_SSID)) {
                    mScanResults.add(scanResult);
                }
            } else if (EnrollScanEntity.getEntityInstance().getDeviceName() == R.string.airtouch_s_str) {
                if (scanResult.SSID.contains(HPlusConstants.AIR_TOUCH_S_SSID)) {
                    mScanResults.add(scanResult);
                }
            } else if (EnrollScanEntity.getEntityInstance().getDeviceName() == R.string.airtouch_p_str) {
                if (scanResult.SSID.contains(HPlusConstants.AIR_TOUCH_P_SSID)) {
                    mScanResults.add(scanResult);
                }
            } else if (EnrollScanEntity.getEntityInstance().getDeviceName() == R.string.airtouch_ffac_str) {
                if (scanResult.SSID.contains(HPlusConstants.AIR_TOUCH_FFAC_SSID) && AppManager.isEnterPriseVersion()) {
                    mScanResults.add(scanResult);
                }
            } else if (EnrollScanEntity.getEntityInstance().getDeviceName() == R.string.smart_ro_600_str) {
                if (scanResult.SSID.contains(HPlusConstants.AQUA_TOUCH_600_SSID)) {
                    mScanResults.add(scanResult);
                }
            } else if (EnrollScanEntity.getEntityInstance().getDeviceName() == R.string.airtouch_xcompact_str) {
                if (scanResult.SSID.contains(HPlusConstants.AIR_TOUCH_XCOMPACT_SSID)) {
                    mScanResults.add(scanResult);
                }

            } else if (EnrollScanEntity.getEntityInstance().getDeviceName() == R.string.smart_ro_400_str) {
                if (scanResult.SSID.contains(HPlusConstants.AQUA_TOUCH_400_SSID)) {
                    mScanResults.add(scanResult);
                }
            } else if (EnrollScanEntity.getEntityInstance().getDeviceName() == R.string.airtouch_x3compact_str) {
                if (scanResult.SSID.contains(HPlusConstants.AIR_TOUCH_X3COMPACT_SSID)) {
                    mScanResults.add(scanResult);
                }
            } else if (EnrollScanEntity.getEntityInstance().getDeviceName() == R.string.smart_ro_100_str) {
                if (scanResult.SSID.contains(HPlusConstants.AQUA_TOUCH_100_SSID)) {
                    mScanResults.add(scanResult);
                }
            } else if (EnrollScanEntity.getEntityInstance().getDeviceName() == R.string.smart_ro_75_str) {
                if (scanResult.SSID.contains(HPlusConstants.AQUA_TOUCH_75_SSID)) {
                    mScanResults.add(scanResult);
                }
            } else if (EnrollScanEntity.getEntityInstance().getDeviceName() == R.string.smart_ro_50_str) {
                if (scanResult.SSID.contains(HPlusConstants.AQUA_TOUCH_50_SSID)) {
                    mScanResults.add(scanResult);
                }
            }
        }
        // If there is only one automatically connect to that scan result.
        if (mScanResults.size() == 1) {
            mSelectedScanResult = mScanResults.get(0);
            mConfigureState = ConfigureState.STAT_NETWORK_SELECTED;
        } else if (mScanResults.size() == 0) {
            noticeCount++;
            if (noticeCount == 10) {
                noticeCount = 0;
                iApConncetDeviceView.settingDialog();
            }
            mConfigureState = ConfigureState.RESCAN;
        } else if (mScanResults.size() >= 2) {
            ArrayList<WAPIRouter> mWAPIRouters = new ArrayList<>();
            for (ScanResult scanResult : mScanResults) {
                WAPIRouter router = new WAPIRouter();
                router.setSSID(scanResult.SSID);
                mWAPIRouters.add(router);
            }
            iApConncetDeviceView.showNetWorkListItem(mWAPIRouters);
            mConfigureState = ConfigureState.CONFIGURED;
        }

        saveSsid();

        loadData();
    }

    private void saveSsid() {
        WifiInfo wifiInfo = mWifi.getConnectionInfo();
        if (wifiInfo != null) {
            String ssid = wifiInfo.getSSID();
            DIYInstallationState.setmHomeConnectedSsid(ssid);
        }
    }

    public void loadData() {

        switch (mConfigureState) {
            case WIFI_OFF:
                iApConncetDeviceView.wifiIsOff();
                break;
            case WIFI_ON:
            case SCANNING:
                startScan();
                break;
            case RESCAN:
                rescan();
                break;
            case STAT_NETWORK_SELECTED:
            case CONNECTING:
                LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "CONNECTING...");
                connectToScanResult(mSelectedScanResult);
                break;
            case CONNECTED:
                LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "CONNECTED...");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(4500);
                            mEnrollDeviceManager.connectDevice();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();

                break;
            case NO_STAT_NETWORKS:
                LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "NO_STAT_NETWORKS...");
                break;
            case CONFIGURED:
                LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "More than 1 device found...");
                break;
            default:
                break;
        }
    }


    private void startScan() {
        mConfigureState = ConfigureState.SCANNING;
        mWifi.startScan();
    }

    public void rescan() {
        if (!isScanning)
            return;

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mConfigureState = ConfigureState.SCANNING;
                mWifi.startScan();
            }

        }).start();

    }

    private void connectToScanResult(ScanResult scanResult) {

        mConfigureState = ConfigureState.CONNECTING;

        if (WifiUtil.reConnectWifiInAndroidM(scanResult, mWifi)) {
            connectionCompleted();
        }
    }

    private void connectionCompleted() {
        mConfigureState = ConfigureState.CONNECTED;
        loadData();
    }

    public String getDeviceWifiStr() {
        return mEnrollDeviceManager.getDeviceWifiStr();
    }

    public void dealWithScanResultsReceiver() {
        if (!WifiUtil.isWifiOpen(mContext)
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iApConncetDeviceView.wifiIsOff();
            return;
        }
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "mConfigureState: " + mConfigureState);
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "noticeCount: " + noticeCount);
        if (mConfigureState != ConfigureState.SCANNING) {
            return;
        }

        try {
            addScanResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initEnrollDeviceManager() {
        mEnrollDeviceManager.processErrorCode();
        mEnrollDeviceManager.setFinishCallback(
                new EnrollDeviceManager.FinishCallback() {
                    @Override
                    public void onFinish(boolean isUpdateWifiSuccess) {
                        if (!isActivityDestoryed) {
                            iApConncetDeviceView.connectWifiSuccess();
                            WAPIDeviceResponse mWAPIDeviceResponse = DIYInstallationState.getWAPIDeviceResponse();
                            EnrollScanEntity.getEntityInstance().setmMacID(mWAPIDeviceResponse.getMacID());
                            if (mSmartLinkUiManager.checkIfSelfDeviceAlreadyEnrolled(mWAPIDeviceResponse.getMacID())
                                    && !EnrollScanEntity.getEntityInstance().isFromTimeout()) {
                                iApConncetDeviceView.dealWithSelfDeviceAlreadyEnrolled();
                            } else if (mSmartLinkUiManager.checkIfAuthDeviceAlreadyEnrolled(mWAPIDeviceResponse.getMacID())
                                    && !EnrollScanEntity.getEntityInstance().isFromTimeout()) {
                                iApConncetDeviceView.dealWithAuthDeviceAlreadyEnrolled();
                            } else {
                                iApConncetDeviceView.dealWithConnectDeviceSuccess();
                            }

                        }

                    }
                }

        );
        mEnrollDeviceManager.setErrorCallback(
                new EnrollDeviceManager.ErrorCallback() {
                    @Override
                    public void onError(ResponseResult responseResult, String errorMsg) {
                        if (!isActivityDestoryed) {
                            iApConncetDeviceView.disableNextButton();
                            if (responseResult.getResponseCode() == StatusCode.RE_CONNECT_DEVICE_WIFI_ERROR) {
                                iApConncetDeviceView.reConncetDeviceWifiError();
                            } else if (responseResult.getResponseCode() == StatusCode.CONNECT_WIFI_BUT_CANNOT_COMMUNICATION) {
                                iApConncetDeviceView.connectWifiButCantCommunication();
                            } else if (responseResult.getResponseCode() == StatusCode.CONNECT_WIFI_BUT_SOCKETTIMEOUT) {
                                iApConncetDeviceView.connectWifiButSocektTimeOut();
                            } else {
                                String msg = errorMsg == null ? mContext.getResources().getString(R.string.device_wifi_off) : errorMsg;
                                iApConncetDeviceView.connectWifiError(msg);
                            }
                        }
                    }
                }
        );
    }

    public void setIsScanning(boolean isScanning) {
        this.isScanning = isScanning;
    }

    public void setIsActivityDestoryed(boolean isActivityDestoryed) {
        this.isActivityDestoryed = isActivityDestoryed;
    }

    public void dealWithScanResultClickListener(WAPIRouter wapiRouter) {
        for (int i = 0; i < mScanResults.size(); i++) {
            if (mScanResults.get(i).SSID.equals(wapiRouter.getSSID())) {
                mSelectedScanResult = mScanResults.get(i);
                mConfigureState = ConfigureState.STAT_NETWORK_SELECTED;
            }
        }
        loadData();
    }
}
